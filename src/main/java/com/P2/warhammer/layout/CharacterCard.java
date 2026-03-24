package com.P2.warhammer.layout;

import com.P2.warhammer.characters.Character;
import com.P2.warhammer.characters.CharacterService;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.card.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public class CharacterCard extends Card {
    public CharacterCard(Character character) {

        Avatar avatar = new Avatar(character.getName());
        this.setMedia(avatar);
        this.add(character.toString());



    }

}
