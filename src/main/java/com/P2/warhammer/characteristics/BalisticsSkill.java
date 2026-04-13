package com.P2.warhammer.characteristics;

public class BalisticsSkill extends Characteristic {
    int base;
    int modifier;
    int penalty;
    public BalisticsSkill(int base, int modifier, int penalty) {
        super(base, modifier, penalty);
        this.base = base;
        this.modifier = modifier;
        this.penalty = penalty;
    }

}
