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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

        ComboBox<Career> dropdownMenu = new ComboBox<>("choose a career");

        dropdownMenu.setItems(careers);
        dropdownMenu.setItemLabelGenerator(Career::getName);

        div.add(dropdownMenu);
        div.add(createField("[1-4]", 1));
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

            Character character = new Character(name,null,null, age, xp);
            characterRepository.save(character);
            System.out.println("Saved Charachter: " + character.getName());
        });
        add(saveButton);
    }

    //TODO vi skal opdatere UI'en og køre denne her funktion for at vise
    private void SetCharacteristics(int level, Career career){
        List<String> careerCharacteristicList = career.getLevelCharacterticsList();
        List<String> characterCharacteristicList = new ArrayList<>();
        for (int i = 0; i < level+2 && i < careerCharacteristicList.size(); i++) {
            characterCharacteristicList.add(careerCharacteristicList.get(i));
        }
    }

    private void SetTalents(int level, Career career){
        List<String> careerTalentList = career.getLevelTalentsList();
        List<String> characterTalentList = new ArrayList<>();
        for (int i = 0; i < level+2 && i < careerTalentList.size(); i++) {
            characterTalentList.add(careerTalentList.get(i));
        }
    }

    private void SetSkills(int level, Career career){
        List<String> careerSkillList = career.getLevelSkillsList();
        List<String> characterSkillList = new ArrayList<>();
        for (int i = 0; i < level+2 && i < careerSkillList.size(); i++) {
            characterSkillList.add(careerSkillList.get(i));
        }
    }


    private void CharacterSkillsGeneration(Div statBox,Div infoBoxParent) {

        Map<String, CharacteristicsDiv> characteristicsMap = new HashMap<>();
        Div characteristicsGrid = new Div();
        characteristicsGrid.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(1, 1fr)")
                .set("color", "blue");


        List<String> characteristicsStringList = List.of("Weapon skill", "Ballistic skill", "Strength", "Toughness", "Initiative", "Agility", "Dexterity", "Intelligence", "Willpower", "Fellowship");

        for (String charName : characteristicsStringList){
            CharacteristicsDiv charDiv = new CharacteristicsDiv(charName);
            characteristicsMap.put(charName, charDiv);

            charDiv.getStyle()
                    .set("grid-template-columns", "180px 80px 120px")
                    .set("background-color", "#F5A3BE");

            TextField charField = new TextField ();
            charField.setReadOnly(true);
            charField.setValue(charName);

            TextField raceField = new TextField ();
            raceField.setReadOnly(true);
            raceField.setValue("Species bonus goes here plz fix x3");

            TextField baseField = createField("[0-99]", 2);
            TextField modifierField = createField("[0-99]", 2);
            TextField penaltyField = createField("[0-99]", 2);
            baseFieldArray.add(baseField);
            modFieldArray.add(modifierField);


            charDiv.add(charField);
            charDiv.add(raceField);
            charDiv.add(baseField);
            charDiv.add(modifierField);
            charDiv.add(penaltyField);

        }

        skills.forEach(skill -> {
            CharacteristicsDiv skillGrid = characteristicsMap.get(skill.getCharacteristic());
            if (skillGrid != null) {
                Div skillDiv = new Div();

                skillDiv.getStyle().set("background-color", "#F5B0A3");

                Button skillButton = new Button(skill.getName(), e -> {
                    infoBoxParent.removeAll();
                    InfoCreator(skill,infoBoxParent);
                    infoBoxParent.setVisible(true);
                });

                TextField charValueField = new TextField ();
                charValueField.setReadOnly(true);
                charValueField.setValue("Characteristic value goes here");

                TextField baseField = createField("[0-99]", 2);
                TextField modifierField = createField("[0-99]", 2);
                TextField penaltyField = createField("[0-99]", 2);

                skillDiv.add(skillButton);
                skillDiv.add(charValueField);
                skillDiv.add(baseField);
                skillDiv.add(modifierField);
                skillDiv.add(penaltyField);

                skillGrid.add(skillDiv);
            }
            });

        characteristicsMap.values().forEach(characteristicsGrid::add); //foreach my beloved ❤️❤️❤️ ⸜(｡˃ ᵕ ˂ )⸝♡ °❀⋆.ೃ࿔*:･°❀⋆.ೃ࿔*:･°❀⋆.ೃ࿔*:･


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

            TextField baseField = createField("[0-99]", 2);
            TextField modifierField = createField("[0-99]", 2);

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
