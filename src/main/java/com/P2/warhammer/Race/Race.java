package com.P2.warhammer.Race;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "race")

public class Race {
    @Id
    private String id;
    String race;
    String dice;
    String career;
    String species;

    public Race() {}

    public Race(String species, String  race, String dice, String career){
        this.race = race;
        this.dice = dice;
        this.career = career;
        this.species = species;
    }

    public String getId(){return id;}
    public String getRace() {return race;}
    public String getCareer() {return career;}
    public String getDice() {return dice;}
    public String getSpecies() {return species;}

    public void setId(String id) {this.id = id;}
    public void setRace(String race){}
    public void setCareer(String career) {}
    public void setDice(String dice) {}
    public void setSpecies(String species) {}
}
