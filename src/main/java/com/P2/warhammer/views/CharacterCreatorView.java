package com.P2.warhammer.views;

import com.P2.warhammer.Race.Race;
import com.P2.warhammer.Race.RaceEntry;
import com.P2.warhammer.Race.RaceRepository;
import com.P2.warhammer.Skills.Skill;
import com.P2.warhammer.Skills.SkillRepository;
import com.P2.warhammer.Talents.Talent;
import com.P2.warhammer.Talents.TalentRepository;
import com.P2.warhammer.careers.Career;
import com.P2.warhammer.careers.CareerRepository;
import com.P2.warhammer.characteristics.Characteristic;
import com.P2.warhammer.characteristics.CharacteristicsDiv;
import com.P2.warhammer.characters.Character;
import com.P2.warhammer.characters.CharacterRepository;
import com.P2.warhammer.characters.CharacterService;
import com.P2.warhammer.items.WarhammerItem;
import com.P2.warhammer.users.UserRepository;
import com.P2.warhammer.utilities.Utilities;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.jspecify.annotations.NonNull;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

//Current known bugs:
// 1. clicking on character creator in the navigator bar, duplicates the site instead of reloading it
// 2. is this skill bought button unchecks everytime you update characteristics
// 3. updating characteristic removes all stats from corresponding skills
// 4. characterCreator crashes if you save a character as lvl 4 and try to edit it



@PermitAll
@PageTitle("Character Creator Page")
@Route("characterCreator")
@StyleSheet("css/characterStyle.css")
public class CharacterCreatorView extends Div implements HasUrlParameter<String> {
    private final UserRepository userRepository;
    Map<String, ArrayList<IntegerField>> characteristicValues = new HashMap<>();
    Map<String, Runnable> characteristicUpdates = new HashMap<>();

    Random random = new Random();
    private final SkillRepository skillRepository;
    private final CharacterRepository characterRepository;
    private final CharacterService characterService;
    private final CareerRepository careerRepository;
    private final TalentRepository talentRepository;
    private final RaceRepository raceRepository;
    private final List<RadioButtonGroup<Integer>> startingBonusGroups = new ArrayList<>();
    private List<Career> careersForSpecies = new ArrayList<>();
    private List<Skill> skills;
    private List<Talent> talents;
    private List<Race> raceItems;
    private Character globalCharacter;
    private Race currentRaceTable;

    Div talentBox = new Div();
    Div startingSkillsBox = new Div();
    Div statBox = new Div();
    Div inventoryDiv = new Div();
    IntegerField levelField;
    Map<String, IntegerField> raceFields = new HashMap<>();

    private static final int MaxThree = 3;
    private static final int MaxFive = 3;



