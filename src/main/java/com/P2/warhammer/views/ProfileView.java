package com.P2.warhammer.views;

import com.P2.warhammer.users.User;
import com.P2.warhammer.users.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Locale;


@Route("profile")
@PermitAll
@PageTitle("Profile")
public class  ProfileView extends VerticalLayout {

    private final UserService userService;
    private final Binder<User> userBinder;
    private String inputUsername;

    ProfileView(UserService userService) {

        this.userBinder = new Binder<>();

        // Greeter
        this.userService = userService;
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();


        // Username text input, validator secures username is unique
        TextField usernameTextfield = new TextField("New username");
        usernameTextfield.setValueChangeMode(ValueChangeMode.EAGER);
        userBinder.forField(usernameTextfield).withValidator(name ->
                        userService.findFromUsername(name) == null,
                "Username not available").bind(User::getUsername, User::setUsername);

        // Dialog pop-up for usernameChange
        Dialog dialogUsernameConfirm = new Dialog();

        Button confirmUsernameButton = new Button("Confirm", e -> {
            User user = userService.findFromEmail(auth.getName());
            user.setUsername(usernameTextfield.getValue());
            userService.saveUser(user);
            UI.getCurrent().getPage().reload();
        });

        Button cancelUsernameButton = new Button("Cancel", e -> {
            dialogUsernameConfirm.close();
        });
        cancelUsernameButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelUsernameButton.getStyle().set("margin-left", "auto");

        // Username change button, this also builds dialog box
        Button usernameButton = new Button("Change username", e -> {
            if(userBinder.validate().isOk()) {
                dialogUsernameConfirm.removeAll();
                dialogUsernameConfirm.add("Change username to: " + usernameTextfield.getValue() + "?");
                dialogUsernameConfirm.getFooter().add(confirmUsernameButton);
                dialogUsernameConfirm.getFooter().add(cancelUsernameButton);
                dialogUsernameConfirm.open();
            }
        });


        // Layout
        add(new H1("Hello " + userService.findFromEmail(auth.getName()).getUsername()));
        add(new H3("Profile settings"));
        add(usernameTextfield);
        add(usernameButton);

    }

}
