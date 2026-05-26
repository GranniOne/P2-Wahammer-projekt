package com.P2.warhammer.views;

import com.P2.warhammer.Skills.Skill;
import com.P2.warhammer.Skills.SkillRepository;
import com.P2.warhammer.Talents.Talent;
import com.P2.warhammer.characteristics.Characteristic;
import com.P2.warhammer.characters.Character;
import com.P2.warhammer.characters.CharacterRepository;
import com.P2.warhammer.characters.CharacterService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverPosition;
import com.vaadin.flow.component.popover.PopoverVariant;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.PermitAll;

import java.util.*;
import java.util.stream.Stream;

@PermitAll
@Route("character")
@StyleSheet("css/charactersheet.css")
public class CharacterView extends Div implements HasUrlParameter<String> {
    private final CharacterService characterService;
    private final CharacterRepository characterRepository;
    private Character character;
    Map<String,Integer> characteristicsMap = new HashMap<>();

    CharacterView(CharacterService  characterService, CharacterRepository characterRepository){
        this.characterService = characterService;
        this.characterRepository = characterRepository;
        this.setClassName("characterPage");


        // Layout

    }

    @Override
        public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String characterId) {
            this.character = characterService.getCharacterFromId(beforeEvent.getLocation().getQueryParameters().getParameters("Character").getFirst());

            //List<Integer> characteristics = character.getCharacteristics();
            //String[] characteristicNames = {"Weapon Skill", "Ballistic Skill", "Strength", "Toughness", "Initiative", "Agility", "Dexterity", "Intelligence", "Willpower", "Fellowship"};



            //for(int i = 0; i < characteristicNames.length; i++){
            //    characteristicsMap.put(characteristicNames[i], characteristics.get(i));
            //}

            Div page = new Div();
            page.setClassName("page");

            Div contentDiv = new Div();
            contentDiv.setClassName("content");

            Image banner = new Image("../images/test7.png", "Warhammer Fantasy Roleplay");
            banner.getStyle().setWidth("55%").setHeight("auto").set("object-fit", "contain");
            banner.setClassName("image");


            Div characterInfo = new Div();
            characterInfo.setClassName("characterInfo");


            Div skillInfo = new Div();
            skillInfo.setClassName("skillInfo");

            characterInfo.add(
                    createInfoSection(),
                    createCareerSection(),
                    createPathSection(),
                    createBodySection(),
                    createFateSection(),
                    MovementWealthArmorDiv(),
                    createTalentSection()
            );

            page.add(banner,contentDiv);
            contentDiv.add(characterInfo, skillInfo);

            skillInfo.add(createSkillGrid());
            add(page);


            Map<Skill,List<Integer>> test = new HashMap<>();


        }

    private Div createTalentSection() {
        Div talentBox = new Div();
        talentBox.setClassName("TalentBox");

        H3 talentHeadline = new H3("Talents");

        Grid<Talent> grid = new Grid<>(Talent.class, false);
        grid.addClassName("Talent");
        grid.setItems(character.getTalents());

        grid.addColumn(Talent::getName)
                .setHeader("Name")
                .setAutoWidth(true);

        grid.addColumn(Talent::getAmountTaken)
                .setHeader("Amount")
                .setAutoWidth(true);

        grid.addColumn(Talent::getDescription)
                .setHeader("Description")
                .setFlexGrow(1);



        grid.addThemeVariants(GridVariant.LUMO_COMPACT);
        grid.setAllRowsVisible(true);

        talentBox.add(talentHeadline, grid);

        return talentBox;
    }

    private Div createInfoSection() {
        Div info = new Div();
        info.setClassName("info");


        info.add(
                createLabeledDiv("Name:" + character.getName(), "DivName"),
                createLabeledDiv("Species:" + character.getRace(), "DivSpecies"),
                createLabeledDiv("Class:" + character.getCareer().getSocialClass(), "DivClass")
        );
        return info;
    }

    private Div createCareerSection() {
        Div careerInfo = new Div();
        careerInfo.setClassName("info");
        careerInfo.getStyle().set("display", "flex");
        careerInfo.getStyle().set("flex-wrap", "wrap");
        careerInfo.getStyle().set("gap", "10px");

        careerInfo.add(
                createLabeledDiv("Career: " + character.getCareer().getName(), "DivCareer"),
                createLabeledDiv("Career Level: " + character.getLevel(), "DivCareerLevel")
        );
        return careerInfo;
    }
    private Div createPathSection() {
        Div careers = new Div();
        careers.setClassName("info"); // Keeps consistency with your other containers

        careers.getStyle().set("display", "flex");
        careers.getStyle().set("flex-wrap", "wrap");
        careers.getStyle().set("gap", "10px");

        // Adding the specific fields
        careers.add(
                createLabeledDiv("Path: " + character.getCareer().getName(), "DivCareerPath"),
                createLabeledDiv("Status: " + character.getCareer().getLevelStatusList().get(character.getLevel()), "DivCareerStatus")
        );

        return careers;
    }


    private Div createBodySection() {
        Div body = new Div();
        body.setClassName("info");

        body.getStyle().set("display", "flex");
        body.getStyle().set("flex-wrap", "wrap");
        body.getStyle().set("gap", "10px");

        body.add(
                createLabeledDiv("Age: " + character.getAge(), "DivAge"),
                createLabeledDiv("Height: " + character.getHeight() , "DivHeight"),
                createLabeledDiv("Hair: " + character.getHair(), "DivHair"),
                createLabeledDiv("Eyes: " + character.getEyes(), "DivEyes")
        );
        return body;
    }
    private Div createLabeledDiv(String text, String className) {
        Div container = new Div(new Span(text));
        container.setClassName(className);
        return container;
    }



    private Div createFateSection(){
        Div div  = new Div();
        div.setClassName("FateResilienceExp");
        Div FateResilience = new Div();
        Div Fate = new Div();
        Div Resilience = new Div();
        Div Exp = new Div();

        FateResilience.setClassName("FXR");
        Fate.setClassName("Fate");
        Resilience.setClassName("Resilience");
        Exp.setClassName("Exp");


        Fate.add(createLabeledDiv("Fate: ", "TitleSpan"));
        Fate.add(createLabeledDiv("Fate: " + character.getFate(), "DivFate"));
        Fate.add(createLabeledDiv("Fortune: " + character.getFortune(), "DivFortune"));


        Div DivResilienceAndResolve = new Div();
        DivResilienceAndResolve.getStyle().set("display","flex").setFlexDirection(Style.FlexDirection.ROW);



        DivResilienceAndResolve.add(createLabeledDiv("Resilience: " + character.getResilience(), "ResilienceResolve"), createLabeledDiv("Resolve" + character.getResolve(), "ResilienceResolve"));

        Resilience.add(createLabeledDiv("Resilience", "TitleSpan"));
        Resilience.add(DivResilienceAndResolve);
        Resilience.add(createLabeledDiv("Motivation: " + character.getMotivation(), "DivMotivation"));



        Exp.add((createLabeledDiv("Exp: " + character.getExperience(), "DivExp")));

        FateResilience.add(Resilience, Fate);
        div.add(FateResilience, Exp);

        return div;
    }



    private Div MovementWealthArmorDiv(){
        Div div = new Div();
        div.setClassName("MovementWealthArmor");
        Div armour = new Div();
        armour.setClassName("armor");
        String[] locations = {
                "Head",
                "Chest",
                "Left arm",
                "Right arm",
                "Left leg",
                "Right leg"
        };

        if (character.getArmourValues() != null && character.getArmourValues().size() >= 6) {
            for (int i = 0; i < 6; i++) {
                int value = character.getArmourValues().get(i);
                Div armourRow = new Div("Armour at " + locations[i] + ": " + value);
                armour.add(armourRow);
            }
        }

        Div hpAndCorruption = new Div();
        Div maxHealth = new Div("Max wounds: " + character.getMaxWounds());
        Div currentHealth = new Div("Current wounds: " + character.getDamageTaken());

        Div maxCorruption = new Div("Max corruption: " + character.getCorruptionMax());
        Div currentCorruption = new Div("Current corruption: " + character.getCorruptionTaken());

        hpAndCorruption.add(maxHealth,currentHealth,maxCorruption,currentCorruption);

        armour.add(hpAndCorruption);

        Div MovementWealth = new Div();
        MovementWealth.setClassName("movementwealth");

        Div Movement = new Div();
        Movement.setClassName("movement");
        Movement.add(createLabeledDiv("Movement: " + character.getMovement(), "DivMovement"));

        Div Wealth = new Div();
        Wealth.setClassName("wealth");
        Wealth.add(createLabeledDiv("Wealth: " + character.getPfennings() + " Pfennings", "DivWealth"));

        MovementWealth.add(Movement, Wealth);

        div.add(MovementWealth,armour);

        return div;
    }


    private Grid<Characteristic> createSkillGrid() {
        // We change the Grid type to String because the "Row" is now a Characteristic name
        Grid<Characteristic> grid = new Grid<>(Characteristic.class, false);
        grid.setClassName("skillGrid");
        // Add the toggle/dropdown column
        grid.addColumn(createToggleDetailsRenderer(grid)).setWidth("60px").setFlexGrow(0);


        grid.addComponentColumn(characteristic -> {
            // 1. Stat Name (e.g., Strength)
            Span name = new Span(characteristic.getName());
            name.getStyle()
                    .set("font-weight", "bold")
                    .set("width", "120px");

            // 2. The Stat breakdown (Base, Modifier, Penalty)
            Span base = new Span(String.valueOf(characteristic.getBase()));
            base.getStyle().set("color", "var(--lumo-secondary-text-color)").set("width", "30px");

            Span mod = new Span("+" + characteristic.getModifier());
            mod.getStyle().set("color", "var(--lumo-success-text-color)").set("width", "30px");

            Span pen = new Span("-" + characteristic.getPenalty());
            pen.getStyle().set("color", "var(--lumo-error-text-color)").set("width", "30px");

            // 3. Total Target Number
            int totalVal = characteristic.getBase() + characteristic.getModifier() - characteristic.getPenalty();
            Span total = new Span(String.valueOf(totalVal));
            total.getStyle()
                    .set("margin-left", "auto") // Pushes total to the far right of the component
                    .set("font-weight", "bold")
                    .set("font-size", "var(--lumo-font-size-l)")
                    .set("color", "var(--lumo-primary-text-color)");

            // 4. Create random number roller

            Div randomButtonBox = new Div();
            Button randomButton = new Button("X");
            randomButton.addClickListener(event -> {
                int randomValue = new Random().nextInt(100)+1;

                boolean rolledOverTotal = randomValue >= totalVal;

                String result = rolledOverTotal ? "Dice rolled is " + randomValue + " (over " + totalVal + ")" : "Dice rolled is " +randomValue + " (under " +totalVal + ")";

                Notification rollNotification = Notification.show(result);

                if (rolledOverTotal) {
                    rollNotification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                } else {
                    rollNotification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                }
            });


            randomButtonBox.add(randomButton);


            // Assemble the "Row"
            HorizontalLayout row = new HorizontalLayout(name, base, mod, pen, total, randomButton);
            row.setAlignItems(FlexComponent.Alignment.CENTER);
            row.setWidthFull();
            row.setPadding(false);
            row.setSpacing(true);

            return row;
        }).setHeader("Characteristic Stat Block").setFlexGrow(1);

        // Set the Details Renderer to our new grouping layout
        grid.setItemDetailsRenderer(new ComponentRenderer<>(
                CharacteristicDetailsLayout::new,
                CharacteristicDetailsLayout::setCharacteristic
        ));

        // DATA LOGIC: Get unique characteristics from the character's skill list
        List<Characteristic> activeCharacteristics = character.getCharacteristics().stream()
                .distinct()
                .toList();

        grid.setItems(activeCharacteristics);
        return grid;
    }

    private Renderer<Characteristic> createToggleDetailsRenderer(
            Grid<Characteristic> grid) {

        return LitRenderer
                .<Characteristic> of("""
                    <vaadin-button
                        theme="tertiary icon"
                        aria-label="Toggle details"
                        aria-expanded="${model.detailsOpened ? 'true' : 'false'}"
                        @click="${handleClick}"
                    >
                        <vaadin-icon
                        .icon="${model.detailsOpened ? 'vaadin:angle-down' : 'vaadin:angle-right'}"
                        ></vaadin-icon>
                    </vaadin-button>
                """)
                .withFunction("handleClick",
                        person -> grid.setDetailsVisible(person,
                                !grid.isDetailsVisible(person)));
    }

    private class CharacteristicDetailsLayout extends VerticalLayout {
        private final Grid<Skill> innerGrid = new Grid<>(Skill.class, false);

        public CharacteristicDetailsLayout() {
            setPadding(true);
            setSpacing(false);
            this.getStyle().set("display", "contents");

            // Configure the inner grid columns
            innerGrid.addColumn(Skill::getName).setHeader("Skill Name").setWidth("145px");
            //innerGrid.addColumn(Skill::getCategory).setHeader("Category").setAutoWidth(true);

            innerGrid.addColumn(Skill::getStartValue)
                    .setHeader("S")
                    .setWidth("45px")
                    .setFlexGrow(0)
                    .setTextAlign(ColumnTextAlign.CENTER);


            innerGrid.addComponentColumn(skill -> {
                Span span = new Span("+" + skill.getBonusValue());
                span.getStyle().set("color", "var(--lumo-success-text-color)");
                return span;
            }).setHeader("B").setWidth("45px").setFlexGrow(0);

            innerGrid.addComponentColumn(skill -> {
                Span span = new Span("-" + skill.getPenaltyValue());
                span.getStyle().set("color", "var(--lumo-error-text-color)");
                return span;
            }).setHeader("P").setWidth("45px").setFlexGrow(0);

            innerGrid.addComponentColumn(skill -> {
                Span span = new Span(String.valueOf(skill.getTotalValue()));
                span.getStyle().set("font-weight", "bold");
                return span;
            }).setHeader("T").setWidth("45px").setFlexGrow(0);

            // Create random number roller
            innerGrid.addComponentColumn(skill -> {
                Div randomButtonBox = new Div();
                Button randomButton = new Button("X");
                randomButton.addClickListener(event -> {
                    int randomValue = new Random().nextInt(100)+1;

                    boolean rolledOverTotal = randomValue >= skill.getTotalValue();

                    String result = rolledOverTotal ? "Dice rolled is " + randomValue + " (over " + skill.getTotalValue() + ")" : "Dice rolled is " +randomValue + " (under " +skill.getTotalValue() + ")";

                    Notification rollNotification = Notification.show(result);

                    if (rolledOverTotal) {
                        rollNotification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    } else {
                        rollNotification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    }
                });


                randomButtonBox.add(randomButton);
                return  randomButtonBox;
            });


            // Make the inner grid look clean
            innerGrid.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_NO_BORDER);
            innerGrid.setAllRowsVisible(true); // Ensures the grid expands to fit all skills

            innerGrid.addItemClickListener(skillItemClickEvent -> {
                Skill skill = skillItemClickEvent.getItem();

                Popover popover = new Popover();

                popover.setTarget(skillItemClickEvent.getSource());
                popover.setWidth("300px");
                popover.addThemeVariants(PopoverVariant.LUMO_ARROW, PopoverVariant.LUMO_NO_PADDING);
                popover.setPosition(PopoverPosition.BOTTOM);


                popover.addOpenedChangeListener(event -> {
                    if (!event.isOpened()) {
                        popover.getElement().removeFromParent();
                    }
                });

                // 1. Header (Name and Category Badge)
                H4 heading = new H4(skill.getName());
                heading.getStyle().set("margin", "0").set("font-size", "var(--lumo-font-size-m)");

                //Span badge = new Span(skill.getCategory());
                //badge.getElement().getThemeList().add("badge contrast small");

                HorizontalLayout header = new HorizontalLayout(heading);
                header.setAlignItems(FlexComponent.Alignment.CENTER);
                header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
                header.getStyle()
                        .set("padding", "var(--lumo-space-m)")
                        .set("border-bottom", "1px solid var(--lumo-contrast-10pct)");

                // 2. Body (Description)
                Div body = new Div();
                body.getStyle()
                        .set("padding", "var(--lumo-space-m)")
                        .set("font-size", "var(--lumo-font-size-s)")
                        .set("color", "var(--lumo-secondary-text-color)");
                body.setText(skill.getDescription() != null ? skill.getDescription() : "No description provided.");

                // 3. Footer (Characteristic)
                Div footer = new Div(new Span("Stat: " + skill.getCharacteristic()));
                footer.getStyle()
                        .set("padding", "var(--lumo-space-s) var(--lumo-space-m)")
                        .set("background-color", "var(--lumo-contrast-5pct)")
                        .set("font-size", "var(--lumo-font-size-xs)")
                        .set("font-weight", "bold");

                // Add everything to popover
                popover.add(header, body, footer);

                // Add to the view so it can be rendered, then open it
                add(popover);
                popover.setOpened(true);
            });
            add(innerGrid);
        }

        public void setCharacteristic(Characteristic characteristic) {
            if (characteristic == null) return;

            // Filter the main character's skill list based on the characteristic name
            List<Skill> filteredSkills = character.getSkills().stream()
                    .filter(skill -> skill.getCharacteristic().equalsIgnoreCase(characteristic.getName()))
                    .toList();

            innerGrid.setItems(filteredSkills);
        }
    }


}
