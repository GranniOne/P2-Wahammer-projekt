package com.P2.warhammer.views;

import com.P2.warhammer.Race.RaceRepository;
import com.P2.warhammer.Skills.Skill;
import com.P2.warhammer.Skills.SkillRepository;
import com.P2.warhammer.Talents.TalentRepository;
import com.P2.warhammer.careers.Career;
import com.P2.warhammer.careers.CareerRepository;
import com.P2.warhammer.characteristics.Characteristic;
import com.P2.warhammer.characteristics.CharacteristicsDiv;
import com.P2.warhammer.characters.Character;
import com.P2.warhammer.characters.CharacterRepository;
import com.P2.warhammer.characters.CharacterService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.PermitAll;

import java.util.*;


@PermitAll
@PageTitle("Character Creator Page")
@Route("characterCreator")
@StyleSheet("css/characterStyle.css")
public class CharacterCreatorView extends Div implements HasUrlParameter<String> {
    /*
    private final List<Button> skillButtonArray = new ArrayList<>();
    private final SkillRepository skillRepository;
    private final List<Skill> skills;
    private final List<Talent> talents;

    private final CharacterRepository characterRepository;

    private final CareerRepository careerRepository;
    private final List<Career> careers;

    private final RaceRepository raceRepository;
    private final List<Race> race, raceItems, careerItem ;
    private final TalentRepository talentRepository;
    private final CharacterService characterService;

    Map<String, ArrayList<IntegerField>> characteristicValues = new HashMap<>();
    private final Random random = new Random();
    private List<String> currentSkillList;

    List<Characteristic> characteristics;

     */

    Random random = new Random();
    private final SkillRepository skillRepository;
    private final CharacterRepository characterRepository;
    private final CharacterService characterService;
    private final CareerRepository careerRepository;
    private final TalentRepository talentRepository;
    private final RaceRepository raceRepository;
    private List<Career> careers;
    private List<Skill> skills;
    private Character globalCharacter;
    Div statBox = new Div();



