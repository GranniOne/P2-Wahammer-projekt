package com.P2.warhammer.characteristics;

import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

public class Characteristic {

    Integer base;
    Integer modifier;
    Integer penalty;
    Integer racemod;
    String name;

    public Characteristic(String name, Integer base, Integer modifier, Integer penalty, Integer racemod){
        this.name = name;
        this.base = base;
        this.modifier = modifier;
        this.penalty = penalty;
        this.racemod = racemod;
    }
    public Characteristic(){

    }

    public String getCategory() {return "Characteristic";}

    public Integer getBase() {
        return base;
    }

    public Integer getModifier() {
        return modifier;
    }

    public Integer getPenalty() {
        return penalty;
    }

    public Integer getRacemod(){return racemod;}

    public void setBase(Integer base) {
        this.base = base;
    }

    public void setModifier(Integer modifier) {
        this.modifier = modifier;
    }

    public void setPenalty(Integer penalty) {
        this.penalty = penalty;
    }

    public void setRacemod(Integer modifier) {
        this.racemod = racemod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
