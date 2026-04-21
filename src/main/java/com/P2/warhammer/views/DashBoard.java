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

        campaigns.forEach((campaign) -> {
            System.out.println(campaign.getName());
        });
        setClassName("dashboard");
        Div dashboard = new Div();

        Div characterContent = new Div();
        Span CharacterTitle = new Span("Characters");
        Div CharacterCards = new Div();

        ownedCharacters.forEach(character -> {
            Card card = new Card();
            card.setTitle(character.getName());
            card.getElement().addEventListener("click", event -> {
                System.out.println(character.getName());
            });
            CharacterCards.add(card);
        });



        Div CampaignContent = new Div();
        Div CampaignField = new Div();
        Span CampaignTitleSpan = new Span("Campaigns");
        Div CampaignCards = new Div();

        campaigns.forEach(campaign -> {
            Card card = new Card();
            card.setTitle(Objects.equals(loadedUser.getId(), campaign.getGameMaster().getId()) ? "Gamemaster: " + campaign.getName() : "player: " +  campaign.getName());
            card.getElement().addEventListener("click", event -> {
                UI.getCurrent().navigate(CampaignView.class,QueryParameters.of("Campaign", campaign.getId()));

            });
            CampaignCards.add(card);
        });

        dashboard.setClassName("dashboardContent");
        characterContent.setClassName("characterContent");
        CampaignContent.setClassName("CampaignContent");
        CharacterTitle.setClassName("CharacterTitle");
        CampaignField.setClassName("CampaignField");
        CampaignTitleSpan.setClassName("CampaignTitle");
        CampaignCards.setClassName("CampaignCards");
        CharacterCards.setClassName("CharacterCards");

        CampaignField.add(CampaignTitleSpan,new Button("Add Campaign", event -> {
            UI.getCurrent().navigate(CampaignCreatorView.class);
        }));

        characterContent.add(CharacterTitle,CharacterCards);
        CampaignContent.add(CampaignField,CampaignCards);

        dashboard.add(characterContent,CampaignContent);
        add(dashboard);





    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("continue")){


            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                System.out.println("BOobies");
            }

                    System.out.println("S");
        }

    }
}
