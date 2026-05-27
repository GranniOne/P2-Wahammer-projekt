package com.P2.warhammer.views;


import com.P2.warhammer.campaigns.Campaign;
import com.P2.warhammer.campaigns.CampaignService;
import com.P2.warhammer.users.User;
import com.P2.warhammer.users.UserService;
import com.P2.warhammer.utilities.Utilities;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.*;

@PermitAll
@StyleSheet("charactercreator.css")
@Route("campaigncreator")
public class CampaignCreatorView extends Div implements BeforeEnterObserver {

    private final CampaignService campaignService;
    private final UserService userService;
    private final Binder<User> binder;
    private final Binder<Campaign> campaignNameBinder;
    private final Binder<List<User>> campaignUsers;
    Campaign campaign;



    CampaignCreatorView(CampaignService campaignService, UserService userService) {
        this.campaignService = campaignService;
        this.userService = userService;
        binder = new Binder<>();
        this.campaignNameBinder = new Binder<>();
        this.campaignUsers = new Binder<>();
    }


    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        this.removeAll();
        String campaignId = beforeEnterEvent.getLocation().getQueryParameters().getSingleParameter("Campaign").orElse(null);
        TextField campaignTextField = new TextField();
        campaignNameBinder.forField(campaignTextField).bind(Campaign::getName, Campaign::setName);

        if (campaignId != null ){
            campaign = campaignService.getCampaignById(campaignId);
            campaignNameBinder.readBean(campaign);
        }else{
            if(campaignService.getCampaignsByPlayerAndGameMaster(Utilities.getUserFromAuthentication(),Utilities.getUserFromAuthentication()).size() >= 5){
                Notification.show("you have to many Campaigns", 5000, Notification.Position.MIDDLE).setThemeVariant(NotificationVariant.LUMO_ERROR,true);
                beforeEnterEvent.forwardTo(DashBoard.class);
            }

            campaign = new Campaign();
        }



        Grid<User> grid = new Grid<>(User.class, false);
        grid.getStyle().setHeight("600px");

        getStyle().set("display", "flex").setJustifyContent(Style.JustifyContent.CENTER).setHeight("100%").setWidth("100%");
        Div layout = new Div();
        layout.getStyle().setHeight("70%").setWidth("70%").setBackground("darkslategrey").setAlignSelf(Style.AlignSelf.CENTER).setFlexDirection(Style.FlexDirection.COLUMN).setDisplay(Style.Display.FLEX).setGap("25px");

        H3 title = new H3("Campaign");
        layout.add(title);
        title.getStyle().setJustifyContent(Style.JustifyContent.CENTER).setDisplay(Style.Display.FLEX);



        campaignTextField.setLabel("Campaign name:");

        layout.add(campaignTextField);

        Span addUserText = new Span("Add user to campaign (e-mail):");
        layout.add(addUserText);
        HorizontalLayout addUserBody = new HorizontalLayout();

        EmailField addUserTextField = new EmailField();
        addUserTextField.setManualValidation(true);

        binder.forField(addUserTextField)
                .asRequired()
                .withValidator(emailvalue -> this.userService.findFromEmail(emailvalue) != null,
                        "User not found")
                .withValidator(emailvalue -> !emailvalue.equals(Utilities.getUserFromAuthentication().getEmail()),
                        "You cannot add yourself")
                .bind(User::getEmail, User::setEmail);

        addUserTextField.setI18n(new EmailField.EmailFieldI18n().setPatternErrorMessage("Enter a valid email address"));




        addUserBody.add(addUserTextField);
        List<User> users = campaign.getPlayers() == null ? new ArrayList<>() : campaign.getPlayers();
        grid.setItems(users);
        Button addUserButton = new Button("Add", event -> {
            if(binder.validate().isOk() && users.stream().noneMatch(u -> u.getEmail().equals(addUserTextField.getValue()))) {
                users.add(this.userService.findFromEmail(addUserTextField.getValue()));
                addUserTextField.clear();
                grid.getDataProvider().refreshAll();


            }
        });

        addUserBody.add(addUserButton);
        layout.add(addUserBody);

        grid.addComponentColumn(user -> new Button("X",event -> {
            users.remove(users.indexOf(user));
            grid.getDataProvider().refreshAll();
        })).setHeader("Remove user");


        grid.addColumn(User::getEmail).setHeader("Added Users");

        layout.add(grid);
        Button button = new Button("Save", event -> {
            if(campaignTextField.getValue().isEmpty()){
                return;
            }
            campaign.setPlayers(users);
            campaign.setName(campaignTextField.getValue());
            campaign.setGameMaster(Utilities.getUserFromAuthentication());
            campaignService.addCompletedCampaign(campaign);
            UI.getCurrent().navigate(DashBoard.class);
        });
        button.getStyle().set("display", "flex").setAlignSelf(Style.AlignSelf.CENTER);
        layout.add(button);

        add(layout);

    }
}


