package com.P2.warhammer.characters;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