    public CharacterCreatorView(SkillRepository skillRepository, CharacterRepository characterRepository, CareerRepository careerRepository, RaceRepository raceRepository, TalentRepository talentRepository, CharacterService characterService, List<Career> careers) {
        this.skillRepository = skillRepository;
        this.characterRepository = characterRepository;
        this.talentRepository = talentRepository;
        this.raceRepository = raceRepository;
        this.careerRepository = careerRepository;
        this.characterService = characterService;

        setClassName("div-page");
        getStyle().set("position", "relative");

    }
    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String Id) {
        String parameterCharacter = beforeEvent.getLocation().getQueryParameters().getSingleParameter("Character").orElse("");

            if(!parameterCharacter.isEmpty()){
                try {
                    globalCharacter = characterService.getCharacterFromId(parameterCharacter);
                } catch (NullPointerException e) {
                    System.out.println("characterID is not linked to character");
                }
            }else{
                globalCharacter =  new Character();
            }

        this.careers = careerRepository.findAll();

        this.skills = skillRepository.findAll();

        this.add(careerBox());
        this.add(statBox);
    }



    private IntegerField createField(int max) {
        IntegerField statField = new IntegerField();

        statField.setRequiredIndicatorVisible(true);

        statField.setMin(1);
        statField.setMax(max);

        statField.setStepButtonsVisible(true);

        statField.setI18n(new IntegerField.IntegerFieldI18n()
                .setRequiredErrorMessage("Enter a number")
                .setMaxErrorMessage("Too large"));

        return statField;
    }

    int myParse(IntegerField integerField){ //bare så den ikke går i stykker hvis ikke alle felter er fyldte. den sparer så der ikke er like 4 linjer med ren if-statements
        if (integerField.isEmpty()){
            return 0;
        }
        int value = integerField.getValue();
        return value;
    }

    private Div careerBox(){
        Div div = new Div();

        TextField socialClassField = new TextField();
        socialClassField.setReadOnly(true);
        socialClassField.setLabel("Social Class");

        TextField statusField = new TextField();
        statusField.setReadOnly(true);
        statusField.setLabel("Status");

        ComboBox<Career> dropdownMenu = new ComboBox<>("Choose a career");

        dropdownMenu.setItems(careers);
        dropdownMenu.setItemLabelGenerator(Career::getName);

        div.add(dropdownMenu);
        IntegerField levelField = createField( 4);
        levelField.setLabel("Level");
        levelField.setValue(1);
        levelField.setReadOnly(true);

        div.add(levelField);
        div.add(socialClassField);
        div.add(statusField);

        if (!dropdownMenu.isEmpty()){
            levelField.setReadOnly(false);
        }

        //adds listener so the skills talents and characteristics can change when another career or level is selected
        levelField.addValueChangeListener(e ->
                careerBoxChanged(socialClassField, statusField, levelField, dropdownMenu)
        );
        dropdownMenu.addValueChangeListener(e ->
                careerBoxChanged(socialClassField, statusField, levelField, dropdownMenu)
        );
        return div;
    }


    private void careerBoxChanged(TextField socialClassField, TextField moneyField, IntegerField levelField, ComboBox<Career> dropdownMenu){

        renderCharacteristicsDivs();
        levelField.setReadOnly(false);
        Career currentCareer = dropdownMenu.getValue();

        try {
            socialClassField.setValue(currentCareer.getSocialClass());

            if (levelField.getValue() == null) {
                levelField.setValue(1);
            }
            int level = levelField.getValue();

            List<Integer> status = currentCareer.getLevelStatusList();
            moneyField.setValue("Your status is " + status.get(level - 1) + " Brass coins");

            globalCharacter.setCareer(currentCareer);
            globalCharacter.setStatusLevel(status.get(level - 1));
        }catch (Exception e){
            System.out.println("Klassen eksisterer ikke");
        }


    }


    private void renderCharacteristicsDivs(){

        Div characteristicsStatBox = new Div();

        int characteristicNumber = 0;
        for (Characteristic characterCharacteristic : globalCharacter.getCharacteristics()){
            CharacteristicsDiv charDiv = new CharacteristicsDiv(characterCharacteristic.getName());
            charDiv.getStyle()
                    .set("grid-template-columns", "180px 80px 120px")
                    .set("background-color", "#F5A3BE");

            TextField charField = new TextField ();
            charField.setReadOnly(true);
            charField.setLabel("Characteristic name");
            charField.setValue(characterCharacteristic.getName());

            IntegerField raceField = new IntegerField ();
            raceField.setReadOnly(true);
            raceField.setLabel("Species bonus");
            raceField.setValue(globalCharacter.getCharacteristics().get(characteristicNumber).getRacemod());

            IntegerField baseField = createField(99);
            baseField.setLabel("Rolled stat");
            IntegerField modifierField = createField(99);
            modifierField.setLabel("Modifier");
            IntegerField penaltyField = createField(99);
            penaltyField.setLabel("Penalty");
            IntegerField totalField = new IntegerField();
            totalField.setLabel("Total");


            Button rollButton = new Button("Roll charateristic");
            rollButton.addClickListener(event -> {
                int die1 = random.nextInt(10) + 1;
                int die2 = random.nextInt(10) + 1;
                baseField.setValue(die1+die2);
            });

            Runnable updateTotal = () -> {
                int total = myParse(baseField) + myParse(modifierField) - myParse(penaltyField) + myParse(raceField);

                totalField.setValue(total);
                charDiv.setTotalField(totalField);

                //SOMEHOW UPDATE SKILL ELEMENTS HERE

                characterCharacteristic.setBase(baseField.getValue());
                characterCharacteristic.setModifier(modifierField.getValue());
                characterCharacteristic.setPenalty(penaltyField.getValue());
                characterCharacteristic.setBase(raceField.getValue());
            };
            baseField.addValueChangeListener(e -> updateTotal.run());
            modifierField.addValueChangeListener(e -> updateTotal.run());
            penaltyField.addValueChangeListener(e -> updateTotal.run());
            raceField.addValueChangeListener(e -> updateTotal.run());
            charDiv.setTotalField(totalField);


            charDiv.add(charField,raceField,baseField,modifierField,penaltyField,totalField,rollButton);


            //RENDER SKILL ELEMENTS HERE


            characteristicsStatBox.add(charDiv);
            characteristicNumber++;
        }

        statBox.removeAll();
        statBox.add(characteristicsStatBox);
    }


