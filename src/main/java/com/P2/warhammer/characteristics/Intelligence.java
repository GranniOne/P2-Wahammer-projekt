package com.P2.warhammer.characteristics;

public class Intelligence extends Characteristic {
    int base;
    int modifier;
    int penalty;
    public Intelligence(int base, int modifier, int penalty) {
        super(base, modifier, penalty);
        this.base = base;
        this.modifier = modifier;
        this.penalty = penalty;
    }
}
