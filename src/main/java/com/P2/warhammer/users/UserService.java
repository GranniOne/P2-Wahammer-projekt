package com.P2.warhammer.users;

import com.vaadin.flow.component.notification.Notification;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Service for managing {@link User} entities in the MongoDB database.
 * Provides methods to:
 * <ul>
 *     <li>Add a new user</li>
 *     <li>Find users by email</li>
 *     <li>Authenticate users with basic validation</li>
 *     <li>Retrieve all users</li>
 * </ul>
 *
 * <p>This class is managed as a Spring {@link Service} and should not be manually instantiated.</p>
 *
 * @see User
 * @see UserRepository
 */
@Service
public class UserService  {
    private final UserRepository repository;


    /**
     * Constructs a new {@link UserService} with the provided {@link UserRepository}.
     * @param repository the repository used to access user data in MongoDB
     */
    public UserService(UserRepository repository) {this.repository = repository;}


    /**
     * Adds a new user to the database.
     *
     * @param username the public username displayed on the profile
     * @param email the email used for authentication
     * @param password the password for authentication
     * @return the newly created {@link User} entity
     */
    public User addUser(String username, String email, String password) {return repository.save(new User(username, email, password));}

    /**
     * Retrieves a user by their email address from the database.
     *
     * <p>This method queries the database for a user with the given email. used by external classes to access
     * the email field of a document in the users collection</p>
     *
     * @param email the email of the user to search for
     * @return the {@link User} with the given email, or {@code null} if no user exists with that email
     */
    public User findFromEmail(String email) {
        return repository.findUserByEmail(email);
    }

    public User findFromUsername(String username) {
        return repository.findUserByUsername(username);
    }

    public void saveUser(User user){
        this.repository.save(user);
    }

    public void deleteUser(User user){
        this.repository.delete(user);
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User  findUserById(String id) {
        return this.repository.findDistinctById(id);
    }

}
