package com.P2.warhammer.Skills;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "skills")
public class Skill {

    @Id
    private String id;
    String Name;
    String Characteristic;
    String Category;
    String Description;
    Integer StartValue;
    Integer BonusValue;
    Integer PenaltyValue;

    public Skill() {}

    public Skill(String Name, String Category, String Characteristic, String Description,  Integer StartValue, Integer BonusValue,  Integer PenaltyValue) {
        this.Name = Name;
        this.Category = Category;
        this.Characteristic = Characteristic;
        this.Description = Description;
        this.StartValue = StartValue;
        this.BonusValue = BonusValue;
        this.PenaltyValue = PenaltyValue;
    }

    @Override
    public String toString(){
        return "Skill{Name = '" + Name + "', Category = '" + Category + "', Characteristic = '" + Characteristic + "', Description = '" + Description + "'}";
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

    public void setId(String id) {this.id = id;}

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

    public Integer getStartValue() {
        return StartValue;
    }

    public void setStartValue(Integer startValue) {
        StartValue = startValue;
    }

    public Integer getBonusValue() {
        return BonusValue;
    }

    public void setBonusValue(Integer bonusValue) {
        BonusValue = bonusValue;
    }

    public Integer getTotalValue(){
        return StartValue + BonusValue - PenaltyValue;
    }

    public Integer getPenaltyValue() {
        return PenaltyValue;
    }

    public void setPenaltyValue(Integer penaltyValue) {
        PenaltyValue = penaltyValue;
    }
}
