package com.P2.warhammer.characteristics;

public class Characteristic {

    int base;
    int modifier;
    int penalty;
    String name;

    public Characteristic(int base, int modifier, int penalty, String name){
        this.base = base;
        this.modifier = modifier;
        this.penalty = penalty;
    }
    public String getCategory() {return "Characteristic";}

    public int getBase() {
        return base;
    }

    public int getModifier() {
        return modifier;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public void setModifier(int modifier) {
        this.modifier = modifier;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
