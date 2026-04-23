package com.P2.warhammer.views;

import com.P2.warhammer.characters.Character;
import com.P2.warhammer.characters.CharacterRepository;
import com.P2.warhammer.characters.CharacterService;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.PermitAll;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PermitAll
@Route("character")
@StyleSheet("css/charactersheet.css")
public class CharacterView extends Div implements HasUrlParameter<String> {
    private final CharacterService characterService;
    private final CharacterRepository characterRepository;
    private Character character;


    CharacterView(CharacterService  characterService, CharacterRepository characterRepository){
        this.characterService = characterService;
        this.characterRepository = characterRepository;
        this.setClassName("characterPage");
        // Layout

    }

    @Override
        public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String characterId) {
            this.character = characterService.getCharacterFromId(beforeEvent.getLocation().getQueryParameters().getParameters("Character").getFirst());
            List<Integer> characteristics = character.getCharacteristics();
            String[] characteristicNames = {"Weapon Skill", "Ballistic Skill", "Strength", "Toughness", "Initiative", "Agility", "Dexterity", "Intelligence", "Willpower", "Fellowship"};
            Map<String,Integer> characteristicsMap = new HashMap<>();

            for(int i = 0; i < characteristicNames.length; i++){
                characteristicsMap.put(characteristicNames[i], characteristics.get(i));
            }



            Div body = new Div();
            body.setClassName("character_sheet");
            add(body);

            Div div  = new Div();
            div.getStyle().setWidth("100%").setHeight("100px");


            body.add(div);


            Div characteristics_body = new Div();
            characteristics_body.setClassName("characteristics_body");
            characteristicsMap.forEach((k,v) -> {
                Div characteristic_box_in_body = new Div();
                characteristic_box_in_body.setClassName("characteristics_box_in_body");
                characteristic_box_in_body.add(new Span(k));
                characteristic_box_in_body.add(new Span(v.toString()));;
                characteristics_body.add(characteristic_box_in_body);

            });

            body.add(characteristics_body);
            Div wounds_box_in_body = new Div();
            wounds_box_in_body.setClassName("wounds_box_in_body");
            wounds_box_in_body.add(new Span("Max wounds " + character.getMaxWounds()));

            IntegerField current_wounds = new IntegerField("Wounds");
            Binder<Character> wound_binder = new Binder<>(Character.class);

            wound_binder.forField(current_wounds)
                    .bind(Character::getDamageTaken, Character::setDamageTaken);
            wound_binder.readBean(character);

            current_wounds.setValueChangeMode(ValueChangeMode.LAZY);
            current_wounds.addValueChangeListener(change -> {
                if(change.getValue() == null) {
                    return;
                }
                character.setDamageTaken(change.getValue());
                    characterRepository.save(character);
              //  current_wounds.getB

            });
            wounds_box_in_body.add(current_wounds);
            body.add(wounds_box_in_body);








        }
}
