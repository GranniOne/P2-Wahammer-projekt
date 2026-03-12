package com.P2.warhammer.views;


import com.P2.warhammer.layout.Navigation;
import com.P2.warhammer.users.User;
import com.P2.warhammer.users.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("signup")
@PageTitle("signup page")
@StyleSheet("css/signupStyle.css")
@AnonymousAllowed
public class SignupView extends Div {
    private final UserService userService;
    private final Binder<User> binder;

    SignupView(UserService userService){
        this.userService = userService;

        setClassName("signupBody");


        Div test = new Div();
        test.setClassName("div-signupform");


        TextField firstName = new TextField("Username"); //det var ikke mig
        EmailField email = new EmailField("Email address");
        email.setManualValidation(true);
        PasswordField password = new PasswordField("Password");
        PasswordField confirmPassword = new PasswordField("Confirm password");

        binder = new Binder<>();

        //username validation
        binder.forField(firstName)
                .asRequired()
                .withValidator(new StringLengthValidator(
                        "username must be between 4 and 15 characters", 4, 15))
                .bind(User::getUsername, User::setUsername);

        //email validation
        binder.forField(email)
                .asRequired()
                .withValidator(new EmailValidator("Invalid email format"))
                .withValidator(emailvalue -> this.userService.findFromEmail(emailvalue) == null,"Email address already exists")
                .bind(User::getEmail, User::setEmail);

        //password validation
        binder.forField(password)
                .asRequired()
                .withValidator(new StringLengthValidator(
                        "Password must be between 8 and 25 characters", 5, 25))
                .bind(User::getPassword, User::setPassword);

        //confirming password
        binder.forField(confirmPassword)
                .asRequired()
                .withValidator(confirm -> confirm.equals(password.getValue()),"passwords must match")
                .bind(user -> "",(u,v) -> {});



        var beanValidationErrors = new Div();
        beanValidationErrors.addClassName(LumoUtility.TextColor.ERROR);
        binder.setStatusLabel(beanValidationErrors);
        binder.setBean(new User());

        Button loginButton = new Button("Sign up", new Icon(VaadinIcon.ARROW_RIGHT), buttonClickEvent -> {
            String Firstname = firstName.getValue();
            String Email = email.getValue().toLowerCase();
            String Password = password.getValue();
            String ConfirmPassword = confirmPassword.getValue();

            if(binder.validate().isOk()) {
                this.userService.AuthenticateUser(Firstname, Email, Password,ConfirmPassword);
                UI.getCurrent().navigate(LoginView.class);
                System.out.println("its good");
            }



        });

        FormLayout formLayout = new FormLayout();
        formLayout.setAutoResponsive(true);
        formLayout.addFormRow(firstName);
        formLayout.addFormRow(email);
        formLayout.addFormRow(password);
        formLayout.addFormRow(confirmPassword);
        formLayout.addFormRow(loginButton);


        test.add(formLayout);
        add(test);
    }
}
