package com.P2.warhammer.views;

import com.P2.warhammer.Skills.Skill;
import com.P2.warhammer.Skills.SkillRepository;
import com.P2.warhammer.characters.Character;
import com.P2.warhammer.characters.CharacterRepository;
import com.P2.warhammer.characters.CharacterService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
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
import com.vaadin.flow.router.*;
import jakarta.annotation.security.PermitAll;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

            List<Integer> characteristics = character.getCharacteristics();
            String[] characteristicNames = {"Weapon Skill", "Ballistic Skill", "Strength", "Toughness", "Initiative", "Agility", "Dexterity", "Intelligence", "Willpower", "Fellowship"};



            for(int i = 0; i < characteristicNames.length; i++){
                characteristicsMap.put(characteristicNames[i], characteristics.get(i));
            }
            Div contentDiv = new Div();
            contentDiv.setClassName("content");


            Div characterInfo = new Div();
            characterInfo.setClassName("characterInfo");


            Div skillInfo = new Div();
            skillInfo.setClassName("skillInfo");




            characterInfo.add(
                    createInfoSection(),
                    createCareerSection(),
                    createPathSection(),
                    createBodySection()
            );


            contentDiv.add(characterInfo, skillInfo);

            skillInfo.add(createSkillGrid());
            add(contentDiv);




            /*

            Div body = new Div();
            body.setClassName("character_sheet");
            add(body);

            Div div  = new Div();
            div.getStyle().setWidth("100%").setHeight("100px");


            body.add(div);


            Div characteristics_body = new Div();
            characteristics_body.setClassName("characteristics_body");
            characteristicsMap.forEach((k,v) -> {
                Div characteristic_box_in_body = new Div();
                characteristic_box_in_body.setClassName("characteristics_box_in_body");
                characteristic_box_in_body.add(new Span(k));
                characteristic_box_in_body.add(new Span(v.toString()));;
                characteristics_body.add(characteristic_box_in_body);

            });

            body.add(characteristics_body);
            Div wounds_box_in_body = new Div();
            wounds_box_in_body.setClassName("wounds_box_in_body");
            wounds_box_in_body.add(new Span("Max wounds " + character.getMaxWounds()));

            IntegerField current_wounds = new IntegerField("Wounds");
            Binder<Character> wound_binder = new Binder<>(Character.class);

            wound_binder.forField(current_wounds)
                    .bind(Character::getDamageTaken, Character::setDamageTaken);
            wound_binder.readBean(character);

            current_wounds.setValueChangeMode(ValueChangeMode.LAZY);
            current_wounds.addValueChangeListener(change -> {
                if(change.getValue() == null) {
                    return;
                }
                character.setDamageTaken(change.getValue());
                    characterRepository.save(character);
              //  current_wounds.getB

            });
            wounds_box_in_body.add(current_wounds);
            body.add(wounds_box_in_body);





             */

            Map<Skill,List<Integer>> test = new HashMap<>();


        }


    private Div createInfoSection() {
        Div info = new Div();
        info.setClassName("info");
        info.getStyle().setBackground("green");

        info.add(
                createLabeledDiv("Name:" + character.getName(), "DivName"),
                createLabeledDiv("Species:" + character.getRace(), "DivSpecies"),
                createLabeledDiv("Class:", "DivClass")
        );
        return info;
    }

    private Div createCareerSection() {
        Div careerInfo = new Div();
        careerInfo.setClassName("info");
        careerInfo.getStyle().setBackground("yellow");

        careerInfo.add(
                createLabeledDiv("Career:", "DivCareer"),
                createLabeledDiv("Career Level:", "DivCareerLevel")
        );
        return careerInfo;
    }
    private Div createPathSection() {
        Div careers = new Div();
        careers.setClassName("info"); // Keeps consistency with your other containers
        careers.getStyle().setBackground("blue");

        // Adding the specific fields
        careers.add(
                createLabeledDiv("Path:", "DivCareerPath"),
                createLabeledDiv("Status:", "DivCareerStatus")
        );

        return careers;
    }

    private Div createBodySection() {
        Div body = new Div();
        body.setClassName("info");
        body.getStyle().setBackground("pink");

        body.add(
                createLabeledDiv("Age:", "DivAge"),
                createLabeledDiv("Height:", "DivHeight"),
                createLabeledDiv("Hair:", "DivHair"),
                createLabeledDiv("Eyes:", "DivEyes")
        );
        return body;
    }

    private Div createLabeledDiv(String text, String className) {
        Div container = new Div(new Span(text));
        container.setClassName(className);
        return container;
    }



    private Grid<String> createSkillGrid() {
        // We change the Grid type to String because the "Row" is now a Characteristic name
        Grid<String> grid = new Grid<>(String.class, false);
        grid.setClassName("skillGrid");
        // Add the toggle/dropdown column
        grid.addColumn(createToggleDetailsRenderer(grid)).setWidth("60px").setFlexGrow(0);


        // Add the Characteristic name column
        grid.addColumn(characteristicName -> characteristicName + ": " + characteristicsMap.get(characteristicName))
                .setHeader("Characteristic Group");

        // Set the Details Renderer to our new grouping layout
        grid.setItemDetailsRenderer(new ComponentRenderer<>(
                CharacteristicDetailsLayout::new,
                CharacteristicDetailsLayout::setCharacteristic
        ));

        // DATA LOGIC: Get unique characteristics from the character's skill list
        List<String> activeCharacteristics = character.getSkills().stream()
                .map(Skill::getCharacteristic)
                .distinct()
                .sorted()
                .toList();

        grid.setItems(activeCharacteristics);
        return grid;
    }

    private Renderer<String> createToggleDetailsRenderer(
            Grid<String> grid) {

        return LitRenderer
                .<String> of("""
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

            // Configure the inner grid columns
            innerGrid.addColumn(Skill::getName).setHeader("Skill Name").setAutoWidth(true);
            innerGrid.addColumn(Skill::getCategory).setHeader("Category").setAutoWidth(true);
            innerGrid.addColumn(Skill::getStartValue).setHeader("S").setAutoWidth(true);
            innerGrid.addColumn(Skill::getBonusValue).setHeader("B").setAutoWidth(true);
            innerGrid.addColumn(Skill::getTotalValue).setHeader("T").setAutoWidth(true);


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

                Span badge = new Span(skill.getCategory());
                badge.getElement().getThemeList().add("badge contrast small");

                HorizontalLayout header = new HorizontalLayout(heading, badge);
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

        public void setCharacteristic(String characteristicName) {
            // Filter the main character's skill list
            List<Skill> filteredSkills = character.getSkills().stream()
                    .filter(skill -> skill.getCharacteristic().equalsIgnoreCase(characteristicName))
                    .toList();

            innerGrid.setItems(filteredSkills);
        }
    }


}
