package com.P2.warhammer.views;


import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;

import static com.vaadin.copilot.shaded.helger.base.mock.CommonsAssert.assertEquals;


@PermitAll
@PageTitle("Character Creator Page")
@Route("characterCreator")
@StyleSheet("css/characterStyle.css")
public class CharacterCreatorView extends Div {

    int InfoboxSide = 100; // should be 0 for left side or 100 for right side
    int InfoboxWidth = 70; // should be 50 for midtscreen and 100 for none
    public CharacterCreatorView() {
        SplitLayout splitLayout = new SplitLayout();

        setClassName("div-page");

        Div statBox = new Div(new Text("Left"));
        /* TODO : Add content to infobox
        Todo : skills
        Todo : talents
        Todo : stats
        Todo : advanved / basic (category)
        Todo : manual / rolled
        */

        Div infoBox = new Div(new Text("Right"));
        /* TODO : Add content to infobox
        Todo : characteristc
        Todo : name
        Todo : stats
        Todo : advanved / basic (category)
        Todo : description
        */


        boolean isAdvanced = true; // TODO get boolean from database

        String skillOrTalent = (isAdvanced ? "Advanced skill" : "Basic skill"); //TODO: make this a text toggleable using boolean from database

        String objectName = "string from database";
        String objectCharacteristic = "int from database".toString();
        String skillMax = "int from database".toString(); //TODO should always be at least 1. (you can only take something once at least)
        String description = "string from database";




        /* TODO : det her er bare for en sikkerheds skyld, gør ikke noget. gider ikke slette
        new Text(skillOrTalent); // is data skill or talent
        new Text(objectName); //name
        new Text(objectCharacteristic); //characteristic
        new Text(skillMax);
        new Text(description);
        */

        infoBox.add(new Text(skillOrTalent), new Text(objectName), new Text(objectCharacteristic), new Text(skillMax), new Text(description));



        infoBox.getStyle().set("background-color", "red");

        statBox.getStyle().setHeight("100vh");
        infoBox.getStyle().setHeight("100vh");

        splitLayout.addToPrimary(statBox);
        splitLayout.addToSecondary(infoBox);

        splitLayout.setSplitterPosition(InfoboxSide);

        splitLayout.addThemeVariants(SplitLayoutVariant.LUMO_MINIMAL);


        Button toggleButton = new Button("Toggle", e -> {
            double current = splitLayout.getSplitterPosition();
            splitLayout.setSplitterPosition(current == InfoboxSide ? InfoboxWidth : InfoboxSide);
        });

        add(toggleButton, splitLayout);
    }

}
