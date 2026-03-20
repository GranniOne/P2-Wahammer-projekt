package com.P2.warhammer.characters;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;


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
    String race;
    int age = 0;

    int experience = 0;

    int advantage = 0;
    int maxWounds = 0;
    int damageTaken = 0;
    List<Integer> armourValues = new ArrayList<>(); // I think we can just say that the first

    int corruptionMax = 0;
    int corruptionTaken = 0;

    int Pfennings = 0;
    int silverShillings = 0;
    int goldCrowns = 0;
    int statusLevel = 0; //your "class gold". see Excel sheet

    List<Object> inventory = new ArrayList<>();
    List<Object> equippedArmour = new ArrayList<>();
    List<Object> equippedWeapons = new ArrayList<>();

    List<Object> talents = new ArrayList<>();
    List<Object> skills = new ArrayList<>();
    List<Object> conditions = new ArrayList<>();
    List<Integer> characteristics = new ArrayList<>(); // will have this order: ws bs strength toughness initiative agility dexterity intelligence willpower fellowship


    Character(String name, String user) {
        this.name = name;
        this.user = user;

        //sets all armour values to zero
        for (int i = 1; i <= 5; i++) {
            armourValues.add(0);
        }
        for (int i = 1; i <= 10; i++) {
            characteristics.add(0);
        }

    }

    public String getName() {
        return name;
    }

    public String getUser() {
        return user;
    }

    public String getRace() {
        return race;
    }

    public int getAge() {
        return age;
    }

    public int getExperience() {
        return experience;
    }

    public int getAdvantage() {
        return advantage;
    }

    public int getMaxWounds() {
        return maxWounds;
    }

    public int getDamageTaken() {
        return damageTaken;
    }

    public List<Integer> getArmourValues() {
        return armourValues;
    }

    public int getCorruptionMax() {
        return corruptionMax;
    }

    public int getCorruptionTaken() {
        return corruptionTaken;
    }

    public int getPfennings() {
        return Pfennings;
    }

    public int getSilverShillings() {
        return silverShillings;
    }

    public int getGoldCrowns() {
        return goldCrowns;
    }

    public int getStatusLevel() {
        return statusLevel;
    }

    public List<Object> getInventory() {
        return inventory;
    }

    public List<Object> getEquippedArmour() {
        return equippedArmour;
    }

    public List<Object> getEquippedWeapons() {
        return equippedWeapons;
    }

    public List<Object> getTalents() {
        return talents;
    }

    public List<Object> getSkills() {
        return skills;
    }

    public List<Object> getConditions() {
        return conditions;
    }

    public List<Integer> getCharacteristics() {
        return characteristics;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setAdvantage(int advantage) {
        this.advantage = advantage;
    }

    public void setMaxWounds(int maxWounds) {
        this.maxWounds = maxWounds;
    }

    public void setDamageTaken(int damageTaken) {
        this.damageTaken = damageTaken;
    }

    public void setArmourValues(List<Integer> armourValues) {
        this.armourValues = armourValues;
    }

    public void setCorruptionMax(int corruptionMax) {
        this.corruptionMax = corruptionMax;
    }

    public void setCorruptionTaken(int corruptionTaken) {
        this.corruptionTaken = corruptionTaken;
    }

    public void setPfennings(int pfennings) {
        Pfennings = pfennings;
    }

    public void setSilverShillings(int silverShillings) {
        this.silverShillings = silverShillings;
    }

    public void setGoldCrowns(int goldCrowns) {
        this.goldCrowns = goldCrowns;
    }

    public void setStatusLevel(int statusLevel) {
        this.statusLevel = statusLevel;
    }

    public void setInventory(List<Object> inventory) {
        this.inventory = inventory;
    }

    public void setEquippedArmour(List<Object> equippedArmour) {
        this.equippedArmour = equippedArmour;
    }

    public void setEquippedWeapons(List<Object> equippedWeapons) {
        this.equippedWeapons = equippedWeapons;
    }

    public void setTalents(List<Object> talents) {
        this.talents = talents;
    }

    public void setSkills(List<Object> skills) {
        this.skills = skills;
    }

    public void setConditions(List<Object> conditions) {
        this.conditions = conditions;
    }

    public void setCharacteristics(List<Integer> characteristics) {
        this.characteristics = characteristics;
    }
}
