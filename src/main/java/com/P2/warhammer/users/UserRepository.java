package com.P2.warhammer.users;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing {@link User} entities in MongoDB.
 * Extends {@link MongoRepository} to provide standard CRUD operations.
 * Includes custom query methods to find users by email or username.
 *
 * @see User
 * @see MongoRepository
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    /**
     * Finds a user by their email address.
     *
     * @param email the email of the user
     * @return the {@link User} with the specified email, or null if none found
     */
    @Query("{email:'?0'}")
    User findUserByEmail(String email);

    User findUserByUsername(String username);
}
