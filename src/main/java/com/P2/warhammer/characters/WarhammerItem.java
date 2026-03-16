package com.P2.warhammer.characters;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarhammerItem {


    String name;
    int amount;
    int weight;

    WarhammerItem(String name, int amount, int weight){
        this.name = name;
        this.amount = amount;
        this.weight = weight;
    }
}
