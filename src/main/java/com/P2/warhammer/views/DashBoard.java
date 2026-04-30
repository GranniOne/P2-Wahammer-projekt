package com.P2.warhammer.views;



import com.P2.warhammer.campaigns.Campaign;
import com.P2.warhammer.campaigns.CampaignService;
import com.P2.warhammer.characters.Character;
import com.P2.warhammer.characters.CharacterRepository;
import com.P2.warhammer.characters.CharacterService;
import com.P2.warhammer.users.User;
import com.P2.warhammer.users.UserService;
import com.P2.warhammer.utilities.Utilities;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
    private final CharacterService characterService;
    private final CampaignService campaignService;

    User loadedUser;
    List<Character> ownedCharacters;
    List<Campaign> campaigns;
    DashBoard(UserService userService, CharacterService  characterService, CampaignService  campaignService) {
        this.characterService =  characterService;
        this.userService = userService;
        this.campaignService = campaignService;
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
                HorizontalLayout characterCard = new HorizontalLayout();
                characterCard.setAlignItems(FlexComponent.Alignment.CENTER);
                characterCard.addClassName("character-card");
                characterCard.setPadding(true);

                characterCard.add(new H5(character.getName()));

                Button characterViewButton = new Button("View", e -> {
                    UI.getCurrent().navigate(CharacterView.class, QueryParameters.of("Character", character.getId()));

                });
                characterCard.addToEnd(characterViewButton);

                Button characterEditButton = new Button("Edit", e ->{
                    UI.getCurrent().navigate(CharacterCreatorView.class, QueryParameters.of("Character", character.getId()));


                });
                characterCard.addToEnd(characterEditButton);

                Button deleteCharacterButton = new Button("Delete", e -> {
                    openDialog(character);
                });
                characterCard.addToEnd(deleteCharacterButton);




                CharacterCards.add(characterCard);
            });
        }catch (Exception e){}




        Div CampaignContent = new Div();
        Div CampaignField = new Div();
        Span CampaignTitleSpan = new Span("Campaigns");
        Div CampaignCards = new Div();
        try {
            campaigns.forEach(campaign -> {
                HorizontalLayout campaignCard = new HorizontalLayout();
                campaignCard.setAlignItems(FlexComponent.Alignment.CENTER);
                campaignCard.addClassName("campaign-card");
                campaignCard.setPadding(true);

                campaignCard.add(new H5(Objects.equals(loadedUser.getId(), campaign.getGameMaster().getId()) ? "Gamemaster: " + campaign.getName() : "Player: " + campaign.getName()));

                Button campaignViewButton = new Button("View", e -> {
                    UI.getCurrent().navigate(CampaignView.class,QueryParameters.of("Campaign", campaign.getId()));
                });
                campaignCard.addToEnd(campaignViewButton);
                if(campaign.getGameMaster().getId().equals(loadedUser.getId())){
                    Button campaignEditButton = new Button("Edit", e ->{
                        UI.getCurrent().navigate(CampaignCreatorView.class, QueryParameters.of("Campaign", campaign.getId()));
                    });
                    campaignCard.addToEnd(campaignEditButton);

                    Button deleteCharacterButton = new Button("Delete", e -> {
                        openDialog(campaign);
                    });
                    campaignCard.addToEnd(deleteCharacterButton);
                }







                /*Card card = new Card();
                System.out.println(campaign.toString());
                card.setTitle(Objects.equals(loadedUser.getId(), campaign.getGameMaster().getId()) ? "Gamemaster: " + campaign.getName() : "player: " +  campaign.getName());
                card.getElement().addEventListener("click", event -> {
                    UI.getCurrent().navigate(CampaignView.class,QueryParameters.of("Campaign", campaign.getId()));

                });*/
                CampaignCards.add(campaignCard);
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

    private void openDialog(Object o) {
        Dialog dialog = new Dialog();
        dialog.open();
        Button cancelButton = new Button("Cancel", e -> {
            dialog.close();
        });

        Button confirmButton = new Button("Confirm", e -> {
            if (o instanceof Character){
                characterService.deleteCharacterFromId(((Character)o).getId());
            }
            if (o instanceof Campaign){
                campaignService.deleteCampaignById(((Campaign)o).getId());
            }
            dialog.close();
            UI.getCurrent().getPage().reload();

        });

        confirmButton.getStyle().set("margin-left", "auto");
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);


        dialog.add("Confirm deletion? Data cannot be restored");
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(confirmButton);

    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("continue")){

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {

            }


        }

    }
}
