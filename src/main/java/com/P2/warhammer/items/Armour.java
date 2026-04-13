package com.P2.warhammer.items;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Armour extends WarhammerItem {
    Armour(String name, int amount, int weight) {
        super(name, amount, weight);
    }
}
