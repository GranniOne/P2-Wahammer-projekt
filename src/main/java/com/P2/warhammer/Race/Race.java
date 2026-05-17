package com.P2.warhammer.Race;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Document(collection = "race")

public class Race {
    @Id
    private String id;
    boolean value;
    String race;
    String dice;
    String career;
    String species;
    String Name;
    String source;
    List<String> skills;
    List<Map<String, Object>> talents;

    @Field("dicerolls")
    Map<String, ArrayList<Integer>> basecharacteristicMap;

    public Race() {}

    public Race(String Name, boolean value, String source, List<String> skills, List<Map<String, Object>> talents ,String species, String  race, String dice, String career, Map<String, ArrayList<Integer>> basecharacteristicMap){
        this.Name = Name;
        this.skills = skills;
        this.talents = talents;
        this.value = value;
        this.source = source;
        this.race = race;
        this.dice = dice;
        this.career = career;
        this.species = species;
        this.basecharacteristicMap = basecharacteristicMap;
    }

    public Map<String, ArrayList<Integer>> getBasecharacteristicMap() {
        return basecharacteristicMap;
    }

    public void setBasecharacteristicMap(Map<String, ArrayList<Integer>> basecharacteristicMap) {
        this.basecharacteristicMap = basecharacteristicMap;
    }

    public String getId(){return id;}
    public String getRace() {return race;}
    public String getCareer() {return career;}
    public String getDice() {return dice;}
    public String getSpecies() {return species;}
    public List<String> getSkills() {return skills;}

    public List<Map<String, Object>> getTalents() {return talents;}
    public void setTalents(List<Map<String, Object>> talents) {this.talents = talents;}

    public void setRace(String race) {this.race = race;}
    public void setCareer(String career) {this.career = career;}
    public void setDice(String dice) {this.dice = dice;}
    public void setSpecies(String species) {this.species = species;}

    private List<RaceEntry> entries;

    public List<RaceEntry> getEntries() { return entries; }
    public void setEntries(List<RaceEntry> entries) { this.entries = entries; }

    public String getName() {return Name;}
    public void setName(String Name) {this.Name = Name;}

    public boolean isValue() {return value;}
    public void setValue(boolean value) {this.value = value;}

    public String getSource() {return source;}
    public void setSource(String source) {this.source = source;}
}
