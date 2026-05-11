package IntegrationTests;


import com.P2.warhammer.Application;
import com.P2.warhammer.characteristics.Characteristic;
import com.P2.warhammer.characters.Character;
import com.P2.warhammer.characters.CharacterRepository;
import com.P2.warhammer.users.User;
import com.P2.warhammer.users.UserRepository;
import com.P2.warhammer.users.UserService;
import com.P2.warhammer.views.CharacterCreatorView;
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
        characterRepository.save(new Character("Mukibuki", dennis, gamemaster, 12, 12,  characteristics));
        super.initVaadinEnvironment();
    }

    @Test
    @WithMockUser(username = "dennis@gmail.com")
    public void testCharacterRouteFromDashboard(){

        // Tjekker vi er på dashboard
        DashBoard dashBoard = navigate(DashBoard.class);
        assertFalse($(CharacterView.class).exists());

        //Trykker på knap og tjekker om vi er i character view og om vi er på character view for mukibuki
        $(Button.class).withText("View").single().click();
        assertTrue($(CharacterView.class).exists());
        assertTrue($(Span.class).withTextContaining("Name:Mukibuki").exists());
    }

}

