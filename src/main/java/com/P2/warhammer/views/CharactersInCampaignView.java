package com.P2.warhammer.views;


import com.P2.warhammer.characters.CharacterService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.PermitAll;

@PermitAll
@Route("campaign/character")
public class CharactersInCampaignView extends Div implements HasUrlParameter<String> {
    private final CharacterService characterService;

    CharactersInCampaignView(CharacterService  characterService) {
        this.characterService = characterService;

    }

    @Override
    public void setParameter(BeforeEvent beforeEvent,@OptionalParameter String s) {
        QueryParameters parameters = beforeEvent.getLocation().getQueryParameters();
        parameters.getParameters().forEach((k,v)->{
            add(new Span(k+": "+v));

        });

        add(new Span(characterService.getCharacterFromId(parameters.getParameters().get("Character").getFirst()).toString()));
    }
}
