package com.P2.warhammer.profile;

import com.P2.warhammer.users.User;
import com.P2.warhammer.users.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.Authentication;

public class DeleteUserButtonComponent extends Div {

    public DeleteUserButtonComponent(UserService userService, Authentication auth, AuthenticationContext authenticationContext){
        Dialog dialog = new Dialog();

        Button button = new Button("Delete user", e -> {
            dialog.open();
        });
        button.addThemeVariants(ButtonVariant.LUMO_ERROR);


        Button cancelButton = new Button("Cancel", e -> {
            dialog.close();
        });

        Button confirmButton = new Button("Confirm", e -> {
           User user = userService.findFromEmail(auth.getName());
           userService.deleteUser(user);
           authenticationContext.logout();
        });

        confirmButton.getStyle().set("margin-left", "auto");
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);


        dialog.add("Confirm user deletion? Data cannot be restored");
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(confirmButton);

        this.add(button);
    }
}
