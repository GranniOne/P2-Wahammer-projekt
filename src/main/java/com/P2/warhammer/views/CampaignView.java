package com.P2.warhammer.views;


import com.P2.warhammer.campaigns.Campaign;
import com.P2.warhammer.campaigns.CampaignRepository;
import com.P2.warhammer.characters.Character;
import com.P2.warhammer.characters.CharacterRepository;
import com.P2.warhammer.users.User;
import com.P2.warhammer.utilities.Utilities;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.PermitAll;
import org.bson.types.ObjectId;

import java.util.*;
import java.util.stream.Collectors;


@PermitAll
@PageTitle("Campaign view Page")
@Route("campaign")
@StyleSheet("css/characterStyle.css")
public class CampaignView extends Div implements HasUrlParameter<String> {

    Map<String,List<String>> parameters;

    private final CampaignRepository campaignRepository;
    private final CharacterRepository characterRepository;


    public CampaignView(CampaignRepository campaignRepository, CharacterRepository characterRepository){
        this.campaignRepository = campaignRepository;
        this.characterRepository = characterRepository;

        setClassName("div-page");
        getStyle().set("position", "relative");





    }



    @Override
    public void setParameter(BeforeEvent beforeEvent,@OptionalParameter String campaignId) {
        parameters = beforeEvent.getLocation().getQueryParameters().getParameters();
        Campaign currentCampaign = campaignRepository.findCampaignById(parameters.get("Campaign").getFirst());
        User user = (User)VaadinSession.getCurrent().getAttribute("user");

        if(currentCampaign.getGameMaster().getId().equals(user.getId())){
            List<Character> characters = characterRepository.findByCampaign(campaignRepository.findCampaignById(parameters.get("Campaign").getFirst()));

            ComboBox<Character> comboBox = new ComboBox<>("all characters in campaign");
            comboBox.setItems(characters);
            comboBox.setItemLabelGenerator(Character::getName);
            add(comboBox);


        }else{
            List<Character> userOwnedCharacters;
            userOwnedCharacters = characterRepository.getCharactersByUser(user);

            List<String> existingIds = currentCampaign.getCharacters().stream().map(Character::getId).toList();

            List<Character> availableCharacters = userOwnedCharacters.stream()
                    .filter(character -> !existingIds.contains(character.getId()))
                    .toList();


            List<Character> addedCharacters = userOwnedCharacters.stream()
                    .filter(character -> currentCampaign.getCharacters().stream()
                            .anyMatch(c -> Objects.equals(c.getId(), character.getId())))
                    .toList();

            ComboBox<Character> comboBox = new ComboBox<>("Select Character to add to campaign");
            comboBox.setItems(availableCharacters);
            comboBox.setItemLabelGenerator(Character::getName);
            add(comboBox);



            add(new Button("Add Character", event -> {
                currentCampaign.getCharacters().add(comboBox.getValue());
                campaignRepository.save(currentCampaign);
                UI.getCurrent().getPage().reload();

            }));
            addedCharacters.forEach(character -> {
                add(new Button(character.getName()));

            });
        }














    }
}
