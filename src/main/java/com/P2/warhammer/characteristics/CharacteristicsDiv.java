package com.P2.warhammer.characteristics;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;

public class CharacteristicsDiv extends Div {

    private String name;

    public CharacteristicsDiv(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}