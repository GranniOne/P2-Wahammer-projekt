package com.P2.warhammer.users;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends MongoRepository<User, String> {

    @Query("{email:'?0'}")
    User findUserByEmail(String email);

    User findUserByUsername(String username);

}
