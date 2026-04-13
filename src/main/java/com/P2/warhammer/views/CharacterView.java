package com.P2.warhammer.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.*;

@Route("/:CharacterId")
public class CharacterView extends Div implements HasUrlParameter<String> {

    CharacterView(){



    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String characterId) {


    }
}
