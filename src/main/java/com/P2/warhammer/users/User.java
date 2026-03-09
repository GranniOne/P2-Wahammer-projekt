package com.P2.warhammer.users;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * Repository interface for managing {@link User} entities in MongoDB.
 * Extends {@link MongoRepository} to provide standard CRUD operations.
 * Includes custom query methods to find users by email or username.
 *
 * @see User
 * @see org.springframework.data.mongodb.repository.MongoRepository
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
}
