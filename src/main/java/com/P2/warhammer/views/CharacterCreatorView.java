package com.P2.warhammer.views;


import com.P2.warhammer.Skills.Skill;
import com.P2.warhammer.Skills.SkillRepository;
import com.P2.warhammer.Talents.Talent;
import com.P2.warhammer.characteristics.Characteristic;
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
    private final SkillRepository skillRepository;
    private final List<Skill> skills;
    public CharacterCreatorView(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
        this.skills = skillRepository.findAll();

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






    }

    private void CharacterSkillsGeneration(Div statBox,Div infoBoxParent) {
        Div skillGrid = new Div();

        skillGrid.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(1, 1fr)")
                .set("gap", "10px");
        skills.forEach(skill -> {
            Div skillItem = new Div();
            skillItem.getStyle()
                    .set("display", "grid")
                    .set("grid-template-columns", "180px 80px 120px")
                    .set("flex-direction", "column")
                    .set("gap", "5px");

            Button skillButton = new Button(skill.getName(), e -> {
                infoBoxParent.removeAll();
                InfoCreator(skill,infoBoxParent);
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
        });


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
    private void InfoCreator(Skill skill, Div infoBoxParent) {
        Div infoBoxTextContainer = getSkillDiv(skill);

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

    private @NonNull Div getSkillDiv(Skill skill) {
        Div infoBoxTextContainer = new Div();
        Div infoLine1 = new Div(new Text(skill.getCategory()));
        Div infoLine2 = new Div(new Text(skill.getName()));
        Div infoLine3 = new Div(new Text(skill.getCharacteristic()));
        Div infoLine4 = new Div(new Text("its maximum power"));
        Div infoLine5 = new Div(new Text(skill.getDescription()));
        infoBoxTextContainer.add(infoLine1, infoLine2, infoLine3, infoLine4, infoLine5);
        return infoBoxTextContainer;
    }

    /*
    //I KNOW AT MAN KAN GØRE DE HER FUNKTIONER TIL EN JEG TESTER NOGET
    private @NonNull Div getTalentDiv(Talent talent) {
        Div infoBoxTextContainer = new Div();
        Div infoLine1 = new Div(new Text(talent.getCategory()));
        Div infoLine2 = new Div(new Text(talent.getName()));
        Div infoLine3 = new Div(new Text(talent.getCharacteristic()));
        Div infoLine4 = new Div(new Text(talent.getDescription()));
        infoBoxTextContainer.add(infoLine1, infoLine2, infoLine3, infoLine4);
        return infoBoxTextContainer;
    }

    private @NonNull Div getCharacteristicDiv(Characteristic characteristic) {
        Div infoBoxTextContainer = new Div();
        Div infoLine1 = new Div(new Text(characteristic.getCategory()));
        Div infoLine2 = new Div(new Text(characteristic.getName()));
        infoBoxTextContainer.add(infoLine1, infoLine2);
        return infoBoxTextContainer;
    }
*/
}
