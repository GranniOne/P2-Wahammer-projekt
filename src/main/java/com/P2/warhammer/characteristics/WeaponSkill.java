package com.P2.warhammer.characteristics;

public class WeaponSkill extends Characteristic {
    int base;
    int modifier;
    int penalty;
    String name;
    public WeaponSkill(int base, int modifier, int penalty, String name) {
        super(base, modifier, penalty, name);
        this.base = base;
        this.modifier = modifier;
        this.penalty = penalty;
        this.name = name;
    }


}
