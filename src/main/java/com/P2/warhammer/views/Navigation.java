package com.P2.warhammer.views;

import com.P2.warhammer.utilities.Utilities;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Layout("")
@AnonymousAllowed
public class Navigation extends AppLayout {

    Navigation() {

        boolean authenticated = Utilities.authentication();
        if(authenticated){
            setPrimarySection(Section.DRAWER);
            HorizontalLayout navBar = new HorizontalLayout();
            navBar.getStyle()
                    .set("padding", "0.5em")
                    .set("background-color", "#f0f0f0")
                    .set("width", "100%").set("display", "flex")
                    .set("justify-content", "center");

            // Static navigation buttons
            Button homeButton = new Button("Home", e -> UI.getCurrent().navigate("Dashboard"));
            Button profileButton = new Button("Profile", e -> UI.getCurrent().navigate("Profile"));
            Button settingsButton = new Button("Settings", e -> UI.getCurrent().navigate("Settings"));

            homeButton.setIcon(new Icon(VaadinIcon.HOME));
            profileButton.setIcon(new Icon(VaadinIcon.USER));
            settingsButton.setIcon(new Icon(VaadinIcon.COG));

            navBar.add(homeButton, profileButton, settingsButton);
            this.addToNavbar(navBar);

        }
    }
}
