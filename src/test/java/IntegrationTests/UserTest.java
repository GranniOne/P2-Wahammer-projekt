package IntegrationTests;

import com.P2.warhammer.Application;
import com.P2.warhammer.characters.CharacterRepository;
import com.P2.warhammer.layout.Navigation;
import com.P2.warhammer.users.User;
import com.P2.warhammer.users.UserRepository;
import com.P2.warhammer.users.UserService;
import com.P2.warhammer.views.DashBoard;
import com.P2.warhammer.views.LoginView;
import com.P2.warhammer.views.SignupView;
import com.vaadin.browserless.SpringBrowserlessTest;
import com.vaadin.browserless.TreeOnFailureExtension;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginFormTester;
import lombok.With;
import org.apache.catalina.Role;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@ExtendWith(TreeOnFailureExtension.class)
@SpringBootTest(classes = Application.class)
public class UserTest extends SpringBrowserlessTest{

    @Container
    public static MongoDBContainer mongoDBContainer =
            new MongoDBContainer("mongo:7.0.0");

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.mongodb.database", () -> "testdb");
    }

    // der bliver overridet fordi en user skal gemmes i repoet inden en mock vaadin session startes
    // dette er fordi mock vaadin session router til dashboard hvis man er authenticated, og dette giver en mongodb fejl
    // hvis ens authentication ikke matcher en eksistrende bruger  i databasen, der kan hentes karakterer / kampagner fra
    @BeforeEach
    @Override
    protected void initVaadinEnvironment() {
        userRepository.deleteAll();
        // normal user
        userRepository.save(new User("Dennis", "dennis@gmail.com", "12345678"));
        // admin user (hardcoded i system til at give bob@gmail.com admin role)
        userRepository.save(new User("Dennis", "bob@gmail.com", "12345678"));
        super.initVaadinEnvironment();
    }

    @Test
    public void testUserSignup(){
        final SignupView signupView = navigate(SignupView.class);
        test(signupView.firstName).setValue("BiggieBob");
        test(signupView.email).setValue("coolaid@gmail.com");
        test(signupView.password).setValue("12345678");
        test(signupView.confirmPassword).setValue("12345678");
        test(signupView.loginButton).click();
        User query_user = userRepository.findUserByEmail("coolaid@gmail.com");
        System.out.println(query_user);
        // TODO: assert user findes
    }

    // tjekker at logud knappen gør at mock vaadin sessionen smider en session invalidering af at blive logget ud
    // bliver nød til at tjekke efter fejl something html lag mangler
    @Test
    @WithMockUser(username = "dennis@gmail.com")
    public void testUserLogoutThrowsInvalidatedSessionError(){
        DashBoard dashBoard = navigate(DashBoard.class);
        Navigation navigation = $(Navigation.class).single();


        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> test(navigation.logoutButton).click()
        );

        assertTrue(exception.getMessage().contains("invalidated"));
    }

    // tjekker om bruger med email har en admin role og ikke user role
    @Test
    @WithMockUser(username = "bob@gmail.com", roles = "ADMIN")
    public void testConfirmAdminRole(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertTrue(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));

        assertFalse(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    @WithMockUser(username = "bob@gmail.com", roles = "USER")
    public void testConfirmUserRole(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertFalse(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));

        assertTrue(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }
}