/*

private void renderSkillElements(Map<String, CharacteristicsDiv> characteristicsMap, Div infoBoxParent, Div characteristicsGrid, Career career, int level){
        //TODO make this loop only run for race skills and career skills

        List<List<String>> allowedSkills = new ArrayList<>(career.getLevelSkillsList()); //laver et hashset og chekker i loopet om skillen er i sættet
        System.out.println("det her er allowedSkills: " + allowedSkills);
        skills.forEach(skill -> {                                          //kører igennem databasen og looper for alle skills
            boolean found = false;
            for (int i = 0; i < level; i++) {
                if (allowedSkills.get(i).contains(skill.getName())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("breaks");
                System.out.println(skill.getName());
                return;
            }


            CharacteristicsDiv skillGrid = characteristicsMap.get(skill.getCharacteristic());
            if (skillGrid != null) {
                Div skillDiv = new Div();

                skillDiv.getStyle().set("background-color", "#F5B0A3");

                Button skillButton = new Button(skill.getName(), e -> {
                    infoBoxParent.removeAll();
                    SkillInfoCreator(skill,infoBoxParent);
                    infoBoxParent.setVisible(true);
                });

                IntegerField skillCharValueField = new IntegerField();
                skillCharValueField.setReadOnly(true);
                IntegerField sourceTotal = skillGrid.getTotalField();


                IntegerField skillModifierField = createField(99);
                IntegerField skillPenaltyField = createField(99);
                IntegerField skillTotalField = createField(99);
                skillTotalField.setReadOnly(true);
                skillCharValueField.setValue(sourceTotal.getValue());

                Runnable updateTotal = () -> {
                    int total = myParse(skillCharValueField) + myParse(skillModifierField) - myParse(skillPenaltyField);

                    skillTotalField.setValue(total);
                };

                sourceTotal.addValueChangeListener(e -> {
                    skillCharValueField.setValue(sourceTotal.getValue());
                    updateTotal.run();
                });



                skillCharValueField.addValueChangeListener(e -> updateTotal.run());
                skillModifierField.addValueChangeListener(e -> updateTotal.run());
                skillPenaltyField.addValueChangeListener(e -> updateTotal.run());

                skillDiv.add(skillButton);
                skillDiv.add(skillCharValueField);
                skillDiv.add(skillModifierField);
                skillDiv.add(skillPenaltyField);
                skillDiv.add(skillTotalField);
                skillGrid.add(skillDiv);
            }
        });

        characteristicsMap.values().forEach(characteristicsGrid::add);
    }




    private Div RaceBox(){
        Div div1 = new Div();

        ComboBox<String> dropdownMenu = new ComboBox<>("choose a species");
        dropdownMenu.setItems(raceItems.stream().map(Race::getRace).toList());

        Button button1 = new Button("Roll for Species", e -> {
            int roll = ThreadLocalRandom.current().nextInt(1, 101);

            Race raceTable = raceRepository.findById("species_table").orElseThrow();

            String result = raceTable.getEntries().stream()
                    .filter(entry -> roll >= entry.getMin() && roll <= entry.getMax())
                    .map(RaceEntry::getSpecies)
                    .findFirst()
                    .orElse("Unknown");

            dropdownMenu.setValue(result);
        });

        div1.add(dropdownMenu);
        div1.add(button1);
        return div1;
    }


*/


