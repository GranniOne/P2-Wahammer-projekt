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
// 2. updating characteristic removes all stats from corresponding skills


@PermitAll
@PageTitle("Character Creator Page")
@Route("characterCreator")
@StyleSheet("css/characterStyle.css")
public class CharacterCreatorView extends Div implements HasUrlParameter<String> {
    private final UserRepository userRepository;
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
    private boolean loadedCharacter = false;

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
                    loadedCharacter = true;
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

        if (loadedCharacter) {
            updateRaceFromLoadedRace(globalCharacter.getRace());
        }
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
                        "helping you build a complete and playable character, through a guided introduction to the character creator. Just remember this is a tool for character " +
                        "creation and storage and should not be used as an alternative to the rules book." +
                        " As with every roleplaying game, each group likes to play things a little different so its recommended to go through your choices with you game master" +
                        " to make sure everything is in order."
        );
        Paragraph paragraph2 = new Paragraph("In some sections of the character creator, you can either choose an option or accept the result of a " +
                "dice roll to make the choice for you. You may receive bonus Experience Points (XP) for choosing to accept random outcomes, as if the " +
                "Dark Gods of Chaos themselves applaud your acceptance of random chance. XP represent learning from experience and are the principal" +
                "way to improve your abilities — you will be able to spend these points to enhance your character’s abilities. " +
                "The XP gain for each choice can be seen in the corresponding section or in the core rule book under character creation, " +
                "but has to be manually added to the XP field at the bottom af the character creator");

        layout.add(headline, paragraph1, paragraph2);
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
                WarhammerItem addedItem = new WarhammerItem(item.trim(), 1);
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


    private VerticalLayout careerBox(){
        VerticalLayout layout = new VerticalLayout();
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setPadding(false);

        Div div = new Div();

        H1 headline = new H1("2. Choose Career");
        headline.getStyle().set("margin", "0 auto");

        Paragraph paragraph = new Paragraph(
                "Your Class determines your general place in society. " +
                "Your Career describes your current job and determines your Status, which also influences how much money you earn. " +
                "You can simply choose your Class and Career from the options below, choose your option from the dropdown menu and move on" +
                "to step 3. Alternatively, if you are unsure which to choose, or just want to randomly select for bonus XP: ");

        Paragraph paragraph1 = new Paragraph(
                "1. Roll 1d100 on the Random Class and Career Table. If you" +
                " don’t like the result, move to step 2. If you keep the result, gain +50 XP.");
        Paragraph paragraph2 = new Paragraph(
                "2. Roll twice more on the table, bringing your total to 3 choices." +
                " If one of the three now suits you, select one and gain +25 XP. If not, move to Step 3.");
        Paragraph paragraph3 = new Paragraph("3. Choose your Class and Career, or keep rerolling on the table" +
                " until you get something you like. There is no XP bonus for this.");

        layout.add(headline, paragraph,paragraph1, paragraph2, paragraph3);

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
        levelField.addValueChangeListener(e -> globalCharacter.setLevel(levelField.getValue()-1));

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
        div.add(dropdownMenu, RandomCareerButton, socialClassField, statusField, levelField);
        layout.add(div);
        if (loadedCharacter){
            careerBoxChanged(socialClassField, statusField, levelField, dropdownMenu);
        }
        return layout;
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

        raceFields.forEach((characteristicname, field) ->{
            System.out.println(characteristicname);
        } );

        raceFields.forEach((characteristicName, field) -> {

            ArrayList<Integer> value = diceRolls.get(characteristicName);

            if (value != null && !value.isEmpty()) {
                System.out.println(characteristicName + " " + value.get(0));
                field.setValue(value.get(0));

                for (Characteristic characteristic : globalCharacter.getCharacteristics()){
                    if (characteristic.getName().equals(characteristicName)){
                        characteristic.setRacemod(value.get(0));

                    }
                }

            } else {
                field.setValue(0);
                System.out.println("idk it breaks");
            }
        });
        startingSkillsBox.removeAll();
        startingSkillsBox.add(StartingSkillsAndTalents());
    }

    private void updateRaceFromLoadedRace(String currentRace) {
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

        raceFields.forEach((characteristicname, field) ->{
            System.out.println(characteristicname);
        } );

        raceFields.forEach((characteristicName, field) -> {

            ArrayList<Integer> value = diceRolls.get(characteristicName);

            if (value != null && !value.isEmpty()) {
                System.out.println(characteristicName + " " + value.get(0));
                field.setValue(value.get(0));

                for (Characteristic characteristic : globalCharacter.getCharacteristics()){
                    if (characteristic.getName().equals("Weapons Skill")){
                        System.out.println("weaponslkills");
                        System.out.println(characteristicName);
                    }
                    if (characteristic.getName().equals(characteristicName)){
                        characteristic.setRacemod(value.get(0));
                        System.out.println("works here");
                        System.out.println("blank");
                    }
                }

            } else {
                field.setValue(0);
                System.out.println("idk it breaks");
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
            System.out.println("noget gik galt");
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
                penaltyField.setValue(skill.getPenaltyValue() != null ? skill.getPenaltyValue() : 0);
                penaltyField.setWidth("120px");

                IntegerField totalField = new IntegerField();
                totalField.setLabel("Total");
                totalField.setValue(0);
                totalField.setWidth("120px");

                Checkbox skillBoughtCheckbox = new Checkbox();
                skillBoughtCheckbox.setLabel("Bought");
                boolean skillBought = false;
                for (Skill characterSkill : globalCharacter.getSkills()) {
                    if (characterSkill.getName().equals(skill.getName())) {
                        skillBought = characterSkill.getBoughtBool() != null && characterSkill.getBoughtBool();
                        break;
                    }
                }
                skillBoughtCheckbox.setValue(skillBought);


                Runnable update = () ->
                        updateSkill(baseField, modifierField, penaltyField, raceField, totalField, skill, skillBoughtCheckbox);

                baseField.addValueChangeListener(e -> update.run());
                modifierField.addValueChangeListener(e -> update.run());
                penaltyField.addValueChangeListener(e -> update.run());
                raceField.addValueChangeListener(e -> update.run());
                skillBoughtCheckbox.addValueChangeListener(e -> update.run());

                skillDiv.add(charField, baseField, emptySpace, modifierField, penaltyField, totalField, skillBoughtCheckbox);
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
        existingSkill.setCharacteristic(skill.getCharacteristic());
        existingSkill.setStartValue(baseField.getValue());
        existingSkill.setBonusValue(modifierField.getValue());
        existingSkill.setPenaltyValue(penaltyField.getValue());
        existingSkill.setBoughtBool(skillBoughtCheckbox.getValue());
        System.out.println(skillBoughtCheckbox.getValue());

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

        Paragraph paragraph = new Paragraph("When you create your character you are given skills, talents and advancements in those, as a one time bonus. " +
                "Below will have a walkthrough af what bonuses your character get and what bonuses you can choose for you character, since not " +
                "every bonus is pre-determined, causing every character to be unique. Just remember that these are one-time bonuses and should not be changed later. " +
                "If the fields below are empty, you have skipped a step. Make sure you've selected a species and race and then come back");

        H1 headline1 = new H1("4.1. Species Skill Bonus");
        headline1.getStyle().set("margin", "0 auto");

        Paragraph paragraph1 = new Paragraph("Each Species has a variety of Skills and Talents to choose from.\n" +
                "You may choose 3 Skills to gain 5 Advances each, and 3 Skills\n" +
                "to gain 3 Advances each. They are automatically added to your skill table above, and the modifier value will be changed according to your choices.");

        layout.add(headline, paragraph, headline1, paragraph1);

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

            TextField charField1 = new TextField();
            charField1.addClassName("skill-field");
            charField1.setReadOnly(true);
            charField1.setLabel("Skill name");
            charField1.setValue(skill.getName());
            charField1.setWidth("200px");

            TextField descriptionField1 = new TextField();
            descriptionField1.addClassName("skill-field");
            descriptionField1.setReadOnly(true);
            descriptionField1.setLabel("Skill Description:");
            descriptionField1.setValue(skill.getDescription());
            descriptionField1.setWidth("639px");

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
            skillDiv.add(charField1, descriptionField1, bonusGroup);
            skillDiv.getStyle().set("border", "3px solid black");
            layout.add(skillDiv);
        });
        H1 headline2 = new H1("4.2 Species Starting Talents");
        headline2.getStyle().set("margin", "0 auto");
        Paragraph paragraph3 = new Paragraph("Your Species grants you the following talents :");

        layout.add(headline2, paragraph3);

        List<String> allowedTalents = raceTable.getTalents().stream().filter(t -> "auto".equals(t.get("source"))).map(t -> (String) t.get("Name")).toList();
        System.out.println(allowedTalents);
        Set<String> allowedSet = new HashSet<>(allowedTalents);

        for (Talent templateTalent : talents) {
            if (!allowedSet.contains(templateTalent.getName())) {
                continue;
            }
            boolean exists = globalCharacter.getTalents().stream().anyMatch(t -> t.getName().equals(templateTalent.getName()));
            if (!exists) {
                Talent copy = new Talent();
                copy.setName(templateTalent.getName());
                copy.setDescription(templateTalent.getDescription());
                copy.setAmountTaken(1);

                globalCharacter.getTalents().add(copy);
            }
        }

        talents.forEach(talent -> {
            if (!allowedSet.contains(talent.getName())) {
                return;
            }
            Div talentDiv = new Div();

            TextField charField2 = new TextField();
            charField2.addClassName("skill-field");
            charField2.setReadOnly(true);
            charField2.setLabel("Talent name");
            charField2.setValue(talent.getName());
            charField2.setWidth("200px");

            TextField descriptionField2 = new TextField();
            descriptionField2.addClassName("skill-field");
            descriptionField2.setReadOnly(true);
            descriptionField2.setLabel("Talent Description:");
            descriptionField2.setValue(talent.getDescription());
            descriptionField2.setWidth("779px");

            talentDiv.add(charField2, descriptionField2);
            talentDiv.getStyle().set("border", "3px solid black");
            layout.add(talentDiv);
        });

        Paragraph paragraph4 = new Paragraph("Your Species lets you choose between the following talent(s) :");
        layout.add(paragraph4);




        Paragraph paragraph5 = new Paragraph("Your Species grants you " + "INSERT NUMBER HERE" +" Random talents from the following table :");  //insert number here xd

        H1 headline3 = new H1("4.3 Career Skills And Talents");
        headline3.getStyle().set("margin", "0 auto");

        layout.add(headline3);
        return layout;
    }

    private void updateCharacteristic(IntegerField baseField, IntegerField modifierField, IntegerField penaltyField, IntegerField raceField, IntegerField totalField, Characteristic characterCharacteristic, Div charSkillDiv, Div baseCharDiv){
        characterCharacteristic.setBase(baseField.getValue());
        characterCharacteristic.setModifier(modifierField.getValue());
        characterCharacteristic.setPenalty(penaltyField.getValue());


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

    private @NonNull IntegerField getIntegerField(Talent talent) {
        IntegerField talentTakenField = new IntegerField();

        List<Talent> globalCharacterTalents = globalCharacter.getTalents();

        Talent existing = globalCharacterTalents.stream().filter(t -> t.getName().equals(talent.getName())).findFirst().orElse(null);
        talentTakenField.setValue(existing != null ? existing.getAmountTaken() : 0);
        talentTakenField.setMin(0);

        talentTakenField.addValueChangeListener(e ->
                updateTalents(e, talent)
        );
        return talentTakenField;
    }


    private void updateTalents(AbstractField.ComponentValueChangeEvent<IntegerField, Integer> e, Talent talent) {
        int amountTakenValue = e.getValue();
        List<Talent> currentTalents = globalCharacter.getTalents();
        for (Talent t : currentTalents) {
            if (t.getName().equals(talent.getName())) {
                t.setAmountTaken(amountTakenValue);
                break;
            }
        }
        if (amountTakenValue > 0) {
            boolean exists = currentTalents.stream().anyMatch(t -> t.getName().equals(talent.getName()));
            if (!exists) {
                Talent copy = new Talent();
                copy.setName(talent.getName());
                copy.setDescription(talent.getDescription());
                copy.setAmountTaken(amountTakenValue);

                currentTalents.add(copy);
            }
            System.out.println("talent: " + talent.getName() + " is now at " + amountTakenValue);
        } else {
            currentTalents.removeIf(t -> t.getName().equals(talent.getName()));
        }
    }
}
