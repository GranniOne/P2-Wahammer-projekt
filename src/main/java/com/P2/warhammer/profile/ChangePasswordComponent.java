package com.P2.warhammer.profile;

import com.P2.warhammer.users.User;
import com.P2.warhammer.users.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.springframework.security.core.Authentication;

public class ChangePasswordComponent extends Div {

    public ChangePasswordComponent(UserService userService, Authentication auth){
        Binder<User> passwordBinder = new Binder<>();
        Dialog changePasswordDialog = new Dialog();

        PasswordField passwordField = new PasswordField("Password");
        passwordField.setValueChangeMode(ValueChangeMode.EAGER);
        PasswordField confirmPasswordField = new PasswordField("Confirm password");
        confirmPasswordField.setValueChangeMode(ValueChangeMode.EAGER);

        passwordBinder.forField(passwordField)
                .withValidator(new StringLengthValidator(
                        "Password must be between 5 and 25 characters", 5, 25))
                .bind(User::getPassword, User::setPassword);

        passwordBinder.forField(confirmPasswordField)
                .withValidator(confirm -> confirm.equals(passwordField.getValue()),"passwords must match")
                .bind(User::getPassword, User::setPassword);

        Button confirmPasswordButton = new Button("Confirm", e -> {

            User user = userService.findFromEmail(auth.getName());
            user.setPassword(confirmPasswordField.getValue());
            userService.saveUser(user);
            UI.getCurrent().getPage().reload();

        });
        confirmPasswordButton.getStyle().set("margin-left", "auto");

        Button cancelPasswordButton = new Button("Cancel", e -> {
            changePasswordDialog.close();
        });
        cancelPasswordButton.addThemeVariants(ButtonVariant.LUMO_ERROR);


        Button changePasswordButton = new Button("Change password", e -> {
            if (! passwordBinder.validate().isOk()){
                return;
            }
            changePasswordDialog.open();
        });

        changePasswordDialog.add("Change password?");
        changePasswordDialog.getFooter().add(cancelPasswordButton);
        changePasswordDialog.getFooter().add(confirmPasswordButton);

        // Layout
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(passwordField);
        verticalLayout.add(confirmPasswordField);
        verticalLayout.add(changePasswordButton);
        this.add(verticalLayout);

    }
}
