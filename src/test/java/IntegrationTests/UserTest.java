package IntegrationTests;

import com.P2.warhammer.Application;
import com.P2.warhammer.characters.CharacterRepository;
import com.P2.warhammer.users.User;
import com.P2.warhammer.users.UserRepository;
import com.P2.warhammer.users.UserService;
import com.P2.warhammer.views.LoginView;
import com.P2.warhammer.views.SignupView;
import com.vaadin.browserless.SpringBrowserlessTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    @BeforeEach
    public void setUp(){
    }

    @Test
    @WithAnonymousUser
    public void testUserSignup(){
        final SignupView signupView = navigate(SignupView.class);
        test(signupView.firstName).setValue("BiggieBob");
        test(signupView.email).setValue("coolaid@gmail.com");
        test(signupView.password).setValue("12345678");
        test(signupView.confirmPassword).setValue("12345678");
        test(signupView.loginButton).click();
        User query_user = userRepository.findUserByEmail("coolaid@gmail.com");
        System.out.println(query_user);
    }

}