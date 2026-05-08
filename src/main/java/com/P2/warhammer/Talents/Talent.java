package com.P2.warhammer.Talents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "talents")
public class Talent {

    @Id
    private String id;
    String Name;
    String Characteristic;
    String Category;
    String Description;
    int amountTaken;

    public Talent() {}

    public Talent(String Name, String Category, String Characteristic, String Description){
        this.Name = Name;
        this.Category = Category;
        this.Characteristic = Characteristic;
        this.Description = Description;
    }

    public Talent(String Name, String Category, String Characteristic, String Description, int amountTaken){
        this.Name = Name;
        this.Category = Category;
        this.Characteristic = Characteristic;
        this.Description = Description;
        this.amountTaken = amountTaken;
    }

    @Override
    public String toString(){
        return "Talent{Name = '" + Name + "', Category = '" + Category + "', Characteristic = '" + Characteristic + "', Description = '" + Description + "'}";
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return Name;
    }

    public String getCharacteristic() {
        return Characteristic;
    }

    public String getCategory() {
        return Category;
    }

    public String getDescription() {
        return Description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setCharacteristic(String characteristic) {
        Characteristic = characteristic;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getAmountTaken() {
        return amountTaken;
    }

    public void setAmountTaken(int amountTaken) {
        this.amountTaken = amountTaken;
    }
}
