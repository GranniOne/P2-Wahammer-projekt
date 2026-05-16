package com.P2.warhammer.views;


import com.P2.warhammer.campaigns.Campaign;
import com.P2.warhammer.campaigns.CampaignService;
import com.P2.warhammer.characters.Character;
import com.P2.warhammer.characters.CharacterService;
import com.P2.warhammer.users.User;
import com.P2.warhammer.utilities.Utilities;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.PermitAll;

import java.util.*;
import java.util.stream.Collectors;


@PermitAll
@PageTitle("Campaign view Page")
@Route("campaign")
@StyleSheet("css/CampaignStyle.css")
public class CampaignView extends Div implements HasUrlParameter<String> {
    QueryParameters queryParameters;
    Div Content = new Div();
    Div layout = new Div();
    Div header = new Div();

    private final CampaignService campaignService;
    private final CharacterService characterService;

    public CampaignView(CampaignService campaignService,CharacterService characterService){
        this.campaignService = campaignService;
        this.characterService = characterService;

        setClassName("div-page-compaign");
        Content.setClassName("campaign-content");
        layout.setClassName("campaign-view");
        header.setClassName("header");
        add(Content);






    }



    @Override
    public void setParameter(BeforeEvent beforeEvent,@OptionalParameter String campaignId) {
        String parameters = beforeEvent.getLocation().getQueryParameters().getSingleParameter("Campaign").orElse("");
        queryParameters = beforeEvent.getLocation().getQueryParameters();


        Campaign currentCampaign = campaignService.getCampaignById(parameters);
        User user = Utilities.getUserFromAuthentication();


        layout.removeAll();
        header.removeAll();


        if(currentCampaign.getGameMaster().getId().equals(user.getId())){
            List<Character> characters = currentCampaign.getCharacters();
            makeCharactersCards(characters,currentCampaign);

            ComboBox<Character> comboBox = new ComboBox<>("All characters in campaign");
            comboBox.setItems(characters);
            comboBox.setItemLabelGenerator(Character::getName);
            header.add(comboBox);

            Content.add(layout);
        }
        else{

            List<Character> userOwnedCharacters;
            userOwnedCharacters = characterService.getCharactersByUser(user);


            //bruges til at lave comobobox
            List<String> existingIds = currentCampaign
                    .getCharacters()
                    .stream()
                    .map(Character::getId)
                    .toList();

            List<Character> availableCharacters = userOwnedCharacters
                    .stream()
                    .filter(character -> !existingIds.contains(character.getId()))
                    .toList();



            //bruges til at lave character cards
            Set<String> campaignCharacterIds = currentCampaign.getCharacters().stream()
                    .map(Character::getId)
                    .collect(Collectors.toSet());

            List<Character> addedCharacters = userOwnedCharacters.stream()
                    .filter(character -> campaignCharacterIds.contains(character.getId()))
                    .toList();


            ComboBox<Character> comboBox = new ComboBox<>("Select Character to add to campaign");
            comboBox.setItems(availableCharacters);
            comboBox.setItemLabelGenerator(Character::getName);
            header.add(comboBox);
            makeCharactersCards(addedCharacters,currentCampaign);
            Button addcharacter = new Button("Add character", event -> {
                currentCampaign.getCharacters().add(comboBox.getValue());
                campaignService.saveCampaign(currentCampaign);
                UI.getCurrent().navigate(CampaignView.class,queryParameters);
            });
            header.add(addcharacter);
            addcharacter.setClassName("add-character");
            Content.add(header);
            Content.add(layout);
        }
    }
    private void makeCharactersCards(List<Character> characters, Campaign currentCampaign){

        characters.forEach(character -> {
            Div buttonToCard =  new Div();
            Span characterName = new  Span(character.getName());
            Card card = new Card();
            card.add(characterName);
            card.getStyle().set("display","flex").setFlexDirection(Style.FlexDirection.ROW);
            buttonToCard.getStyle().setGap("200px");
            buttonToCard.add(new Button("View", event -> {
                UI.getCurrent().navigate(CharacterView.class,QueryParameters.of("Character",character.getId()));

            }));

            buttonToCard.add(new Button("Edit", event -> {

            }));

            buttonToCard.add(new Button("Delete", event -> {
                if(currentCampaign.getCharacters().removeIf(character2 -> character2.getId().equals(character.getId()))){
                    campaignService.saveCampaign(currentCampaign);
                    UI.getCurrent().navigate(CampaignView.class,queryParameters);
                }
            }));
            card.add(buttonToCard);
            layout.add(card);
        });
    }
}
