package com.P2.warhammer.views;


import com.P2.warhammer.campaigns.Campaign;
import com.P2.warhammer.campaigns.CampaignService;
import com.P2.warhammer.users.User;
import com.P2.warhammer.users.UserService;
import com.P2.warhammer.utilities.Utilities;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;
import java.util.stream.Collectors;

@PermitAll
@StyleSheet("charactercreator.css")
@Route("campaigncreator")
public class CampaignCreatorView extends Div {

    private final CampaignService campaignService;
    private final UserService userService;
    private final Binder<User> binder;

    CampaignCreatorView(CampaignService campaignService, UserService userService) {
        this.campaignService = campaignService;
        this.userService = userService;
        binder = new Binder<>();
        Grid<User> grid = new Grid<>(User.class, false);
        grid.getStyle().setHeight("600px");

        getStyle().set("display", "flex").setJustifyContent(Style.JustifyContent.CENTER).setHeight("100%").setWidth("100%");
        Div layout = new Div();
        layout.getStyle().setHeight("70%").setWidth("70%").setBackground("darkslategrey").setAlignSelf(Style.AlignSelf.CENTER).setFlexDirection(Style.FlexDirection.COLUMN).setDisplay(Style.Display.FLEX).setGap("25px");

        H3 title = new H3("Create campaign");
        layout.add(title);
        title.getStyle().setJustifyContent(Style.JustifyContent.CENTER).setDisplay(Style.Display.FLEX);


        TextField campaignTextField = new TextField();
        campaignTextField.setLabel("Campaign name:");
        layout.add(campaignTextField);

        Span addUserText = new Span("Add user to campaign (e-mail):");
        layout.add(addUserText);
        HorizontalLayout addUserBody = new HorizontalLayout();

        EmailField addUserTextField = new EmailField();
        addUserTextField.setManualValidation(true);


        binder.forField(addUserTextField)
                .asRequired()
                .withValidator(new EmailValidator("Invalid email format"))
                .withValidator(emailvalue -> this.userService.findFromEmail(emailvalue) != null,"User not found")
                .bind(User::getEmail, User::setEmail);



        addUserBody.add(addUserTextField);
        List<User> users = new ArrayList<>();
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
        })).setHeader("Rem");


        grid.addColumn(User::getEmail).setHeader("Added Users");

        layout.add(grid);
        Button button = new Button("Create", event -> {
            if(campaignTextField.getValue().isEmpty()){
                return;
            }
            Campaign newCampaign = new Campaign(campaignTextField.getValue(),Utilities.getUserFromAuthentication());
            newCampaign.setPlayers(users);
            campaignService.addCompletedCampaign(newCampaign);
            UI.getCurrent().navigate(DashBoard.class);
        });
        button.getStyle().set("display", "flex").setAlignSelf(Style.AlignSelf.CENTER);
        layout.add(button);










        add(layout);
    }



}


