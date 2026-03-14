package com.P2.warhammer.views;

import com.P2.warhammer.profile.UsernameChangeComponent;
import com.P2.warhammer.users.User;
import com.P2.warhammer.users.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.awt.*;
import java.util.Locale;


@Route("profile")
@PermitAll
@PageTitle("Profile")
@StyleSheet("css/profileStyle.css")
public class  ProfileView extends VerticalLayout {

    private final UserService userService;

    ProfileView(UserService userService) {
        this.userService = userService;
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();

        // Components
        UsernameChangeComponent usernameChangeComponent = new UsernameChangeComponent(userService, auth);


        setSizeFull();
        setAlignItems(FlexComponent.Alignment.CENTER);

        // Layout
        VerticalLayout layout = new VerticalLayout();
        layout.addClassName("profileViewBody");
        layout.setWidth("20%");
        layout.setHeight("auto");
        layout.setMargin(true);

        HorizontalLayout greeter = new HorizontalLayout();

        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(new H1("Hello " + userService.findFromEmail(auth.getName()).getUsername()));
        layout.add(new H3("Profile settings"));

        layout.add(usernameChangeComponent);
        layout.setAlignSelf(FlexComponent.Alignment.START, usernameChangeComponent);

        add(layout);

    }

}
