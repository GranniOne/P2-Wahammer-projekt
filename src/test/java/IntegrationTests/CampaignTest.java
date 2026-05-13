package IntegrationTests;

import static org.junit.jupiter.api.Assertions.*;

import com.P2.warhammer.Application;
import com.P2.warhammer.campaigns.Campaign;
import com.P2.warhammer.campaigns.CampaignRepository;
import com.P2.warhammer.characteristics.Characteristic;
import com.P2.warhammer.characters.Character;
import com.P2.warhammer.characters.CharacterRepository;
import com.P2.warhammer.users.User;
import com.P2.warhammer.users.UserRepository;
import com.P2.warhammer.users.UserService;
import com.P2.warhammer.utilities.Utilities;
import com.P2.warhammer.views.CampaignCreatorView;
import com.P2.warhammer.views.CampaignView;
import com.P2.warhammer.views.DashBoard;
import com.vaadin.browserless.SpringBrowserlessTest;
import com.vaadin.browserless.TreeOnFailureExtension;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import java.util.ArrayList;
import java.util.List;
import lombok.With;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.xml.stream.events.Characters;

@Testcontainers
@ExtendWith(TreeOnFailureExtension.class)
@SpringBootTest(classes = Application.class)
public class CampaignTest extends SpringBrowserlessTest {
    @Container public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.0");

    @Autowired private UserService userService;

    @Autowired private UserRepository userRepository;

    @Autowired CharacterRepository characterRepository;

