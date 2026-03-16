package com.P2.warhammer.characters;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Weapon extends  WarhammerItem{
    Weapon(String name, int amount, int weight) {
        super(name, amount, weight);
    }
}
