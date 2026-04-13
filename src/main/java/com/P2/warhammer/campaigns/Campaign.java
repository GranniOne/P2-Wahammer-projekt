package com.P2.warhammer.campaigns;

import com.P2.warhammer.users.UserRepository;
import  com.P2.warhammer.users.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a campaign in the system, stored in the "campaigns" collection in MongoDB.
 * A campaign contains Fields yet to be determined
 *
 * <p>Provides constructors, getters, and setters for managing campaign data.</p>
 */
@Document(collection = "campaigns")
public class Campaign {

    @Id
    private String Id;
    String name;
    String gameMaster;

    UserRepository userRepository;
    List<User> players;

    Campaign(String name, String gameMaster){
        this.name = name;
        this.gameMaster = gameMaster;
        this.players = new ArrayList<>();

    }

    public String getName() {
        return name;
    }
}
