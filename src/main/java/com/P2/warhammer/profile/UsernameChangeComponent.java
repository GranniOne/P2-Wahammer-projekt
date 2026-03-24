package com.P2.warhammer.profile;

import com.P2.warhammer.users.User;
import com.P2.warhammer.users.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UsernameChangeComponent extends Div {
    private final UserService userService;
    private final Binder<User> userBinder;
    private final Authentication auth;

    public UsernameChangeComponent(UserService userService, Authentication auth) {

        this.userBinder = new Binder<>();

        // Greeter
        this.userService = userService;
        this.auth = auth;


        // Username text input, validator secures username is unique
        TextField usernameTextfield = new TextField("New username:");
        usernameTextfield.setValueChangeMode(ValueChangeMode.EAGER);
        userBinder.forField(usernameTextfield).withValidator(name ->
                        userService.findFromUsername(name) == null,
                "Username already taken").
                withValidator(name -> name.contains(" ") == false, "Spaces not allowed")
                .bind(User::getUsername, User::setUsername);

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
            if (userBinder.validate().isOk()) {
                dialogUsernameConfirm.removeAll();
                dialogUsernameConfirm.add("Change username to: " + usernameTextfield.getValue() + "?");
                dialogUsernameConfirm.getFooter().add(confirmUsernameButton);
                dialogUsernameConfirm.getFooter().add(cancelUsernameButton);
                dialogUsernameConfirm.open();
            }
        });

        //layout
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(usernameTextfield);
        verticalLayout.add(usernameButton);
        this.add(verticalLayout);
    }
}
