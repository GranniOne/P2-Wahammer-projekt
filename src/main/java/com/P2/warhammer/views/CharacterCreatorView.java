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
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
// 1. Race roll table gives error when tolling 99 and 100 (typical elves)
// 2. Buying and removing skills does not remove from database
// 3. Save Character currently only saves in database but does not give player access
// 4. Skills only show up after selecting career meaning any roll before selecting career needs to be rerolled
// 5. clicking on character creator in the navigator bar, duplicates the site instead of reloading it
// 6. Characteristic total does not include species

@PermitAll
@PageTitle("Character Creator Page")
@Route("characterCreator")
@StyleSheet("css/characterStyle.css")
public class CharacterCreatorView extends Div implements HasUrlParameter<String> {
    private final UserRepository userRepository;
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


        private final Random random = new Random();
        private List<String> currentSkillList;

        List<Characteristic> characteristics;

         */
    Map<String, ArrayList<IntegerField>> characteristicValues = new HashMap<>();

    Random random = new Random();
    private final SkillRepository skillRepository;
    private final CharacterRepository characterRepository;
    private final CharacterService characterService;
    private final CareerRepository careerRepository;
    private final TalentRepository talentRepository;
    private final RaceRepository raceRepository;
    private List<Career> careers;
    private List<Skill> skills;
    private List<Talent> talents;
    private List<Race> raceItems;
    private Character globalCharacter;
    Div talentBox = new Div();
    Div statBox = new Div();
    Div inventoryDiv = new Div();
    IntegerField levelField;
    List<IntegerField> raceFields = new ArrayList<>();


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
        this.raceItems = raceRepository.findAll();
        this.skills = skillRepository.findAll();
        this.talents = talentRepository.findAll();

        renderCharacteristicsDivs();
        inventoryDivCreator();

        container.add(raceBox(), careerBox(), statBox, talentBox, inventoryDiv);


