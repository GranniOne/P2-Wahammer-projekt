package com.P2.warhammer.characters;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "characters")
public class Character {

    @Id
    String name;
    int age;

    Character(String name, int age){
        this.name = name;
        this.age = age;
    }

}
