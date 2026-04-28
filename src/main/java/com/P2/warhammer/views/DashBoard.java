package com.P2.warhammer.views;



import com.P2.warhammer.campaigns.Campaign;
import com.P2.warhammer.campaigns.CampaignService;
import com.P2.warhammer.characters.Character;
import com.P2.warhammer.characters.CharacterService;
import com.P2.warhammer.users.User;
import com.P2.warhammer.users.UserService;
import com.P2.warhammer.utilities.Utilities;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Objects;

@StyleSheet("css/dashboardPage.css")
@PageTitle("dashboard Page")
@Route("")
@PermitAll
public class DashBoard extends Div implements BeforeEnterObserver {
    private final UserService userService;
    User loadedUser;
    List<Character> ownedCharacters;
    List<Campaign> campaigns;
    DashBoard(UserService userService, CharacterService  characterService, CampaignService  campaignService) {
        this.userService = userService;
        try{
            loadedUser = Utilities.getUserFromAuthentication();
            ownedCharacters =  characterService.getCharactersByUser(loadedUser);
            campaigns = campaignService.getCampaignsByPlayerAndGameMaster(loadedUser,loadedUser);
        }catch (Exception e){
            e.printStackTrace();
        }


        setClassName("dashboard");
        Div dashboard = new Div();

        Div characterContent = new Div();
        Div CharacterField = new Div();
        Span CharacterTitleSpan = new Span("Characters");
        Div CharacterCards = new Div();
        try {
            ownedCharacters.forEach(character -> {
                Card card = new Card();
                card.setTitle(character.getName());
                card.getElement().addEventListener("click", event -> {
                    UI.getCurrent().navigate(CharacterView.class, QueryParameters.of("Character", character.getId()));
                });
                CharacterCards.add(card);
            });
        }catch (Exception e){}




        Div CampaignContent = new Div();
        Div CampaignField = new Div();
        Span CampaignTitleSpan = new Span("Campaigns");
        Div CampaignCards = new Div();
        try {
            campaigns.forEach(campaign -> {
                Card card = new Card();
                System.out.println(campaign.toString());
                card.setTitle(Objects.equals(loadedUser.getId(), campaign.getGameMaster().getId()) ? "Gamemaster: " + campaign.getName() : "player: " +  campaign.getName());
                card.getElement().addEventListener("click", event -> {
                    UI.getCurrent().navigate(CampaignView.class,QueryParameters.of("Campaign", campaign.getId()));

                });
                CampaignCards.add(card);
            });

        }catch (Exception e){

        }


        dashboard.setClassName("dashboardContent");
        characterContent.setClassName("characterContent");
        CampaignContent.setClassName("CampaignContent");
        CampaignField.setClassName("Field");
        CharacterField.setClassName("Field");

        CharacterTitleSpan.setClassName("CharacterTitle");
        CampaignTitleSpan.setClassName("CampaignTitle");
        CampaignCards.setClassName("CampaignCards");
        CharacterCards.setClassName("CharacterCards");

        Button addCampaignButton =  new Button("Add Campaign",buttonClickEvent -> {
            UI.getCurrent().navigate(CampaignCreatorView.class);
        });

        Button addCharacterButton =  new Button("Add Character",buttonClickEvent -> {
            UI.getCurrent().navigate(CharacterCreatorView.class);
        });

        addCampaignButton.getStyle().setMarginLeft("20px").setBackground("black");
        addCharacterButton.getStyle().setMarginLeft("20px").setBackground("black");

        CampaignField.add(CampaignTitleSpan,addCampaignButton);
        CharacterField.add(CharacterTitleSpan,addCharacterButton);

        characterContent.add(CharacterField,CharacterCards);
        CampaignContent.add(CampaignField,CampaignCards);



        dashboard.add(characterContent,CampaignContent);
        add(dashboard);





    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("continue")){

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {



            }

                  ;
        }

    }
}
