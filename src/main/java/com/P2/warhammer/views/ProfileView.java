package com.P2.warhammer.views;

import com.P2.warhammer.profile.ChangePasswordComponent;
import com.P2.warhammer.profile.DeleteUserButtonComponent;
import com.P2.warhammer.profile.UsernameChangeComponent;
import com.P2.warhammer.users.UserService;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@Route("profile")
@PermitAll
@PageTitle("Profile")
@StyleSheet("css/profileStyle.css")
public class  ProfileView extends VerticalLayout {

    private final AuthenticationContext authenticationContext;

    ProfileView(UserService userService, AuthenticationContext authenticationContext) {
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
        this.authenticationContext = authenticationContext;

        // Components
        UsernameChangeComponent usernameChangeComponent = new UsernameChangeComponent(userService, auth);
        ChangePasswordComponent changePasswordComponent = new ChangePasswordComponent(userService, auth);
        DeleteUserButtonComponent deleteUserButtonComponent = new DeleteUserButtonComponent(userService, auth, authenticationContext);

        setSizeFull();
        setAlignItems(FlexComponent.Alignment.CENTER);

        // Layout
        VerticalLayout layout = new VerticalLayout();
        layout.addClassName("profileViewBody");
        layout.setWidth("30%");
        layout.setHeight("auto");
        layout.setMargin(true);

        H3 profileInformationHeader = new H3("User information");
        H3 profileSettingHeader = new H3("Profile settings");

        layout.setAlignItems(FlexComponent.Alignment.START);
        layout.add(profileInformationHeader);
        layout.setAlignSelf(Alignment.CENTER, profileInformationHeader);
        layout.add(new Pre("Username: " + userService.findFromEmail(auth.getName()).getUsername() +
                "\nE-mail: " + userService.findFromEmail(auth.getName()).getEmail()));
        layout.add(new Hr());

        layout.add(profileSettingHeader);
        layout.setAlignSelf(Alignment.CENTER, profileSettingHeader);

        layout.add(usernameChangeComponent);
        layout.setAlignSelf(FlexComponent.Alignment.START, usernameChangeComponent);

        layout.add(changePasswordComponent);
        layout.setAlignSelf(FlexComponent.Alignment.START, usernameChangeComponent);

        layout.add(deleteUserButtonComponent);
        layout.setAlignSelf(FlexComponent.Alignment.END, deleteUserButtonComponent);

        add(layout);

    }

}
