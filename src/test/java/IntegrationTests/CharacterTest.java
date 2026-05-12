package IntegrationTests;


import com.P2.warhammer.Application;
import com.P2.warhammer.Skills.Skill;
import com.P2.warhammer.Skills.SkillRepository;
import com.P2.warhammer.characteristics.Characteristic;
import com.P2.warhammer.characters.Character;
import com.P2.warhammer.characters.CharacterRepository;
import com.P2.warhammer.users.User;
import com.P2.warhammer.users.UserRepository;
import com.P2.warhammer.users.UserService;
import com.P2.warhammer.views.CharacterView;
import com.P2.warhammer.views.DashBoard;
import com.vaadin.browserless.SpringBrowserlessTest;
import com.vaadin.browserless.TreeOnFailureExtension;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
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
public class CharacterTest extends SpringBrowserlessTest {

    @Container
    public static MongoDBContainer mongoDBContainer =
            new MongoDBContainer("mongo:7.0.0");

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired CharacterRepository characterRepository;

    @Autowired
    SkillRepository skillRepository;

    // TODO: ingen testcontainers her lav en ny instant du har fuld kontrol over.
    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.mongodb.database", () -> "testdb");
    }

    @BeforeEach
    @Override
    protected void initVaadinEnvironment(){
        userRepository.deleteAll();
        User dennis = new User("Dennis", "dennis@gmail.com", "12345678");
        userRepository.save(dennis);
        User gamemaster = new User("GameMaster", "gamemaster@gmail.com", "12345678");
        userRepository.save(gamemaster);

        characterRepository.deleteAll();
        List<Characteristic> characteristics = new ArrayList<Characteristic>();
        characterRepository.save(new Character("Mukibuki", dennis, gamemaster, 12, 12,  characteristics, 1));

        skillRepository.deleteAll();

        super.initVaadinEnvironment();
    }

    @Test
    @WithMockUser(username = "dennis@gmail.com")
    public void testCharacterRouteFromDashboard(){

        // Tjekker vi er på dashboard
        DashBoard dashBoard = navigate(DashBoard.class);
        assertFalse($(CharacterView.class).exists());

        //Trykker på knap og tjekker om vi er i character view og om vi er på character view for mukibuki
        test($(Button.class).withText("View").single()).click();
        assertTrue($(CharacterView.class).exists());
        assertTrue($(Span.class).withTextContaining("Name:Mukibuki").exists());
    }

    @Test
    @WithMockUser(username = "dennis@gmail.com")
    public void testCharacterCreation(){
        this.addSkillsToDb();
        this.addRacesEntriesToDb();
    }

    private void addRacesEntriesToDb() {
    }

    private void addSkillsToDb(){
        skillRepository.save(new Skill("Animal Care", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Animal Training (Any)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Animal Training (Demigryph)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Animal Training (Dog)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Animal Training (Horse)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Animal Training (Pegasus)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Animal Training (Pigeon)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Art (Any)", "False", "Dexterity", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Art (Calligrahpy)", "False", "Dexterity", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Art (Cartography)", "False", "Dexterity", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Art (Engraving)", "False", "Dexterity", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Art (Mosaics)", "False", "Dexterity", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Art (Painting)", "False", "Dexterity", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Art (Sculpture)", "False", "Dexterity", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Art (Tattoo)", "False", "Dexterity", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Art (Weaving)", "False", "Dexterity", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Art (Writing)", "False", "Dexterity", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Athletics", "False", "Agility", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Bribery", "False", "Fellowship", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Channelling (Any)", "True", "Willpower", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Channelling (Aqshy)", "True", "Willpower", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Channelling (Azyr)", "True", "Willpower", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Channelling (Chamon)", "True", "Willpower", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Channelling (Dhar)", "True", "Willpower", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Channelling (Ghur)", "True", "Willpower", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Channelling (Ghyran)", "True", "Willpower", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Channelling (Hysh)", "True", "Willpower", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Channelling (Shyish)", "True", "Willpower", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Channelling (Ulgu)", "True", "Willpower", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Charm", "False", "Fellowship", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Charm Animal", "False", "Willpower", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Climb", "True", "Strength", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Command", "False", "Fellowship", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Cool", "False", "Willpower", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Deceive", "False", "Fellowship", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Disguise", "False", "Fellowship", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Dodge", "False", "Agility", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Drive", "False", "Agility", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Evaluate", "False", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Gamble", "False", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Gossip", "False", "Fellowship", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Haggle", "False", "Fellowship", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Heal", "False", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Intimidate", "False", "Strength", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Intuition", "False", "Initiative", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Language (Any)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Language (Aqshy)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Language (Battle Tongue)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Language (Beast-tongue)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Language (Bretonnian)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Language (Chamon)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Language (Dark Tongue)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Language (Daemonic)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Language (Dwarfen)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Language (Eltharin)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Language (Ghyran)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Language (Goblin)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Language (Grapple)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Language (Hysh)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Language (Khazalid)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Language (Kislev)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Language (Magick)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Language (Mootish)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Language (Reikspiel)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Language (Shyish)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Language (Skaven)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Language (Theile)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Language (Thieves' Tongue)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Language (Tilean)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Language (Ulgu)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Leadership", "False", "Fellowship", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Lore (Any)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Lore (Aqshy)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Lore (Art)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Lore (Beasts)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Lore (Bretonnia)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Lore (Chamon)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Lore (Daemons)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Lore (Dwarfs)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Lore (Elves)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Lore (Engineering)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Lore (Ghyran)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Lore (Geography)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Lore (Hysh)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Lore (Kislev)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Lore (Local)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Lore (Magic)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Lore (Medicine)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Lore (Merchant)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Lore (Military)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Lore (Necromancy)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Lore (Navigation)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Lore (Numbers)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Lore (Plants)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Lore (Politics)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Lore (Shyish)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Lore (Sigmar)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Lore (Skaven)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Lore (The Empire)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Lore (Ulgu)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Lore (Undead)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Lore (Underworld)", "True", "Intelligence", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Melee (Any)", "True", "Strength", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Melee (Axe)", "True", "Strength", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Melee (Brawling)", "True", "Strength", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Melee (Cavalry)", "True", "Strength", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Melee (Chain)", "True", "Strength", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Melee (Fencing)", "True", "Strength", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Melee (Flail)", "True", "Strength", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Melee (Glaive)", "True", "Strength", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Melee (Great Weapon)", "True", "Strength", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Melee (Halberd)", "True", "Strength", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Melee (Hammer)", "True", "Strength", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Melee (Lance)", "True", "Strength", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Melee (Polearm)", "True", "Strength", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Melee (Sword)", "True", "Strength", "", 0, 0, 0, false));
        skillRepository.save
                (new Skill("Melee (Two-handed)", "True", "Strength", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Navigation", "False", "Initiative", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Perception", "False", "Initiative", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Persuasion", "False", "Fellowship", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Pick Lock", "False", "Dexterity", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Pray", "False", "Willpower", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Ranged (Any)", "True", "Agility", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Ranged (Blowgun)", "True", "Agility", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Ranged (Bow)", "True", "Agility", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Ranged (Crossbow)", "True", "Agility", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Ranged (Engineering)", "True", "Agility", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Ranged (Entangle)", "True", "Agility", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Ranged (Gunpowder)", "True", "Agility", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Ranged (Sling)", "True", "Agility", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Ranged (Throwing)", "True", "Agility", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Ride", "False", "Agility", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Row", "False", "Strength", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Sail", "False", "Agility", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Seduce", "False", "Fellowship", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Sense Danger", "False", "Initiative", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Sing", "False", "Fellowship", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Sleight of Hand", "False", "Dexterity", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Stealth (Any)", "False", "Agility", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Stealth (Rural)", "False", "Agility", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Stealth (Underground)", "False", "Agility", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Stealth (Urban)", "False", "Agility", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Swim", "True", "Strength", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Track", "True", "Initiative", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Trade (Any)", "True", "Dexterity", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Trade (Apothecary)", "True", "Dexterity", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Trade (Barber)", "True", "Dexterity", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Trade (Boatbuilding)", "True", "Dexterity", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Trade (Brewer)", "True", "Dexterity", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Trade (Calligrapher)", "True", "Dexterity", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Trade (Carpenter)", "True", "Dexterity", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Trade (Chandler)", "True", "Dexterity", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Trade (Cook)", "True", "Dexterity", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Trade (Embalmer)", "True", "Dexterity", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Trade (Engineer)", "True", "Dexterity", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Trade (Herbalist)", "True", "Dexterity", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Trade (Printing)", "True", "Dexterity", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Trade (Smith)", "True", "Dexterity", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Trade (Tanner)", "True", "Dexterity", "", 0, 0, 0, false));
        skillRepository.save(new Skill("Trade (Vintner)", "True", "Dexterity", "", 0, 0, 0, false));
    }

}

