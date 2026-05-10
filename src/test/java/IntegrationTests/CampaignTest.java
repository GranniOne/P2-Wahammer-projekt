package IntegrationTests;

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
import com.vaadin.browserless.SpringBrowserlessTest;
import com.vaadin.browserless.TreeOnFailureExtension;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@ExtendWith(TreeOnFailureExtension.class)
@SpringBootTest(classes = Application.class)
public class CampaignTest extends SpringBrowserlessTest {

    @Container
    public static MongoDBContainer mongoDBContainer =
            new MongoDBContainer("mongo:7.0.0");

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    CharacterRepository characterRepository;

    @Autowired
    CampaignRepository campaignRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.mongodb.database", () -> "testdb");
    }

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
        characterRepository.save(new Character("Mukibuki", dennis, null, 12, 12, characteristics));

        campaignRepository.deleteAll();
        super.initVaadinEnvironment();
    }

    @Test
    @WithMockUser(username = "dennis@gmail.com")
    protected void testCampaignCreation(){
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

        //henter kampagne og ser om den svarer til den gemte
        campaigns = campaignRepository.findAll();
        Campaign savedCampaign = campaigns.get(0);
        assertEquals("Test Campaign", savedCampaign.getName());
        assertEquals("marley@gmail.com", savedCampaign.getPlayers().getFirst().getEmail());
        assertTrue(savedCampaign.getGameMaster().toString().equals(Utilities.getUserFromAuthentication().toString()));
    }
}
