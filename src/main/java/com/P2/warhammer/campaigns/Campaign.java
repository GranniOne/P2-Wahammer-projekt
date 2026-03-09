package com.P2.warhammer.campaigns;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



/**
 * Represents a campaign in the system, stored in the "campaigns" collection in MongoDB.
 * A campaign contains Fields yet to be determined
 *
 * <p>Provides constructors, getters, and setters for managing campaign data.</p>
 */
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
