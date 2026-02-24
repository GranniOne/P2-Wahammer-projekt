package com.P2.warhammer.campaigns;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "campaigns")
public class Campaign {

    @Id
    String name;
    int age;

    Campaign(String name, int age){
        this.name = name;
        this.age = age;
    }
}
