package com.P2.warhammer.views;

import com.P2.warhammer.Skills.Skill;
import com.P2.warhammer.Skills.SkillRepository;
import com.P2.warhammer.campaigns.Campaign;
import com.P2.warhammer.campaigns.CampaignService;
import com.P2.warhammer.characteristics.Characteristic;
import com.P2.warhammer.characters.Character;
import com.P2.warhammer.characters.CharacterService;
import com.P2.warhammer.layout.CharacterCard;
import com.P2.warhammer.users.User;
import com.P2.warhammer.users.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.RolesAllowed;

import java.util.*;

@PageTitle("User Page")
@RolesAllowed("ROLE_ADMIN")
@Route("admin-dashboard/UserProfile/:userID")
public class AdminProfileView extends HorizontalLayout implements BeforeEnterObserver {
    final UserService userService;
    final CharacterService characterService;
    final CampaignService campaignService;


    private Grid<Character> characterGrid;
    private Grid<Campaign> campaignGrid;

    private List<Character> characters = new ArrayList<>();
    private List<Campaign> campaigns = new ArrayList<>();

    public AdminProfileView(UserService userService, CharacterService characterService, CampaignService  campaignService) {
        this.userService = userService;
        this.characterService = characterService;
        this.campaignService = campaignService;

        System.out.println("first");

    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        String userID = beforeEnterEvent.getRouteParameters().get("userID").get();

        try{
            characters = new ArrayList<>(
                    characterService.getCharactersByUser(userService.findUserById(userID))
            );
        } catch (Exception e) {
            characters = new ArrayList<>();
        }

        try{
            campaigns = new ArrayList<>(
                    campaignService.findByGameMaster(userService.findUserById(userID))
            );
        } catch (Exception e) {
            campaigns = new ArrayList<>();
        }



        // Character grid
        characterGrid = new Grid<>(Character.class, false);
        // Campaign grid
        campaignGrid = new Grid<>(Campaign.class, false);


        characterGrid.setItems(characters);
        campaignGrid.setItems(campaigns);





        characterGrid.addColumn(Character::getId)
                .setHeader("Character ID");

        characterGrid.addColumn(Character::getName)
                .setHeader("Character Name");


        characterGrid.addComponentColumn(character ->
                new Button("X", event -> {
                    characterService.deleteCharacterFromId(character.getId());
                    characters.remove(character);
                    characterGrid.getDataProvider().refreshAll();
                })
        );




        campaignGrid.addColumn(Campaign::getId)
                .setHeader("Campaign ID");

        campaignGrid.addColumn(Campaign::getName)
                .setHeader("Campaign Name");

        campaignGrid.addComponentColumn(campaign ->
                new Button("X", event -> {
                    campaignService.deleteCampaignById(campaign.getId());
                    campaigns.remove(campaign);
                    campaignGrid.getDataProvider().refreshAll();
                })
        );




        setSizeFull();

        characterGrid.setSizeFull();
        campaignGrid.setSizeFull();
        this.add(characterGrid, campaignGrid);




    }
}
