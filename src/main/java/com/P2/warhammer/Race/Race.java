package com.P2.warhammer.Race;

import com.P2.warhammer.characteristics.Characteristic;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Document(collection = "race")

public class Race {
    @Id
    private String id;
    String race;
    String dice;
    String career;
    String species;
    Map<String, ArrayList<Integer>> basecharacteristicMap;

    public Race() {}

    public Race(String species, String  race, String dice, String career, Map<String, ArrayList<Integer>> basecharacteristicMap){
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

    public void setRace(String race) {this.race = race;}
    public void setCareer(String career) {this.career = career;}
    public void setDice(String dice) {this.dice = dice;}
    public void setSpecies(String species) {this.species = species;}

    private List<RaceEntry> entries;

    public List<RaceEntry> getEntries() { return entries; }
    public void setEntries(List<RaceEntry> entries) { this.entries = entries; }
}
