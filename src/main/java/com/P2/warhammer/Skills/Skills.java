package com.P2.warhammer.Skills;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "skills")
public class Skills {

    @Id
    private String id;
    String Name;
    String Characteristic;
    String Category;
    String Description;

    public Skills() {}

    public Skills(String Name, String Category, String Characteristic, String Description){
        this.Name = Name;
        this.Category = Category;
        this.Characteristic = Characteristic;
        this.Description = Description;
    }

    @Override
    public String toString(){
        return "Skill{Name = '" + Name + "', Category = '" + Category + "', Characteristic = '" + Characteristic + "', Description = '" + Description + "'}";
    }
}
