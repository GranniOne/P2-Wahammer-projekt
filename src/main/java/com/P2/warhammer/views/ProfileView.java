package com.P2.warhammer.views;

import com.P2.warhammer.layout.Navigation;
import com.P2.warhammer.users.User;
import com.P2.warhammer.users.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@Route("profile")
@PermitAll
@PageTitle("Profile")
public class  ProfileView extends VerticalLayout {

    private final UserService userService;

    ProfileView(UserService userService) {

        // Greeter
        this.userService = userService;
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();

        // Change username functionality
        TextField usernameTextfield = new TextField("New username");
        Button usernameButton = new Button("Change username", e -> {
            User user = userService.findFromEmail(auth.getName());
            user.setUsername(usernameTextfield.getValue());
            userService.saveUser(user);
            // tilføj validator, confirm og auto reload page
        });


        add(new H1("Hello " + userService.findFromEmail(auth.getName()).getUsername()));
        add(new H3("Profile settings"));
        // Change username field
        add(usernameTextfield);
        add(usernameButton);

    }
}
