package com.P2.warhammer.characters;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



/**
 * Represents a game character in the system, stored in the "characters" collection in MongoDB.
 * Contains fields for character yet to be determined.
 *
 * <p>Provides constructors, getters, and setters for accessing and modifying character data.</p>

 */
@Document(collection = "characters")
public class Character {

    @Id
    String name;
    String user;

    Character(String name, String user){
        this.name = name;
        this.user = user;

    }

}
