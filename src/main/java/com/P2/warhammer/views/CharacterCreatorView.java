package com.P2.warhammer.views;


import com.P2.warhammer.Skills.Skill;
import com.P2.warhammer.Skills.SkillRepository;
import com.P2.warhammer.careers.Career;
import com.P2.warhammer.careers.CareerRepository;
import com.P2.warhammer.characteristics.CharacteristicsDiv;
import com.P2.warhammer.characters.Character;
import com.P2.warhammer.characters.CharacterRepository;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.jspecify.annotations.NonNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


@PermitAll
@PageTitle("Character Creator Page")
@Route("characterCreator")
@StyleSheet("css/characterStyle.css")
public class CharacterCreatorView extends Div {
    private final List<TextField> baseFieldArray = new ArrayList<>();
    private final List<TextField> modFieldArray = new ArrayList<>();
    private final List<Button> skillButtonArray = new ArrayList<>();
    private final SkillRepository skillRepository;
    private final List<Skill> skills;
    private final CharacterRepository characterRepository;
    private final CareerRepository careerRepository;
    private final List<Career> careers;
    public CharacterCreatorView(SkillRepository skillRepository, CharacterRepository characterRepository, CareerRepository careerRepository) {
        this.skillRepository = skillRepository;
        this.skills = skillRepository.findAll();
        this.characterRepository = characterRepository;
        this.careerRepository = careerRepository;
        this.careers = careerRepository.findAll();

        setClassName("div-page");
        getStyle().set("position", "relative");

        Div statBox = new Div();

        Div infoBoxParent = new Div();

        add(CareerBox());
        CharacterSkillsGeneration(statBox,infoBoxParent);
        saveCharacterButtonCreator();

    }


    //TODO FIND UD AF HVORDAN MAN ENFORCER STØRRELSEN. Så vidt jeg kan læse mig til kan man skrive som jeg har gjort men jeg kan ikke få den til at stoppe en i at skrive for stort, kun bogstaver.
    private TextField createField(String size, int max) {
        TextField statField = new TextField();
        statField.setRequiredIndicatorVisible(true);
        statField.setAllowedCharPattern("[0-9]");
        statField.setPattern(size);
        statField.setMinLength(1);
        statField.setMaxLength(max);
        statField.setI18n(new TextField.TextFieldI18n()
                .setRequiredErrorMessage("Enter a number")
                .setMaxLengthErrorMessage("Too large"));

        return statField;
    }

    //gets all careers and adds them to a dropdown menu in a div it returns. i will code it such that when you add a career it is saved to the character and then you can change it later.
    //no multi-jobbing >:(
    private Div CareerBox(){
        Div div = new Div();

        ComboBox<String> dropdownMenu = new ComboBox<>("choose a career");

        System.out.println("her");
        for (Career career : careers) {
            System.out.println(career.getName());
            dropdownMenu.getListDataView().addItem(career.getName());
        }
        div.add(dropdownMenu);
        div.add(createField("[1-4]", 4));

        return div;
    }


    private void saveCharacterButtonCreator() {
        TextField nameField = new TextField("Character Name");
        IntegerField ageField = new IntegerField("Character age");
        IntegerField xpField = new IntegerField("Character XP");
        add(nameField);
        add(ageField);
        add(xpField);

        Button saveButton = new Button("Save Character", e -> {
            String name = nameField.getValue();
            Integer age = ageField.getValue();
            Integer xp = xpField.getValue();

            if (name == null || name.isEmpty()){
                System.out.println("Name is requried");
                return;
            }

            com.P2.warhammer.characters.Character character = new Character(name,null,null, age, xp);
            characterRepository.save(character);
            System.out.println("Saved Charachter: " + character.getName());
        });
        add(saveButton);
    }

    private void CharacterSkillsGeneration(Div statBox,Div infoBoxParent) {

        Div characteristicsGrid = new Div();
        characteristicsGrid.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(1, 1fr)")
                .set("gap", "10px");

        List<String> characteristicsStringList = List.of("Weapon skill", "Ballistic skill", "Strength", "Toughness", "Initiative", "Agility", "Dexterity", "Intelligence", "Willpower", "Fellowship");

        for (String charName : characteristicsStringList){
            CharacteristicsDiv characteristicsDiv = new CharacteristicsDiv(charName);
            characteristicsDiv.getStyle()
                    //.set("display", "grid")
                    .set("grid-template-columns", "180px 80px 120px")
                    //.set("flex-direction", "column")
                    .set("gap", "5px");

            TextField charField = new TextField ();
            charField.setReadOnly(true);
            charField.setValue(charName);

            TextField raceField = new TextField ();
            raceField.setReadOnly(true);
            raceField.setValue("Species bonus goes here plz fix x3");

            TextField baseField = createField("[0-99]", 99);
            TextField modifierField = createField("[0-99]", 99);
            TextField penaltyField = createField("[0-99]", 99);
            baseFieldArray.add(baseField);
            modFieldArray.add(modifierField);


            characteristicsDiv.add(charField);
            characteristicsDiv.add(raceField);
            characteristicsDiv.add(baseField);
            characteristicsDiv.add(modifierField);
            characteristicsDiv.add(penaltyField);


            characteristicsGrid.add(characteristicsDiv);
        }


        /*
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
            skillItem.add(skillButton);
            skillButtonArray.add(skillButton);

            TextField baseField = createField("[0-99]", 99);
            TextField modifierField = createField("[0-99]", 99);

            baseFieldArray.add(baseField);
            modFieldArray.add(modifierField);

           /*
            //TODO denne her skal kun være på characteristics. man kan ikke rulle skills og talents i character creatoren kun i spillearket
            Button randomButton = new Button("roll die", e -> {
                int randomValue = (int) (Math.random() * 100);
                baseField.setValue(String.valueOf(randomValue));
            });

            skillItem.add(skillButton, baseField, modifierField);
            skillGrid.add(skillItem);
        });


        statBox.add(skillGrid);

        */


         /* TODO denne her er sin egen ting xd
        Button saveButton = new Button("Save Character", e -> {
            //TODO save character to database
            //lige nu printer denne her bare values fra de fields som ikke er tomme
            for (int i = 0; i < baseFieldArray.size(); i++) {
                String name = skillButtonArray.get(i).getText();
                String baseValue = baseFieldArray.get(i).getValue();
                String modValue = modFieldArray.get(i).getValue();
                if (!baseValue.isEmpty() && !modValue.isEmpty()) {
                    System.out.println(name + ": " + Integer.parseInt(baseValue)+Integer.parseInt(modValue));
                }
            }

        });


        statBox.add(saveButton);
 */

        statBox.add(characteristicsGrid);
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
