package com.P2.warhammer.views;

import com.P2.warhammer.characters.CharacterService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.PermitAll;

@PermitAll
@Route("character")
public class CharacterView extends Div implements HasUrlParameter<String> {
    private final CharacterService characterService;
    CharacterView(CharacterService  characterService){
        this.characterService = characterService;


    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String characterId) {
        QueryParameters parameters = beforeEvent.getLocation().getQueryParameters();
        parameters.getParameters().forEach((k,v)->{
            add(new Span(k+": "+v));

        });
        add(new Span(characterService.getCharacterFromId(parameters.getParameters().get("Character").getFirst()).toString()));

    }
}
