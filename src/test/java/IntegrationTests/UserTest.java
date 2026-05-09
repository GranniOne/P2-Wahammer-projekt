package IntegrationTests;

import com.P2.warhammer.Application;
import com.P2.warhammer.characters.CharacterRepository;
import com.P2.warhammer.users.User;
import com.P2.warhammer.users.UserRepository;
import com.P2.warhammer.users.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
public class UserTest {
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
    public void testUserSignup(){
        List<User> userList = userRepository.findAll();
        assertTrue(userList.isEmpty());
        userService.addUser("bob","bob","bob");
        userList = userRepository.findAll();
        assertEquals(1, userList.size());
    }

}
