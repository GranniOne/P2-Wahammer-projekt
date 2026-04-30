package com.P2.warhammer.layout;

import com.P2.warhammer.utilities.Utilities;
import com.P2.warhammer.views.CharacterCreatorView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;

/**
 * the Navigation layout, is automatically implemented by spring using the @Layout annotation, do not use it manually
 */
@Layout("")
@PermitAll
public class Navigation extends AppLayout {

    Navigation(AuthenticationContext authenticationContext) {

        boolean authenticated = Utilities.authentication();
        if(authenticated){
            setPrimarySection(Section.DRAWER);
            HorizontalLayout navBar = new HorizontalLayout();
            navBar.getStyle()
                    .set("padding", "0.5em")
                    .set("background-color", "#f0f0f0")
                    .set("width", "100%").set("display", "flex");

            // Static navigation buttons
            Button homeButton = new Button("Home", e -> UI.getCurrent().navigate(""));
            Button profileButton = new Button("Profile", e -> UI.getCurrent().navigate("profile"));
            Button profileCharactersButton = new Button("View characters", e -> UI.getCurrent().navigate("profileCharacters"));
            Button campaignViewButton = new Button("characterCreator", e -> UI.getCurrent().navigate("characterCreator"));
            Button campaigncreator = new Button("campaigncreator", e -> UI.getCurrent().navigate("campaigncreator"));
            Button admindashboard = new Button("admin dashboard", e -> UI.getCurrent().navigate("admin-dashboard"));
            Button characterCreatorbutton = new Button("button", e -> UI.getCurrent().navigate(CharacterCreatorView.class, QueryParameters.of("Character","65f1a2b3c4d5e6f7a8b90123")));

            Button logoutButton = new Button("Log out", e -> authenticationContext.logout());

            homeButton.setIcon(new Icon(VaadinIcon.HOME));
            profileButton.setIcon(new Icon(VaadinIcon.USER));
            campaigncreator.setIcon(new Icon(VaadinIcon.COG));
            profileCharactersButton.setIcon(new Icon(VaadinIcon.MALE));
            logoutButton.setIcon(new Icon(VaadinIcon.SIGN_OUT));
            campaignViewButton.setIcon(new Icon(VaadinIcon.MAP_MARKER));

            navBar.addToStart(homeButton, profileButton, campaigncreator, campaignViewButton, profileCharactersButton, admindashboard,characterCreatorbutton);
            navBar.addToEnd(logoutButton);
            this.addToNavbar(navBar);

        }
    }
}
