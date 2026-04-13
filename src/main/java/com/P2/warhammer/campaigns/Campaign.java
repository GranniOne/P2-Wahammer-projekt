package com.P2.warhammer.campaigns;

import com.P2.warhammer.users.UserRepository;
import  com.P2.warhammer.users.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

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
    private String id;
    String name;
    @DocumentReference
    User gameMaster;
    @DocumentReference
    List<User> players;

    Campaign(String name, User gameMaster){
        this.name = name;
        this.gameMaster = gameMaster;
        this.players = new ArrayList<>();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getGameMaster() {
        return gameMaster;
    }

    public void setGameMaster(User gameMaster) {
        this.gameMaster = gameMaster;
    }

    public List<User> getPlayers() {
        return players;
    }

    public void setPlayers(List<User> players) {
        this.players = players;
    }
}
