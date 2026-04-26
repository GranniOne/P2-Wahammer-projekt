package com.P2.warhammer.characteristics;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.IntegerField;

public class CharacteristicsDiv extends Div {

    private String name;
    private IntegerField totalField;

    public CharacteristicsDiv(String name) {
        this.name = name;
    }

    public IntegerField getTotalField() {
        return totalField;
    }

    public void setTotalField(IntegerField totalField) {
        this.totalField = totalField;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}