package com.P2.warhammer.views;


import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;


@PermitAll
@PageTitle("Character Creator Page")
@Route("characterCreator")
@StyleSheet("css/characterStyle.css")
public class CharacterCreatorView extends Div {
    private final List<TextField> fieldArray = new ArrayList<>();

    public CharacterCreatorView() {
        setClassName("div-page");
        getStyle().set("position", "relative");

        Div statBox = new Div();
        /* TODO : Add content to infobox
        Todo : skills
        Todo : talents
        Todo : stats
        Todo : advanved / basic (category)
        Todo : manual / rolled
        */

        Div infoBoxParent = new Div();
        /* TODO : Add content to infobox
        Todo : characteristc
        Todo : name
        Todo : stats
        Todo : advanved / basic (category)
        Todo : description
        */
        CharacterSkillsGeneration(statBox,infoBoxParent);
        InfoCreator(infoBoxParent);



        


    }

    private void CharacterSkillsGeneration(Div statBox,Div infoBoxParent) {
        Div skillGrid = new Div();

        skillGrid.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(1, 1fr)")
                .set("gap", "10px");

        for (int i = 0; i < 16; i++) { //TODO make this a loop based on characteristics and skills

            Div skillItem = new Div();
            skillItem.getStyle()
                    .set("display", "grid")
                    .set("grid-template-columns", "180px 80px 120px")
                    .set("flex-direction", "column")
                    .set("gap", "5px");

            Button skillButton = new Button("skill number: " + i, e -> {
                infoBoxParent.setVisible(true);
            });

            TextField field = new TextField();
            field.setRequiredIndicatorVisible(true);
            field.setAllowedCharPattern("[0-9]");
            field.setMinLength(1);
            field.setMaxLength(2);
            field.setI18n(new TextField.TextFieldI18n()
                    .setRequiredErrorMessage("Enter a number")
                    .setMaxLengthErrorMessage("Too large"));
            fieldArray.add(field);

            //TODO denne her roller bare et tal, ikke de terninger man skal rulle med
            Button randomButton = new Button("roll die", e -> {
                int randomValue = (int) (Math.random() * 100);
                field.setValue(String.valueOf(randomValue));
            });

            skillItem.add(skillButton, field, randomButton);
            skillGrid.add(skillItem);

        }

        statBox.add(skillGrid);
        Button saveButton = new Button("Save Character", e -> {
            //TODO save character to database
            for (TextField field : fieldArray) {
                //testing
                System.out.println(field.getValue());
            }

        });
        statBox.add(saveButton);

        add(statBox);
    }
    private void InfoCreator(Div infoBoxParent) {
        Div infoBoxTextContainer = getDiv();

        Button closeInfoButton = new Button("X", e -> {
            infoBoxParent.setVisible(false);
        });

        infoBoxParent.getStyle().set("background-color", "#474747").
                set("top", "0").
                set("bottom", "0").
                setWidth("33vw").
                set("position", "absolute").
                set("top", "0").
                set("right", "0").
                set("z-index", "10");


        infoBoxParent.add(closeInfoButton);
        infoBoxParent.add(infoBoxTextContainer);
        infoBoxParent.setVisible(false);

        add(infoBoxParent);
    }

    private static @NonNull Div getDiv() {
        Div infoBoxTextContainer = new Div();

        boolean isAdvanced = true; // TODO get boolean from database

        String skillOrTalent = (isAdvanced ? "Advanced skill" : "Basic skill"); //TODO: make this a text toggleable using boolean from database
        String objectName = "string from database";
        String objectCharacteristic = "int from database".toString();
        String skillMax = "int from database".toString(); //TODO should always be at least 1. (you can only take something once at least)
        String description = "string from database";

        Div infoLine1 = new Div(new Text(skillOrTalent));
        Div infoLine2 = new Div(new Text(objectName));
        Div infoLine3 = new Div(new Text(objectCharacteristic));
        Div infoLine4 = new Div(new Text(skillMax));
        Div infoLine5 = new Div(new Text(description));

        infoBoxTextContainer.add(infoLine1, infoLine2, infoLine3, infoLine4, infoLine5);

        return infoBoxTextContainer;
    }

}
