package com.P2.warhammer.Race;

import java.util.ArrayList;
import java.util.Map;

public class RaceEntry {
    private int min;
    private int max;
    private String species;
    private String career;
    Map<String, ArrayList<Integer>> basecharacteristicMap;

    public RaceEntry() {}

    public int getMin() { return min; }
    public int getMax() { return max; }
    public String getSpecies() { return species; }
    public Map<String, ArrayList<Integer>> getBasecharacteristicMap(){return basecharacteristicMap; };
    public void setBasecharacteristicMap(Map<String, ArrayList<Integer>> basecharacteristicMap) {
        this.basecharacteristicMap = basecharacteristicMap;
    }

    public void setMin(int min) { this.min = min; }
    public void setMax(int max) { this.max = max; }
    public void setSpecies(String species) { this.species = species; }

    public String getCareer() {
        return career;
    }
}