package com.P2.warhammer.Skills;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "skills")
public class Skill {

    @Id
    private String id;
    String Name;
    String Characteristic;
    String Category;
    String Description;
    Integer PenaltyValue;
    Boolean boughtBool;
    Integer BonusValue;
    Integer StartValue;
    Integer speciesStartingBonus;
    Integer careerStartingBonus;

    public Skill() {
        this.PenaltyValue = 0;
        this.BonusValue = 0;
        this.StartValue = 0;
        this.speciesStartingBonus = 0;
        this.careerStartingBonus = 0;
    }

    public Skill(String Name, int speciesStartingBonus, int careerStartingBonus, String Category, String Characteristic, String Description,  int StartValue, int BonusValue,  int PenaltyValue, Boolean boughtBool) {
        this.Name = Name;
        this.Category = Category;
        this.Characteristic = Characteristic;
        this.Description = Description;
        this.StartValue = StartValue;
        this.BonusValue = BonusValue;
        this.PenaltyValue = PenaltyValue;
        this.boughtBool = boughtBool;
        this.speciesStartingBonus = speciesStartingBonus;
        this.careerStartingBonus = careerStartingBonus;
    }

    @Override
    public String toString(){
        return "Skill{Name = '" + Name + "', Category = '" + Category + "', Characteristic = '" + Characteristic + "', Description = '" + Description + "'}";
    }

    public String getId() {
        return id;
    }

    public String getName() {return Name;}

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
        this.Name = name;
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

    public Boolean getBoughtBool() {
        return boughtBool;
    }

    public void setBoughtBool(Boolean boughtBool) {
        this.boughtBool = boughtBool;
    }

    public void setSpeciesStartingBonus(Integer speciesStartingBonus) {this.speciesStartingBonus = speciesStartingBonus;}
    public Integer getSpeciesStartingBonus() {return speciesStartingBonus;}

    public void setCareerStartingBonus(Integer careerStartingBonus) {this.careerStartingBonus = careerStartingBonus;}
    public Integer getCareerStartingBonus() {return careerStartingBonus;}

    public int getModifierTotal() {
        return safe(speciesStartingBonus)
                + safe(careerStartingBonus)
                + safe(BonusValue);
    }

    private int safe(Integer v) {
        return v == null ? 0 : v;
    }

}


