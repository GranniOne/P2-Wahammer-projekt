package com.P2.warhammer.items;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarhammerItem {


    String name;
    int amount;
    int weight;

    public WarhammerItem(String name, int amount, int weight){
        this.name = name;
        this.amount = amount;
        this.weight = weight;
    }

    public WarhammerItem(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }

    public WarhammerItem() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