        add(container);
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
        boolean colorbool = true;
        inventoryDiv.getStyle().set("background-color", "green");
        for (WarhammerItem inventoryItem : globalCharacter.getInventory()){
             Div inventoryElementDiv = new Div();
             TextField itemField = new TextField();
             itemField.setValue(inventoryItem.getName());
             itemField.setReadOnly(true);

             IntegerField itemAmount = new IntegerField();
            itemAmount.setValue(inventoryItem.getAmount());

             if (colorbool) {
                 inventoryElementDiv.getStyle().set("background-color", "#88CF8F");
             } else {
                 inventoryElementDiv.getStyle().set("background-color", "#CF89A0");
             }

            inventoryElementDiv.add(itemField);
            inventoryElementDiv.add(itemAmount);

            colorbool = !colorbool;

            inventoryDiv.add(inventoryElementDiv);
        }


    }

    private void saveCharacter(){
        this.characterService.addCharacter(globalCharacter);
    }

    private Div careerBox(){
        Div div = new Div();



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


        TextField nameField = new TextField();
        nameField.setLabel("Character name");
        try {
            nameField.setValue(globalCharacter.getName());
        } catch (NullPointerException e) {
        }
        nameField.addValueChangeListener(e -> globalCharacter.setName(nameField.getValue()));


        IntegerField xpField = new IntegerField();
        xpField.setLabel("XP");
        try {
            xpField.setValue(globalCharacter.getExperience());
        } catch (NullPointerException e) {
        }
        xpField.addValueChangeListener(e -> globalCharacter.setExperience(xpField.getValue()));


        IntegerField ageField = new IntegerField();
        ageField.setLabel("Age");
        try {
            ageField.setValue(globalCharacter.getAge());
        } catch (NullPointerException e) {
        }
        ageField.addValueChangeListener(e -> globalCharacter.setAge(ageField.getValue()));

        Button saveCharacterButton = new Button("Save Character", e -> {
            saveCharacter();
        });

        ComboBox<Career> dropdownMenu = new ComboBox<>("Choose a career");

        try {
            dropdownMenu.setValue(globalCharacter.getCareer());
        } catch (NullPointerException e) {
        }
        dropdownMenu.setItems(careers);
        dropdownMenu.setItemLabelGenerator(Career::getName);

        div.add(dropdownMenu);
        levelField = createField( 4);
        levelField.setLabel("Level");
        levelField.setValue(globalCharacter.getLevel());
        levelField.setMin(1);
        levelField.setReadOnly(true);
        levelField.addValueChangeListener(e -> globalCharacter.setLevel(levelField.getValue()));


        Button addTrappingsButton = new Button("Add Trappings");
        addTrappingsButton.addClickListener(event -> {
            addTrappingFunction(levelField);
        });

        div.add(levelField,socialClassField,statusField,addTrappingsButton,nameField,xpField,ageField,saveCharacterButton);


        if (!dropdownMenu.isEmpty()){
            levelField.setReadOnly(false);
        }

        nameField.addValueChangeListener(e ->
                globalCharacter.setName(nameField.getValue())
        );

        //adds listener so the skills talents and characteristics can change when another career or level is selected
        levelField.addValueChangeListener(e ->
                careerBoxChanged(socialClassField, statusField, levelField, dropdownMenu)
        );
        dropdownMenu.addValueChangeListener(e ->
                careerBoxChanged(socialClassField, statusField, levelField, dropdownMenu)
        );

        return div;
    }

    private Div raceBox(){
        Div raceBoxDiv = new Div();

        ComboBox<String> dropdownMenu = new ComboBox<>("Choose a species");
        dropdownMenu.setItems(raceItems.stream().map(Race::getRace).filter(Objects::nonNull).toList());

        Button button1 = new Button("Roll for Species", e -> {
            int roll = ThreadLocalRandom.current().nextInt(1, 101);
            System.out.println(roll);

            Race raceTable = raceRepository.findById("species_table").orElseThrow();

            String result = raceTable.getEntries().stream()
                    .filter(entry -> roll >= entry.getMin() && roll <= entry.getMax())
                    .map(RaceEntry::getSpecies)
                    .findFirst()
                    .orElse("Unknown");

            dropdownMenu.setValue(result);
        });


        dropdownMenu.setValue(globalCharacter.getRace());
        raceBoxDiv.add(dropdownMenu);
        raceBoxDiv.add(button1);


        //change race
        dropdownMenu.addValueChangeListener(e ->
                updateRace(dropdownMenu)
        );

        return raceBoxDiv;
    }

    private void updateRace(ComboBox<String> dropdownMenu) {
        String currentRace = dropdownMenu.getValue();

        Race race = raceItems.stream()
                .filter(r -> currentRace.equals(r.getRace()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Race not found"));
        globalCharacter.setRace(currentRace);

        Map<String, ArrayList<Integer>> diceRolls = race.getBasecharacteristicMap();
        ArrayList<Integer> allValues = new ArrayList<>();

        for (ArrayList<Integer> list : diceRolls.values()) {
            allValues.addAll(list);
        }

        for (int i = 0; i < 10; i++){
            raceFields.get(i).setValue(allValues.get(i));
        }


    }

    private void addTrappingFunction(IntegerField levelField){
        addTrappings(globalCharacter.getCareer(), levelField.getValue());
        this.remove(inventoryDiv);
        inventoryDiv.removeAll();
        inventoryDivCreator();
        this.add(inventoryDiv);
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
            moneyField.setValue("B" + status.get(level - 1));

            globalCharacter.setCareer(currentCareer);
            globalCharacter.setStatusLevel(status.get(level - 1));

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
                for (int i = 0; i < levelField.getValue(); i++) {
                    if (allowedSkills.get(i).contains(skill.getName()) && characteristic.getName().equals(skill.getCharacteristic())) {
                        found = true;
                        System.out.println(skill.getName());
                        break;
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
                charField.setWidth("160px");

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
                baseField.setValue(characteristic.getBase() + characteristic.getModifier() - characteristic.getPenalty());
                baseField.setReadOnly(true);
                baseField.setWidth("150px");

                IntegerField modifierField = createField(99);
                modifierField.setLabel("Modifier");
                modifierField.setValue(skill.getBonusValue());
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

                skillDiv.add(charField, baseField, emptySpace, modifierField, penaltyField, totalField, skillBoughtCheckbox);
                skillDivBox.add(skillDiv);
            });}
            return skillDivBox;

    }

    private void updateSkill(IntegerField baseField, IntegerField modifierField, IntegerField penaltyField, IntegerField raceField, IntegerField totalField, Skill skill, Checkbox skillBoughtCheckbox){
        int total = myParse(baseField) + myParse(modifierField) - myParse(penaltyField) + myParse(raceField);

        totalField.setValue(total);
        globalCharacter.getSkills().remove(skill);
        Skill newSkill = new Skill();
        newSkill.setName(skill.getName());
        newSkill.setStartValue(baseField.getValue());
        newSkill.setBonusValue(modifierField.getValue());
        newSkill.setPenaltyValue(penaltyField.getValue());
        newSkill.setBoughtBool(skillBoughtCheckbox.getValue());
        List<Skill> newSkillList = globalCharacter.getSkills();
        if (!newSkillList.contains(skill.getName())) {//TODO JEG ER RET SIKKER PÅ AT DENNEHER KUN TRIGGER HVIS SKILLET ER PRÆCIS DET SAMME. DET BETYDER VI KAN LAVE FLERE VERSIONER AF DET SAMME SKILLET
            newSkillList.add(newSkill);
            globalCharacter.setSkills(newSkillList);
        }
    }

    private void renderCharacteristicsDivs(){
        Div characteristicsStatBox = new Div();
        characteristicsStatBox.getStyle().set("gap", "10px");
        int characteristicNumber = 0;
        boolean colorbool = true;

        for (Characteristic characterCharacteristic : globalCharacter.getCharacteristics()){
            System.out.println(characterCharacteristic.getBase());
            CharacteristicsDiv charDiv = new CharacteristicsDiv(characterCharacteristic.getName());

            if (colorbool) {
                charDiv.getStyle().set("background-color", "#91BAB2");
            } else {
                charDiv.getStyle().set("background-color", "#F5A3BE");
            }

            colorbool = !colorbool;

            TextField charField = new TextField ();
            charField.setReadOnly(true);
            charField.setLabel("Characteristic name");
            charField.setValue(characterCharacteristic.getName());
            charField.setWidth("160px");

            IntegerField raceField = new IntegerField ();
            raceField.setReadOnly(true);
            raceField.setWidth("150px");
            raceField.setLabel("Species bonus");
            raceField.setValue(0);
            raceFields.add(raceField);

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

        statBox.add(characteristicsStatBox);
    }

    private void updateCharacteristic(IntegerField baseField, IntegerField modifierField, IntegerField penaltyField, IntegerField raceField, IntegerField totalField, Characteristic characterCharacteristic, Div charSkillDiv, Div baseCharDiv){
        int total = myParse(baseField) + myParse(modifierField) - myParse(penaltyField) + myParse(raceField);

        totalField.setValue(total);

        characterCharacteristic.setBase(baseField.getValue());
        characterCharacteristic.setModifier(modifierField.getValue());
        characterCharacteristic.setPenalty(penaltyField.getValue());

        charSkillDiv.removeAll();
        charSkillDiv.add(renderSkillDivs(characterCharacteristic));
        baseCharDiv.add(charSkillDiv);
    }

    private void renderTalentsDivs(Career career, int level){

        Div characterTalentDiv = new Div();
        List<List<String>> careerTalents = career.getLevelTalentsList();
        boolean colorbool = true;
        for (int talentIndex = 0; talentIndex < talents.size(); talentIndex++) {
            Talent talent = talents.get(talentIndex);
            boolean foundTalentInDatabase = false;
            for (int i = 0; i < level; i++) {
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

            if (colorbool) {
                talentDiv.getStyle().set("background-color", "#91BAB2");
            } else {
                talentDiv.getStyle().set("background-color", "#F5A3BE");
            }
            colorbool = !colorbool;

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

*/



    private Map<String, Characteristic> characterHashmapCharacteristics(){

        Map<String, Characteristic> characterHashmapCharacteristics = new HashMap<>();

        for (Map.Entry<String, ArrayList<IntegerField>> characteristicElement : characteristicValues.entrySet()) {
            String key = characteristicElement.getKey();
            ArrayList<IntegerField> values = characteristicElement.getValue();
            int baseValue = values.get(0).getValue();
            int modifierValue = values.get(1).getValue();
            int penaltyValue = values.get(2).getValue();
            int racemodifier = values.get(3).getValue();

            Characteristic characteristic = new Characteristic(key, baseValue, modifierValue, penaltyValue, racemodifier);

            characterHashmapCharacteristics.put(key, characteristic);

        }
        return characterHashmapCharacteristics;
    }


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
                racefield
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
