package com.P2.warhammer.views;

import com.P2.warhammer.Skills.Skill;
import com.P2.warhammer.Skills.SkillService;
import com.P2.warhammer.careers.Career;
import com.P2.warhammer.careers.CareerRepository;
import com.P2.warhammer.characteristics.Characteristic;
import com.P2.warhammer.characters.Character;
import com.P2.warhammer.characters.CharacterRepository;
import com.P2.warhammer.characters.CharacterService;
import com.P2.warhammer.users.UserRepository;
import com.P2.warhammer.Race.RaceRepository;
import com.P2.warhammer.Talents.TalentRepository;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.PermitAll;

import java.util.*;
import java.util.stream.Stream;

@PermitAll
@PageTitle("Character Creator Page")
@Route("characterCreator")
@StyleSheet("css/characterStyle.css")
public class CharacterCreatorView extends Div implements HasUrlParameter<String> {

    private final SkillService skillService;
    private final CharacterRepository characterRepository;
    private final CareerRepository careerRepository;

    private Character currentCharacter;
    private Career currentCareer;
    private List<Skill> sessionSkills = new ArrayList<>();
    private List<Characteristic> sessionCharacteristics = new ArrayList<>();
    private final Set<String> allowedSkills = new HashSet<>();
    private final Map<String, List<Binder<Skill>>> characteristicToSkillBinders = new HashMap<>();

    private static final List<String> CHAR_NAMES = List.of(
            "Weapon Skill", "Ballistic Skill", "Strength", "Toughness",
            "Initiative", "Agility", "Dexterity", "Intelligence",
            "Willpower", "Fellowship"
    );

    public CharacterCreatorView(SkillService skillService, CharacterRepository characterRepository,
                                CareerRepository careerRepository, RaceRepository raceRepository,
                                TalentRepository talentRepository, CharacterService characterService,
                                UserRepository userRepository) {
        this.skillService = skillService;
        this.characterRepository = characterRepository;
        this.careerRepository = careerRepository;

        setClassName("div-page");
        getStyle().set("position", "relative");
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String Id) {
        String id = beforeEvent.getLocation().getQueryParameters().getSingleParameter("Character").orElse("");
        if (currentCharacter == null) {
            currentCharacter = characterRepository.findById(id).orElse(new Character());

            List<Career> careers = careerRepository.findAll();
            if (!careers.isEmpty()) {
                this.currentCareer = careers.get(0);
            }

            if (currentCharacter.getSkills().isEmpty()) {
                currentCharacter.setSkills(new ArrayList<>(skillService.getAllSkills()));
            }
            if (currentCharacter.getCharacteristics().isEmpty()) {
                currentCharacter.setCharacteristics(new ArrayList<>(initializeCharacteristics()));
            }

            this.sessionSkills = currentCharacter.getSkills();
            this.sessionCharacteristics = currentCharacter.getCharacteristics();

            // Initial calculation of allowed skills based on starting level
            updateAllowedSkills(currentCharacter.getLevel() != null ? currentCharacter.getLevel() : 1);
        }

        VerticalLayout container = new VerticalLayout();
        container.setSizeFull();

        // Level Field to dictate skill visibility
        IntegerField levelField = new IntegerField("Career Level");
        levelField.setMin(1);
        levelField.setMax(4);
        levelField.setValue(currentCharacter.getLevel() != null ? currentCharacter.getLevel() : 1);
        levelField.setStepButtonsVisible(true);

        Grid<Characteristic> charGrid = new Grid<>(Characteristic.class, false);
        charGrid.setAllRowsVisible(true);
        charGrid.addColumn(Characteristic::getName).setHeader("Characteristic").setFlexGrow(1);

        levelField.addValueChangeListener(e -> {
            currentCharacter.setLevel(e.getValue());
            updateAllowedSkills(e.getValue());
            charGrid.getDataProvider().refreshAll(); // Refresh grid to trigger re-filtering of details
        });

        charGrid.addComponentColumn(stat -> new CharacteristicEditor(stat, charGrid))
                .setHeader("Base | Adv | Mod | Total");

        charGrid.setItemDetailsRenderer(new ComponentRenderer<>(stat -> {
            characteristicToSkillBinders.put(stat.getName(), new ArrayList<>());

            Grid<Skill> skillGrid = new Grid<>(Skill.class, false);
            skillGrid.setAllRowsVisible(true);
            skillGrid.addColumn(Skill::getName).setHeader("Skill Name");

            skillGrid.addComponentColumn(skill -> {
                skill.setStartValue(stat.getTotalValue());
                SkillEditor editor = new SkillEditor(skill);
                characteristicToSkillBinders.get(stat.getName()).add(editor.getBinder());
                return editor;
            }).setHeader("Bought | Start | Bonus | Pen | Total");

            List<Skill> filtered = sessionSkills.stream()
                    .filter(s -> s.getCharacteristic().equalsIgnoreCase(stat.getName()))
                    .filter(s -> {
                        // Logic: Show if in Career list for current/previous levels OR if explicitly owned
                        boolean isCareerSkill = allowedSkills.contains(s.getName());
                        boolean hasStats = (s.getBonusValue() != null && s.getBonusValue() > 0);
                        boolean isBought = (s.getBoughtBool() != null && s.getBoughtBool());

                        return isCareerSkill || hasStats || isBought;
                    })
                    .toList();

            skillGrid.setItems(filtered);
            return new VerticalLayout(skillGrid);
        }));

        charGrid.setItems(sessionCharacteristics);

        Button debugButton = new Button("Print Character State", e -> printCurrentState());
        container.add(levelField, debugButton, charGrid);
        add(container);
    }

