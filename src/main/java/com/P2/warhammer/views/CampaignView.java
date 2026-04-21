package com.P2.warhammer.views;


import com.P2.warhammer.campaigns.Campaign;
import com.P2.warhammer.campaigns.CampaignRepository;
import com.P2.warhammer.characters.Character;
import com.P2.warhammer.characters.CharacterRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.PermitAll;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@PermitAll
@PageTitle("Campaign view Page")
@Route("campaign")
@StyleSheet("css/characterStyle.css")
public class CampaignView extends Div implements HasUrlParameter<String> {

    private static final String QUERY_PARAM_CHARACTER = "character";
    private static final String QUERY_PARAM_CAMPAIGN = "campaign";

    Map<String,List<String>> parameters;

    private final List<TextField> fieldArray = new ArrayList<>();
    private final CampaignRepository campaignRepository;
    private final List<Campaign> campaigns;
    private final CharacterRepository characterRepository;
    private final List<Character> characters;

    public CampaignView(CampaignRepository campaignRepository, CharacterRepository characterRepository){

        this.campaignRepository = campaignRepository;
        this.campaigns = campaignRepository.findAll();

        this.characterRepository = characterRepository;
        this.characters = characterRepository.findAll();

        setClassName("div-page");
        getStyle().set("position", "relative");

        Div infoBox = new Div();
        infoBox.getStyle().set("color", "green");

        TextField field = new TextField();

        infoBox.add(field);
        /*TODO Der er errors her, men jeg tror at jeg kan fikse koden snart
        infoDisplayer(infoBox, campaigns);
        infoDisplayer(infoBox, characters);*/
        add(infoBox);



    }

    private void infoDisplayer(Div infoBox, List<Object> list) {
        Div infoGrid = new Div();
        list.forEach(object -> {
            Div infoItem = new Div();
            /* TODO fikse det her lol. jeg er for doven til at fikse det nu men hvis det virker burde man kunne se både characters og campaigns i databasen. alt vi mangler er at den kun displayer de data der indeholder sit eget userId
            TODO problemet ligger i at java ikke vil tillade mig at kalde på getname fra de forskellige klasser;
            Button infoButton = new Button(object.getName(), e -> {
                System.out.println("hello");
            });
            infoItem.add(infoButton);
            infoGrid.add(infoItem);*/
        });
        infoBox.add(infoGrid);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent,@OptionalParameter String campaignId) {
        parameters = beforeEvent.getLocation().getQueryParameters().getParameters();

        characterRepository.findByCampaign(campaignRepository.findCampaignById(parameters.get("Campaign").getFirst())).forEach(character -> {

            add(new Button(character.getName(), buttonClickEvent -> {
                Map<String, List<String>> newParamsMap = new HashMap<>(parameters);

                newParamsMap.put("Character", List.of(character.getId()));

                UI.getCurrent().navigate(CharactersInCampaignView.class,new QueryParameters(newParamsMap));


            }));
        });



    }
}
