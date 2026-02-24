package com.P2.warhammer.users;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;




@Document(collection = "users")
public class User {


    @Id
    private String id;
    String name;
    int age;

    public User() {}

    User(String name, int age){
        this.name = name;
        this.age = age;
    }


}