    public CharacterCreatorView(SkillRepository skillRepository, CharacterRepository characterRepository, CareerRepository careerRepository, RaceRepository raceRepository, TalentRepository talentRepository, CharacterService characterService, List<Career> careers, UserRepository userRepository) {

        this.skillRepository = skillRepository;
        this.characterRepository = characterRepository;
        this.talentRepository = talentRepository;
        this.raceRepository = raceRepository;
        this.careerRepository = careerRepository;
        this.characterService = characterService;

        setClassName("div-page");
        getStyle().set("position", "relative");
        this.userRepository = userRepository;

    }
    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String Id) {
        VerticalLayout container = new VerticalLayout();
        container.addClassName("container");
        container.setWidth("60%");
        container.getStyle().set("margin", "0 auto");

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

        this.raceItems = raceRepository.findAll();
        this.skills = skillRepository.findAll();
        this.talents = talentRepository.findAll();
        this.careersForSpecies = careerRepository.findAll();

        renderCharacteristicsDivs();
        inventoryDivCreator();
        startingSkillsBox.add(StartingSkillsAndTalents());

        container.add(IntroBox(), raceBox(), careerBox(), statBox, talentBox, startingSkillsBox, trappings(), inventoryDiv, CharacterInfoBox());
        add(container);
    }

    private Div IntroBox(){
        Div intro = new Div();
        intro.setWidthFull();

        VerticalLayout layout = new VerticalLayout();
        layout.setWidthFull();

        H1 headline = new H1("WFRP 4e Character Creator");
        headline.getStyle().set("margin", "0 auto");

        Paragraph paragraph1 = new Paragraph(
                "Welcome to the WFRP 4e Character Creator. Here you can create a new character or edit an existing one for Warhammer Fantasy Roleplay 4th Edition. " +
                        "The tools below will guide you through each step of the process, including species, career, characteristics, skills, talents, and equipment, " +
                        "helping you build a complete and playable character, through a guided introduction to the character creator." +
                        " As with every roleplaying game, each group likes to play things a little different so its recommended to go through your choices with you game master" +
                        " to make sure everything is in order."
        );

        layout.add(headline, paragraph1);
        intro.add(layout);
        return intro;
    }


    private IntegerField createField(int max) {
        IntegerField statField = new IntegerField();

        statField.setRequiredIndicatorVisible(true);

        statField.setMin(0);
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


    private void addTrappings(Career career, int level){ //TODO lille bug her med at den giver de samme trappings flere gange hvis man gemmer karakteren
        List<WarhammerItem> inventory = globalCharacter.getInventory();

        for (int i = 0; i < level; i++) {
            String newItems = career.getLevelTrappingsList().get(i);
            String[] splitItems = newItems.split(",");

            for (String item : splitItems) {
                WarhammerItem addedItem = new WarhammerItem(item.trim(), 1); //TODO add amount of added trapping
                inventory.add(addedItem);
            }
        }
        globalCharacter.setInventory(inventory);
    }

    private void inventoryDivCreator(){
        for (WarhammerItem inventoryItem : globalCharacter.getInventory()){
             Div inventoryElementDiv = new Div();
             TextField itemField = new TextField();
             itemField.setValue(inventoryItem.getName());
             itemField.setReadOnly(true);

             IntegerField itemAmount = new IntegerField();
             itemAmount.setValue(inventoryItem.getAmount());

             inventoryElementDiv.add(itemField);
             inventoryElementDiv.add(itemAmount);

             inventoryDiv.add(inventoryElementDiv);
        }
    }

    private void saveCharacter(){

        globalCharacter.setUser(Utilities.getUserFromAuthentication());
        this.characterService.addCharacter(globalCharacter);
    }

    private Div CharacterInfoBox(){
        Div div = new Div();

        VerticalLayout layout = new VerticalLayout();
        layout.setWidthFull();

        H1 headline = new H1("6. Adding Detail");
        //headline.getStyle().set("margin", "0 auto");

        TextField nameField = new TextField();
        nameField.setLabel("Character name");
        try {
            nameField.setValue(globalCharacter.getName());
        } catch (NullPointerException e) {
        }
        nameField.addValueChangeListener(e -> globalCharacter.setName(nameField.getValue()));

        IntegerField ageField = new IntegerField();
        ageField.setLabel("Age");
        try {
            ageField.setValue(globalCharacter.getAge());
        } catch (NullPointerException e) {
        }
        ageField.addValueChangeListener(e -> globalCharacter.setAge(ageField.getValue()));

        IntegerField xpField = new IntegerField();
        xpField.setLabel("XP");
        try {
            xpField.setValue(globalCharacter.getExperience());
        } catch (NullPointerException e) {
        }
        xpField.addValueChangeListener(e -> globalCharacter.setExperience(xpField.getValue()));

        Button saveCharacterButton = new Button("Save Character", e -> {
            saveCharacter();
        });

        layout.add(headline);
        div.add(layout, nameField, ageField, xpField, saveCharacterButton);

        nameField.addValueChangeListener(e ->
                globalCharacter.setName(nameField.getValue())
        );
        return div;
    }


    private Div careerBox(){
        Div div = new Div();

        VerticalLayout layout = new VerticalLayout();
        layout.setWidthFull();

        H1 headline = new H1("2. Choose Career");
        headline.getStyle().set("margin", "0 auto");

        TextField socialClassField = new TextField();
        socialClassField.setReadOnly(true);
        socialClassField.setLabel("Social Class");

        try {
            socialClassField.setValue(globalCharacter.getCareer().getSocialClass());
        } catch (NullPointerException e) {
        }

        TextField statusField = new TextField();
        statusField.setReadOnly(true);
        statusField.setLabel("Status");
        try {
            statusField.setValue(String.valueOf(globalCharacter.getCareer().getLevelStatusList().get(globalCharacter.getLevel())));
        } catch (NullPointerException e) {
        }

        ComboBox<Career> dropdownMenu = new ComboBox<>("Choose a career");

        dropdownMenu.setItems(careersForSpecies);
        dropdownMenu.setItemLabelGenerator(Career::getName);

        try {
            dropdownMenu.setValue(globalCharacter.getCareer());
        } catch (NullPointerException e) {
        }

        Button RandomCareerButton = new Button("Roll for career",e ->{
            if (currentRaceTable == null) {
                System.out.println("No race selected");
                return;
            }

            int roll = ThreadLocalRandom.current().nextInt(1, 15);
            System.out.println("Career roll: " + roll );

            RaceEntry result = currentRaceTable.getEntries().stream().filter(entry -> roll >= entry.getMin() && roll <= entry.getMax()).findFirst().orElseThrow();
            Career career = careerRepository.findAll().stream().filter(c -> c.getName().equals(result.getCareer())).findFirst().orElseThrow();

            globalCharacter.setCareer(career);
            dropdownMenu.setValue(career);

            careerBoxChanged(socialClassField, statusField, levelField, dropdownMenu);
        });

        levelField = createField(4);
        levelField.setLabel("Level");
        levelField.setValue(globalCharacter.getLevel());
        levelField.setMin(1);
        levelField.setReadOnly(true);
        levelField.addValueChangeListener(e -> globalCharacter.setLevel(levelField.getValue()));

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
        div.add(headline, dropdownMenu, RandomCareerButton, socialClassField, statusField, levelField);
        return div;
    }

    private Div trappings(){
        Div TrappingsBox = new Div();

        VerticalLayout layout = new VerticalLayout();
        layout.setWidthFull();

        H1 headline = new H1("5. Add Starting Trappings");
        headline.getStyle().set("margin", "0 auto");

        Button addTrappingsButton = new Button("Add Trappings");
        addTrappingsButton.addClickListener(event -> {
            addTrappingFunction(levelField);
        });
        layout.add(headline, addTrappingsButton);
        TrappingsBox.add(layout);
        return TrappingsBox;
    }

    private Div raceBox(){
        Div raceBoxDiv = new Div();

        VerticalLayout layout = new VerticalLayout();
        layout.setWidthFull();

        H1 headline = new H1("1. Choose Species");
        headline.getStyle().set("margin", "0 auto");

        ComboBox<String> dropdownMenu = new ComboBox<>("Choose a species");
        dropdownMenu.setItems(raceItems.stream().map(Race::getRace).filter(Objects::nonNull).toList());

        Button button1 = new Button("Roll for Species", e -> {
            int roll = ThreadLocalRandom.current().nextInt(1, 101);
            System.out.println("Species roll: " + roll);

            Race speciesTable = raceRepository.findById("species_table").orElseThrow();

            String result = speciesTable.getEntries().stream()
                    .filter(entry -> roll >= entry.getMin() && roll <= entry.getMax())
                    .map(RaceEntry::getSpecies)
                    .findFirst()
                    .orElse("Unknown");

            dropdownMenu.setValue(result);
        });

        dropdownMenu.setValue(globalCharacter.getRace());
        raceBoxDiv.add(headline, dropdownMenu, button1);

        dropdownMenu.addValueChangeListener(e ->
                updateRace(dropdownMenu)
        );
        return raceBoxDiv;
    }

    private void updateRace(ComboBox<String> dropdownMenu) {
        String currentRace = dropdownMenu.getValue();

        String normalized = currentRace.trim();

        Race race = raceItems.stream()
                .filter(r -> r.getRace() != null
                        && r.getRace().trim().equalsIgnoreCase(normalized))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Race not found: " + currentRace
                ));
        globalCharacter.setRace(currentRace);
        currentRaceTable = race;

        Map<String, ArrayList<Integer>> diceRolls = race.getBasecharacteristicMap();

        raceFields.forEach((characteristicName, field) -> {

            ArrayList<Integer> values = diceRolls.get(characteristicName);

            if (values != null && !values.isEmpty()) {
                field.setValue(values.get(0));
            } else {
                field.setValue(0);
            }
        });
        startingSkillsBox.removeAll();
        startingSkillsBox.add(StartingSkillsAndTalents());
    }

    private void addTrappingFunction(IntegerField levelField){
        addTrappings(globalCharacter.getCareer(), levelField.getValue());

        inventoryDiv.removeAll();
        inventoryDivCreator();
    }


    private void careerBoxChanged(TextField socialClassField, TextField moneyField, IntegerField levelField, ComboBox<Career> dropdownMenu){
        levelField.setReadOnly(false);
        Career currentCareer = dropdownMenu.getValue();

        talentBox.removeAll();
        renderTalentsDivs(currentCareer, levelField.getValue());
        try {
            socialClassField.setValue(currentCareer.getSocialClass());

            if (levelField.getValue() == null) {
                levelField.setValue(1);
            }
            int level = levelField.getValue();

            List<Integer> status = currentCareer.getLevelStatusList();
            level = Math.min(level, status.size());
            moneyField.setValue("B" + status.get(level - 1));

            globalCharacter.setCareer(currentCareer);
            globalCharacter.setStatusLevel(status.get(level - 1));

            characteristicUpdates.values().forEach(Runnable::run);

        }catch (Exception e){
            System.out.println("Klassen eksisterer ikke");
        }
    }

    private Div renderSkillDivs(Characteristic characteristic){
        Div skillDivBox = new Div();

        if (globalCharacter.getCareer() != null) {
            List<List<String>> allowedSkills = new ArrayList<>(globalCharacter.getCareer().getLevelSkillsList()); //laver et hashset og chekker i loopet om skillen er i sættet
            skills.forEach(skill -> {
                boolean found = false;
                int maxLevel = Math.min(levelField.getValue(), allowedSkills.size());
                for (int i = 0; i < maxLevel; i++) {
                    if (allowedSkills.get(i).contains(skill.getName()) && characteristic.getName().equals(skill.getCharacteristic())) {
                        found = true;
                        System.out.println(skill.getName());
                        break;
                    }
                }
                if (!found) {
                    for (Skill characterSkill : globalCharacter.getSkills()) {
                        if (characterSkill.getName().equals(skill.getName())
                                && characterSkill.getSpeciesStartingBonus() != null
                                && characterSkill.getSpeciesStartingBonus() > 0
                                && characteristic.getName().equals(skill.getCharacteristic())) {
                            found = true;
                            break;
                        }
                    }
                }

                if (!found) {
                    return;
                }
                Div skillDiv = new Div();

                TextField charField = new TextField();
                charField.setReadOnly(true);
                charField.setLabel("Skill name");
                charField.setValue(skill.getName());
                charField.setWidth("200px");

                IntegerField raceField = new IntegerField();
                raceField.setReadOnly(true);
                raceField.setLabel("Species bonus");
                raceField.setValue(characteristic.getRacemod());

                TextField emptySpace = new TextField();
                emptySpace.setVisible(true);
                emptySpace.getStyle().set("visibility", "hidden");
                emptySpace.setWidth("120px");


                IntegerField baseField = createField(99);
                baseField.setLabel("Characteristic total");
                baseField.setValue(characteristic.getBase() + characteristic.getModifier() - characteristic.getPenalty() + characteristic.getRacemod() + raceFields.get(characteristic.getName()).getValue());
                baseField.setReadOnly(true);
                baseField.setWidth("150px");


                IntegerField modifierField = createField(99);
                int speciesBonus = skill.getSpeciesStartingBonus() != null ? skill.getSpeciesStartingBonus() : 0;
                int careerBonus = skill.getCareerStartingBonus() != null ? skill.getCareerStartingBonus() : 0;
                int manualBonus = skill.getBonusValue() != null ? skill.getBonusValue() : 0;
                modifierField.setLabel("Modifier");
                modifierField.setValue(speciesBonus + careerBonus + manualBonus);
                modifierField.setWidth("120px");

                IntegerField penaltyField = createField(99);
                penaltyField.setLabel("Penalty");
                penaltyField.setValue(skill.getPenaltyValue());
                penaltyField.setWidth("120px");

                IntegerField totalField = new IntegerField();
                totalField.setLabel("Total");
                totalField.setValue(0);
                totalField.setWidth("120px");

                Checkbox skillBoughtCheckbox = new Checkbox();
                skillBoughtCheckbox.setLabel("Is this skill bought?");


                Runnable update = () ->
                        updateSkill(baseField, modifierField, penaltyField, raceField, totalField, skill, skillBoughtCheckbox);

                baseField.addValueChangeListener(e -> update.run());
                modifierField.addValueChangeListener(e -> update.run());
                penaltyField.addValueChangeListener(e -> update.run());
                raceField.addValueChangeListener(e -> update.run());
                skillBoughtCheckbox.addValueChangeListener(e -> update.run());

                skillDiv.add(charField, baseField, emptySpace, modifierField, penaltyField, totalField /*,skillBoughtCheckbox*/);
                skillDiv.getStyle().set("border-top", "1px solid black");
                skillDivBox.add(skillDiv);
            });}
        return skillDivBox;
    }

    private void updateSkill(IntegerField baseField, IntegerField modifierField, IntegerField penaltyField, IntegerField raceField, IntegerField totalField, Skill skill, Checkbox skillBoughtCheckbox){
        int total = myParse(baseField)
                + myParse(modifierField)
                - myParse(penaltyField)
                + myParse(raceField);

        totalField.setValue(total);

        List<Skill> skillList = globalCharacter.getSkills();

        Skill existingSkill = null;

        for (Skill s : skillList) {
            if (s.getName().equals(skill.getName())) {
                existingSkill = s;
                break;
            }
        }

        if (existingSkill == null) {
            existingSkill = new Skill();
            skillList.add(existingSkill);
        }

        existingSkill.setName(skill.getName());
        existingSkill.setStartValue(baseField.getValue());
        existingSkill.setBonusValue(modifierField.getValue());
        existingSkill.setPenaltyValue(penaltyField.getValue());
        existingSkill.setBoughtBool(skillBoughtCheckbox.getValue());

        globalCharacter.setSkills(skillList);
    }

    private VerticalLayout renderCharacteristicsDivs(){
        Div characteristicsStatBox = new Div();
        characteristicsStatBox.getStyle().set("gap", "10px");

        VerticalLayout layout = new VerticalLayout();
        layout.setWidthFull();

        H1 headline = new H1("3. Determining Characteristics/Attributes");
        headline.getStyle().set("margin", "0 auto");

        int characteristicNumber = 0;
        boolean colorbool = true;

        for (Characteristic characterCharacteristic : globalCharacter.getCharacteristics()){
            System.out.println(characterCharacteristic.getBase());
            CharacteristicsDiv charDiv = new CharacteristicsDiv(characterCharacteristic.getName());

            if (colorbool) {
                charDiv.getStyle().set("border", "3px solid black");
            } else {
                charDiv.getStyle().set("border", "3px solid black");
            }

            colorbool = !colorbool;

            TextField charField = new TextField ();
            charField.setReadOnly(true);
            charField.setLabel("Characteristic name");
            charField.getStyle().set("--vaadin-input-field-label-font-weight", "bold");
            charField.setValue(characterCharacteristic.getName());
            charField.setWidth("200px");

            IntegerField raceField = new IntegerField ();
            raceField.setReadOnly(true);
            raceField.setWidth("150px");
            raceField.setLabel("Species bonus");
            raceField.setValue(0);
            raceFields.put(characterCharacteristic.getName(),raceField);

            IntegerField baseField = createField(99);
            baseField.setLabel("Rolled stat");
            baseField.setValue(globalCharacter.getCharacteristics().get(characteristicNumber).getBase());
            baseField.setWidth("120px");

            IntegerField modifierField = createField(99);
            modifierField.setLabel("Modifier");
            modifierField.setValue(globalCharacter.getCharacteristics().get(characteristicNumber).getModifier());
            modifierField.setWidth("120px");

            IntegerField penaltyField = createField(99);
            penaltyField.setLabel("Penalty");
            penaltyField.setValue(globalCharacter.getCharacteristics().get(characteristicNumber).getPenalty());
            penaltyField.setWidth("120px");

            IntegerField totalField = new IntegerField();
            totalField.setLabel("Total");
            totalField.setValue(0);
            totalField.setWidth("120px");

            Button rollButton = new Button("Roll charateristic");
            rollButton.addClickListener(event -> {
                int die1 = random.nextInt(10) + 1;
                int die2 = random.nextInt(10) + 1;
                baseField.setValue(die1+die2);
            });

            Div charSkillDiv = new Div();

            Runnable update = () ->
                    updateCharacteristic(baseField, modifierField, penaltyField, raceField, totalField, characterCharacteristic, charSkillDiv, charDiv);

            characteristicUpdates.put(characterCharacteristic.getName(), update);

            baseField.addValueChangeListener(e -> update.run());
            modifierField.addValueChangeListener(e -> update.run());
            penaltyField.addValueChangeListener(e -> update.run());
            raceField.addValueChangeListener(e -> update.run());


            ArrayList<IntegerField> fieldArray = new ArrayList<> (
                    List.of(
                            baseField,
                            modifierField,
                            penaltyField,
                            raceField
                    )
            );
            Map<String, ArrayList<IntegerField>> characteristicValues = new HashMap<>();
            characteristicValues.put(characterCharacteristic.getName(), fieldArray);

            charDiv.add(charField,raceField,baseField,modifierField,penaltyField,totalField,rollButton,charSkillDiv);
            characteristicsStatBox.add(charDiv);
            characteristicNumber++;
        }

        statBox.add(headline, characteristicsStatBox);
        layout.add(statBox);
        return layout;
    }

    private VerticalLayout StartingSkillsAndTalents(){
        startingBonusGroups.clear();

        VerticalLayout layout = new VerticalLayout();
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setPadding(false);

        H1 headline = new H1("4. Determining Starting Skills And Talents");
        headline.getStyle().set("margin", "0 auto");

        layout.add(headline);

        if (globalCharacter.getRace() == null) {
            return layout;
        }

        String race = globalCharacter.getRace().toLowerCase().replace(" ", "_") + "_starting_table";
        System.out.println(race);

        Race raceTable = raceRepository.findById(race).orElseThrow();
        List<String> allowedSkills = raceTable.getSkills();


        skills.forEach(skill -> {
            if (!allowedSkills.contains(skill.getName())) {
                return;
            }

            Div skillDiv = new Div();

            TextField charField = new TextField();
            charField.addClassName("skill-field");
            charField.setReadOnly(true);
            charField.setLabel("Skill name");
            charField.setValue(skill.getName());
            charField.setWidth("200px");

            TextField descriptionField = new TextField();
            descriptionField.addClassName("skill-field");
            descriptionField.setReadOnly(true);
            descriptionField.setLabel("Skill Description:");
            descriptionField.setValue(skill.getDescription());
            descriptionField.setWidth("639px");

            RadioButtonGroup<Integer> bonusGroup = new RadioButtonGroup<>();
            bonusGroup.setLabel("Starting bonus");
            bonusGroup.setItems(0, 3, 5);

            bonusGroup.setValue(skill.getSpeciesStartingBonus() != null ? skill.getSpeciesStartingBonus() : 0);
            startingBonusGroups.add(bonusGroup);
            bonusGroup.addValueChangeListener(event -> {

                Integer oldValue = event.getOldValue();
                Integer newValue = event.getValue();

                if (oldValue == null) oldValue = 0;
                if (newValue == null) newValue = 0;

                int threeCount = 0;
                int fiveCount = 0;

                for (RadioButtonGroup<Integer> group : startingBonusGroups) {
                    Integer value = group.getValue();
                    if (value == null) {value = 0;}
                    if (value == 3) {threeCount++;}
                    else if (value == 5) {fiveCount++;}}

                if (threeCount > MaxThree || fiveCount > MaxFive) {
                    bonusGroup.setValue(oldValue);
                    return;
                }
                skill.setSpeciesStartingBonus(newValue);
                boolean exists = false;

                for (Skill existingSkill : globalCharacter.getSkills()) {
                    if (existingSkill.getName().equals(skill.getName())) {
                        exists = true;
                        existingSkill.setSpeciesStartingBonus(newValue);
                        break;
                    }
                }

                if (!exists) {
                    globalCharacter.getSkills().add(skill);
                }

                characteristicUpdates.values().forEach(Runnable::run);
            });
            skillDiv.add(charField, descriptionField, bonusGroup);
            skillDiv.getStyle().set("border", "3px solid black");
            layout.add(skillDiv);
        });
        return layout;
    }

    private void updateCharacteristic(IntegerField baseField, IntegerField modifierField, IntegerField penaltyField, IntegerField raceField, IntegerField totalField, Characteristic characterCharacteristic, Div charSkillDiv, Div baseCharDiv){
        characterCharacteristic.setBase(baseField.getValue());
        characterCharacteristic.setModifier(modifierField.getValue());
        characterCharacteristic.setPenalty(penaltyField.getValue());
        characterCharacteristic.setRacemod(raceField.getValue());

        int total = myParse(baseField) + myParse(modifierField) - myParse(penaltyField) + myParse(raceField);

        totalField.setValue(total);

        charSkillDiv.removeAll();
        charSkillDiv.add(renderSkillDivs(characterCharacteristic));
        baseCharDiv.add(charSkillDiv);
    }

    private void renderTalentsDivs(Career career, int level){

        Div characterTalentDiv = new Div();
        List<List<String>> careerTalents = career.getLevelTalentsList();
        for (int talentIndex = 0; talentIndex < talents.size(); talentIndex++) {
            Talent talent = talents.get(talentIndex);
            boolean foundTalentInDatabase = false;
            int maxLevel = Math.min(level, careerTalents.size());

            for (int i = 0; i < maxLevel; i++) {
                List<String> currentCareerLevelTalents = careerTalents.get(i);

                if (currentCareerLevelTalents.contains(talent.getName())) {
                    foundTalentInDatabase = true;
                    break;
                }
            }
            if (!foundTalentInDatabase) {
                continue;
            }

            Div talentDiv = new Div();

            TextField talentField = new TextField();
            talentField.setReadOnly(true);
            talentField.setValue(talent.getName());

            IntegerField talentTakenField = getIntegerField(talent);

            talentDiv.add(talentField,talentTakenField);

            characterTalentDiv.add(talentDiv);

        }

        talentBox.add(characterTalentDiv);
    }
        //extracted method. skriv lige hvis i for lyst til at optimere den
    private @NonNull IntegerField getIntegerField(Talent talent) {
        IntegerField talentTakenField = new IntegerField();

        List<Talent> globalCharacterTalents = globalCharacter.getTalents();

        boolean foundInList = false;
        for (Talent t : globalCharacterTalents) {
            if (t.getName().equals(talent.getName())) {
                foundInList = true;
                talentTakenField.setValue(t.getAmountTaken());
                break;
            }
        }
        if (!foundInList) {
            talentTakenField.setValue(0);
        }

        talentTakenField.setMin(0);

        talentTakenField.addValueChangeListener(e ->
                updateTalents(e, talent)
        );
        return talentTakenField;
    }


    private void updateTalents(AbstractField.ComponentValueChangeEvent<IntegerField, Integer> e, Talent talent){
        int amountTakenValue = e.getValue();
        List<Talent> currentTalents = globalCharacter.getTalents();
        for (Talent t : currentTalents) {
            if (t.getName().equals(talent.getName())) {
                t.setAmountTaken(amountTakenValue);
                break;
            }
        }
        if (amountTakenValue > 0){
            if (!currentTalents.contains(talent.getName())) { //TODO JEG ER RET SIKKER PÅ AT DENNEHER KUN TRIGGER HVIS TALENTET ER PRÆCIS DET SAMME. DET BETYDER VI KAN LAVE FLERE VERSIONER AF DET SAMME TALENT
                currentTalents.add(talent);
                System.out.println("talent: " + talent.getName() + " is now at " + amountTakenValue);
            }
        }else{
            currentTalents.remove(talent);
        }
        globalCharacter.setTalents(currentTalents);
    }
}
