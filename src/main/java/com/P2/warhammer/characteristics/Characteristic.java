package com.P2.warhammer.characteristics;

public class Characteristic {

    Integer base;
    Integer modifier;
    Integer penalty;
    String name;

    public Characteristic(String name, Integer base, Integer modifier, Integer penalty){
        this.name = name;
        this.base = base;
        this.modifier = modifier;
        this.penalty = penalty;
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

    public void setBase(Integer base) {
        this.base = base;
    }

    public void setModifier(Integer modifier) {
        this.modifier = modifier;
    }

    public void setPenalty(Integer penalty) {
        this.penalty = penalty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
