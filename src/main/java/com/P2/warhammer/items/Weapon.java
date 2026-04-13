package com.P2.warhammer.items;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Weapon extends WarhammerItem {
    Weapon(String name, int amount, int weight) {
        super(name, amount, weight);
    }
}