    private void updateAllowedSkills(int level) {
        allowedSkills.clear();
        if (currentCareer != null && currentCareer.getLevelSkillsList() != null) {
            // Flatten lists up to the current level (e.g., Level 2 includes Level 1 skills)
            for (int i = 0; i < level && i < currentCareer.getLevelSkillsList().size(); i++) {
                allowedSkills.addAll(currentCareer.getLevelSkillsList().get(i));
            }
        }
    }

    private void printCurrentState() {
        System.out.println("======= CHARACTER DEBUG LOG =======");
        System.out.println("Name: " + currentCharacter.getName() + " | Level: " + currentCharacter.getLevel());
        System.out.println("--- Characteristics ---");
        sessionCharacteristics.forEach(c -> System.out.printf("Name: %-15s | Total: %d%n", c.getName(), c.getTotalValue()));

        System.out.println("\n--- Skills (Current Allowed/Owned) ---");
        sessionSkills.stream()
                .filter(s -> allowedSkills.contains(s.getName()) || (s.getBoughtBool() != null && s.getBoughtBool()))
                .forEach(s -> System.out.printf("Skill: %-15s | Total: %d (B: %d P: %d)%n",
                        s.getName(), s.getTotalValue(), s.getBonusValue(), s.getPenaltyValue()));
        System.out.println("====================================");
    }

    private void updateAssociatedSkillBinders(String charName, Integer newTotal) {
        sessionSkills.stream()
                .filter(s -> s.getCharacteristic().equalsIgnoreCase(charName))
                .forEach(s -> s.setStartValue(newTotal));

        List<Binder<Skill>> binders = characteristicToSkillBinders.get(charName);
        if (binders != null) {
            binders.forEach(b -> b.readBean(b.getBean()));
        }
    }

    private List<Characteristic> initializeCharacteristics() {
        return CHAR_NAMES.stream().map(name -> {
            Characteristic c = new Characteristic();
            c.setName(name);
            c.setBase(0); c.setRacemod(0); c.setModifier(0);
            return c;
        }).toList();
    }

    private class SkillEditor extends HorizontalLayout {
        private final Checkbox bought = new Checkbox();
        private final IntegerField start = new IntegerField();
        private final IntegerField bonus = new IntegerField();
        private final IntegerField penalty = new IntegerField();
        private final IntegerField total = new IntegerField();
        private final Binder<Skill> binder = new Binder<>(Skill.class);
        private final Skill skill;

        public SkillEditor(Skill skill) {
            this.skill = skill;

            if (skill.getBonusValue() == null) skill.setBonusValue(0);
            if (skill.getPenaltyValue() == null) skill.setPenaltyValue(0);
            if (skill.getStartValue() == null) skill.setStartValue(0);

            setSpacing(true);
            setAlignItems(Alignment.CENTER);
            Stream.of(start, bonus, penalty, total).forEach(f -> f.setWidth("75px"));
            total.setReadOnly(true);

            binder.forField(bought).bind(Skill::getBoughtBool, Skill::setBoughtBool);
            binder.forField(start).bind(Skill::getStartValue, Skill::setStartValue);
            binder.forField(bonus).withNullRepresentation(0).bind(Skill::getBonusValue, Skill::setBonusValue);
            binder.forField(penalty).withNullRepresentation(0).bind(Skill::getPenaltyValue, Skill::setPenaltyValue);

            binder.setBean(skill);

            // 1. Define the listener using the most generic ValueChangeEvent
            HasValue.ValueChangeListener<HasValue.ValueChangeEvent<?>> updateTotal = e -> {
                total.setValue(skill.getTotalValue());
            };

// 2. Use a "Wildcard" stream to treat them all as basic value-bearing components
            Stream.<HasValue<?, ?>>of(start, bonus, penalty, bought)
                    .forEach(f -> f.addValueChangeListener(updateTotal));
            total.setValue(skill.getTotalValue());
            add(bought, start, bonus, penalty, total);
        }

        public Binder<Skill> getBinder() { return binder; }
    }

    private class CharacteristicEditor extends HorizontalLayout {
        private final Binder<Characteristic> binder = new Binder<>(Characteristic.class);

        public CharacteristicEditor(Characteristic stat, Grid<Characteristic> parentGrid) {
            setSpacing(true);
            IntegerField base = new IntegerField();
            IntegerField adv = new IntegerField();
            IntegerField mod = new IntegerField();
            IntegerField totalField = new IntegerField();

            Stream.of(base, adv, mod, totalField).forEach(f -> f.setWidth("70px"));
            totalField.setReadOnly(true);

            binder.forField(base).withNullRepresentation(0).bind(Characteristic::getBase, Characteristic::setBase);
            binder.forField(adv).withNullRepresentation(0).bind(Characteristic::getRacemod, Characteristic::setRacemod);
            binder.forField(mod).withNullRepresentation(0).bind(Characteristic::getModifier, Characteristic::setModifier);

            binder.setBean(stat);

            HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<?, ?>> updateAll = e -> {
                totalField.setValue(stat.getTotalValue());
                updateAssociatedSkillBinders(stat.getName(), stat.getTotalValue());
                parentGrid.getDataProvider().refreshItem(stat);
            };

            Stream.of(base, adv, mod).forEach(f -> f.addValueChangeListener(updateAll));

            totalField.setValue(stat.getTotalValue());
            add(base, adv, mod, totalField);
        }
    }
}