/*
    private Map<String, Characteristic> characterHashmapCharacteristics(){

        Map<String, Characteristic> characterHashmapCharacteristics = new HashMap<>();

        for (Map.Entry<String, ArrayList<IntegerField>> characteristicElement : characteristicValues.entrySet()) {
            String key = characteristicElement.getKey();
            ArrayList<IntegerField> values = characteristicElement.getValue();
            int baseValue = values.get(0).getValue();
            int modifierValue = values.get(1).getValue();
            int penaltyValue = values.get(2).getValue();

            Characteristic characteristic = new Characteristic(key, baseValue, modifierValue, penaltyValue);

            characterHashmapCharacteristics.put(key, characteristic);

        }
        return characterHashmapCharacteristics;
    }

*/
    /*
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
                name = " 'no name was entered' ";
            }
            if (age <= 0){
                age = 0;
            }
            if (xp <= 0){
                xp = 0;
            }

            //Character character = new Character(name,null,null, age, xp, characterHashmapCharacteristics());
            //characterRepository.save(character);
            //System.out.println("Saved Charachter: " + character.getName());
        });
        add(saveButton);
    }




    private Div renderTalentElements(Div infoBoxParent, Career career, int level){
        Div characterTalentDiv = new Div();
        List<List<String>> allowedTalents = new ArrayList<>(career.getLevelTalentsList());
        talents.forEach(talent -> {
            boolean found = false;
            for (int i = 0; i < level; i++) {
                if (allowedTalents.get(i).contains(talent.getName())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("breaks");
                System.out.println(talent.getName());
                return;
            }

            Div talentDiv = new Div();

            talentDiv.getStyle().set("background-color", "#F5B0A3");

            Button talentButton = new Button(talent.getName(), e -> {
                infoBoxParent.removeAll();
                TalentInfoCreator(talent,infoBoxParent);
                infoBoxParent.setVisible(true);
            });

            TextField talentTakenField = new TextField ();
            talentTakenField.setReadOnly(true);
            talentTakenField.setValue("Talent amount goes here");

            TextField talentMaxField = new TextField ();
            talentMaxField.setReadOnly(true);
            talentMaxField.setValue("Max amount of talent goes here");

            talentDiv.add(talentButton);

            talentDiv.add(talentTakenField);
            talentDiv.add(talentMaxField);

            characterTalentDiv.add(talentDiv);

        });

        return characterTalentDiv;
    }

    int myParse(IntegerField integerField){ //bare så den ikke går i stykker hvis ikke alle felter er fyldte. den sparer så der ikke er like 4 linjer med ren if-statements
        if (integerField.isEmpty()){
            return 0;
        }
        int value = integerField.getValue();
        return value;
    }


    //renders characteristics and puts them into a hashmap used for skills rendering
    private void renderCharacteristicsElements(String characteristicName, Map<String, CharacteristicsDiv> characteristicsMap){
        CharacteristicsDiv charDiv = new CharacteristicsDiv(characteristicName);
        characteristicsMap.put(characteristicName, charDiv);


        charDiv.getStyle()
                .set("grid-template-columns", "180px 80px 120px")
                .set("background-color", "#F5A3BE");

        TextField charField = new TextField ();
        charField.setReadOnly(true);
        charField.setValue(characteristicName);

        IntegerField raceField = new IntegerField ();
        raceField.setReadOnly(true);
        raceField.setValue(20);//Species bonus goes here plz fix x3


        IntegerField baseField = createField(99);
        baseField.setLabel("base");
        IntegerField modifierField = createField(99);
        modifierField.setLabel("mod");
        IntegerField penaltyField = createField(99);
        penaltyField.setLabel("penalty");
        IntegerField totalField = new IntegerField();
        totalField.setLabel("total");

        Button rollButton = new Button("Roll charateristic");
        rollButton.addClickListener(event -> {
            int die1 = random.nextInt(10) + 1;
            int die2 = random.nextInt(10) + 1;
            baseField.setValue(die1+die2);
        });

        Runnable updateTotal = () -> {
            int total = myParse(baseField) + myParse(modifierField) - myParse(penaltyField) + myParse(raceField);

            totalField.setValue(total);
            charDiv.setTotalField(totalField);
        };
        baseField.addValueChangeListener(e -> updateTotal.run());
        modifierField.addValueChangeListener(e -> updateTotal.run());
        penaltyField.addValueChangeListener(e -> updateTotal.run());
        raceField.addValueChangeListener(e -> updateTotal.run());
        charDiv.setTotalField(totalField);


        ArrayList<IntegerField> fieldArray = new ArrayList<> (
            List.of(
                baseField,
                modifierField,
                penaltyField
            )
        );
        characteristicValues.put(characteristicName, fieldArray);

        charDiv.add(charField);
        charDiv.add(raceField);
        charDiv.add(baseField);
        charDiv.add(modifierField);
        charDiv.add(penaltyField);
        charDiv.add(totalField);
        charDiv.add(rollButton);

    }

    private void renderAllCharacterElements(Div statBox, Div infoBoxParent, Career career, int level) {

        //initializes hashmap for characteristics. used to put skills into characteristics
        Map<String, CharacteristicsDiv> characteristicsMap = new HashMap<>();
        Div characteristicsGrid = new Div();
        characteristicsGrid.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(1, 1fr)")
                .set("color", "blue");

        //add characteristics to hashmap and create visual elements
        List<String> characteristicsStringList = List.of("Weapon Skill", "Ballistic Skill", "Strength", "Toughness", "Initiative", "Agility", "Dexterity", "Intelligence", "Willpower", "Fellowship");
        for (String characteristicName : characteristicsStringList){
           renderCharacteristicsElements(characteristicName, characteristicsMap);
        }

        renderSkillElements(characteristicsMap, infoBoxParent, characteristicsGrid, career, level);
        statBox.add(characteristicsGrid);
        statBox.add(renderTalentElements(infoBoxParent, career, level));
        add(statBox);

    }

    private void SkillInfoCreator(Skill skill, Div infoBoxParent) {
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

    private void TalentInfoCreator(Talent talent, Div infoBoxParent) {
        Div infoBoxTextContainer = getTalentDiv(talent);
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
        infoBoxParent.setVisible(false);
        infoBoxParent.add(infoBoxTextContainer);
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

     */

}
