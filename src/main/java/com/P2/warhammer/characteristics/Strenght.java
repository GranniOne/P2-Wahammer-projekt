package com.P2.warhammer.characteristics;

public class Strenght extends Characteristic {
    int base;
    int modifier;
    int penalty;
    public Strenght(int base, int modifier, int penalty) {
        super(base, modifier, penalty);
        this.base = base;
        this.modifier = modifier;
        this.penalty = penalty;
    }


}
