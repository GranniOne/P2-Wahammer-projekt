package com.P2.warhammer.users;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * Represents a user account in the system.
 * <p>
 * This class is stored in the "users" collection in MongoDB and acts as the
 * {@link Document} for the {@code users} collection on the database.
 * All interactions with user data should go through {@link UserService} to
 * ensure validation, authentication.
 * </p>
 *
 * <p><strong>Guidelines:</strong></p>
 * <ul>
 *     <li>{@code username} – public display name for the user.</li>
 *     <li>{@code email} – used for login.</li>
 *     <li>{@code password} – stored for authentication.</li>
 * </ul>
 *
 * <p>Use {@link UserService} methods to create, authenticate, and manage users in the database.</p>
 */
@Document(collection = "users")
public class User {


    @Id
    private String id;

    String email;
    String password;
    String username;



    public User() {}

    User(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }


    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