    @Autowired CampaignRepository campaignRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.mongodb.database", () -> "testdb");
    }

    // TODO: ryk setup ned i tilhørende test udover usersne (vaadin session kan ikke klare sig uden usersne bliver sat op først)
    @BeforeEach
    @Override
    protected void initVaadinEnvironment() {
        userRepository.deleteAll();
        User dennis = new User("Dennis", "dennis@gmail.com", "12345678");
        userRepository.save(dennis);
        User marley = new User("Marley", "marley@gmail.com", "12345678");
        userRepository.save(marley);

        characterRepository.deleteAll();
        List<Characteristic> characteristics = new ArrayList<Characteristic>();
        characterRepository.save(new Character("Mukibuki", marley, null, 12, 12, characteristics, 12));

        campaignRepository.deleteAll();
        // Oprette en test kampagne som Marley er med i og hvor
        // Dennis er gamemaster
        Campaign testCampaign = new Campaign();
        testCampaign.setName("Test Campaign");
        testCampaign.setGameMaster(dennis);
        ArrayList<User> players = new ArrayList<>();
        players.add(marley);
        testCampaign.setPlayers(players);
        campaignRepository.save(testCampaign);

        super.initVaadinEnvironment();
    }
    @Test
    @WithMockUser(username = "dennis@gmail.com")
    public void testCampaignCreation() {
        campaignRepository.deleteAll();
        // tjekker repo er tomt
        List<Campaign> campaigns = campaignRepository.findAll();
        assertTrue(campaigns.isEmpty());

        // user går ind i kampagnekreatør
        CampaignCreatorView campaignCreatorView = navigate(CampaignCreatorView.class);
        TextField campaignNameTextField = $(CampaignCreatorView.class).thenOnFirst(TextField.class).single();
        test(campaignNameTextField).setValue("Test Campaign");

        EmailField emailField = $(CampaignCreatorView.class).thenOnFirst(EmailField.class).single();
        test(emailField).setValue("marley@gmail.com");

        Button addUserButton = $(Button.class).withText("Add").single();
        test(addUserButton).click();

        test($(Button.class).withText("Save").single()).click();

        // henter kampagne og ser om den svarer til den gemte
        campaigns = campaignRepository.findAll();
        Campaign savedCampaign = campaigns.get(0);
        assertEquals("Test Campaign", savedCampaign.getName());
        assertEquals("marley@gmail.com", savedCampaign.getPlayers().getFirst().getEmail());
        // tjekker om gamemaster er samme som bruger der har
        // sessionen (Den der lige har oprettet)
        assertEquals(savedCampaign.getGameMaster().getId(), Utilities.getUserFromAuthentication().getId());
    }

    @Test
    @WithMockUser(username = "marley@gmail.com")
    public void testUserAddingCharacterToCampaign() {
        // Først tjek den eneste kampagne i repo ikke har nogen
        // karakterer
        assertTrue(campaignRepository.findAll().getFirst().getCharacters().isEmpty());

        // Marley navigerer til campaign view for at tilføje en
        // karakter
        DashBoard dashBoard = navigate(DashBoard.class);
        test($(Button.class, $(Div.class).withClassName("CampaignCards").single()).withTextContaining("View").single())
                .click();
        test($(ComboBox.class).single()).selectItem("Mukibuki");
        test($(Button.class).withClassName("add-character").single()).click();

        // Tjekker om karakter er tilføjet til kampagne
        assertFalse(campaignRepository.findAll().getFirst().getCharacters().isEmpty());
        // tjekker om karakter i kampagne er den samme som
        // karakteren i repo der er blevet tilføjet
        assertEquals(campaignRepository.findAll().getFirst().getCharacters().getFirst().getId(),
                characterRepository.findAll().getFirst().getId());
    }

    @Test
    @WithMockUser("marley@gmail.com")
    public void testCharactersVisibleForGamemaster(){
        campaignRepository.deleteAll();
        characterRepository.deleteAll();

        Campaign testCampaign = new Campaign();
        testCampaign.setName("Destroyer campaign");
        testCampaign.setGameMaster(userRepository.findUserByEmail("marley@gmail.com"));

        // generer 5 chars, de bliver gemt i repoet så de kan få et document id af mongo, så campaign can lave documentrefs
        for(int i = 0; i < 5; i++){
            Character character = new Character(
                    String.format("Mukibuki%d", i),
                    userRepository.findUserByEmail("dennis@gmail.com"),
                    userRepository.findUserByEmail("marley@gmail.com"),
                    12, 12, new ArrayList<>(), 12);

            characterRepository.save(character);
        }
        List<Character> charactersList = characterRepository.findAll();

        testCampaign.setCharacters(charactersList);

        campaignRepository.save(testCampaign);

        // genindlæs dashboard side efter databasen er ændret ovenover
        UI.getCurrent().getPage().reload();

        // Navigate to campaign view
        test($(Button.class, $(HorizontalLayout.class).withClassName("campaign-card").single()).withCaption("View").single()).click();

        //Tjekker vi er på campaign view
        assertTrue($(CampaignView.class).exists());

        // Tjekker om alle spans med karakternavne Mukibuki 1-5 er visible for gm
        for(int i = 0; i < 5; i++){
            assertTrue($(Span.class, $(CampaignView.class).single()).withText(String.format("Mukibuki%d", i)).single().isVisible());
        }
        // TODO: måske ogs lige tjek om om man kan view chars side
    }

    @Test
    @WithMockUser("dennis@gmail.com")
    public void testFiveCampaignLimit(){
        // opsætter 4 ekstrea dummy campaigns til med marley som gm, udnytter setup kampagnen
        for(int i = 2; i < 6; i ++){
            Campaign campaign = campaignRepository.findAll().getFirst();
            campaign.setName(String.format("%s %d", campaign.getName(), i));
            campaign.setId(Integer.toString(i));
            campaignRepository.save(campaign);
        }

        // genindlæs dashboard side efter databasen er ændret ovenover
        UI.getCurrent().getPage().reload();
        test($(Button.class).withText("Add Campaign").single()).click();

        // tjekker om notifikation om limit er reached er tilstede
        if($(Notification.class).withText("Campaign limit of 5 reached").exists()){
            assertTrue($(Notification.class).withText("Campaign limit of 5 reached").single().isVisible());
        }

        // sikrer bruger ikke er blevet reroutet til campaign creator
        assertFalse($(CampaignCreatorView.class).exists());
        assertTrue($(DashBoard.class).single().isVisible());


    }
}
