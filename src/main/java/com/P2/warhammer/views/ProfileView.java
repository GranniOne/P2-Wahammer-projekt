package com.P2.warhammer.views;

import com.P2.warhammer.users.User;
import com.P2.warhammer.users.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
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
    private final Binder<User> userBinder;

    ProfileView(UserService userService) {

        this.userBinder = new Binder<>();

        // Greeter
        this.userService = userService;
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();


        // Change username functionality
        TextField usernameTextfield = new TextField("New username");

        userBinder.forField(usernameTextfield).withValidator(name ->
                        userService.findFromUsername(name) == null,
                "User already exists").bind(User::getUsername, User::setUsername);

        Button usernameButton = new Button("Change username", e -> {

            if(userBinder.validate().isOk()) {
                User user = userService.findFromEmail(auth.getName());
                user.setUsername(usernameTextfield.getValue());
                userService.saveUser(user);
                UI.getCurrent().getPage().reload();
                // tilføj validator, confirm og auto reload page
                }
        });




        // Layout
        add(new H1("Hello " + userService.findFromEmail(auth.getName()).getUsername()));
        add(new H3("Profile settings"));
        add(usernameTextfield);
        add(usernameButton);

    }

}
