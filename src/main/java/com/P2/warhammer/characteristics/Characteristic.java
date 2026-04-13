package com.P2.warhammer.characteristics;

public class Characteristic {

    int base;
    int modifier;
    int penalty;

    public Characteristic(int base, int modifier, int penalty){
        this.base = base;
        this.modifier = modifier;
        this.penalty = penalty;
    }
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
}
