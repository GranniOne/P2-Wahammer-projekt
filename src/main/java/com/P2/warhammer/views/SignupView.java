package com.P2.warhammer.views;


import com.P2.warhammer.utilities.ServiceProvider;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("signup")
@PageTitle("signup page")
@StyleSheet("css/signupStyle.css")
@AnonymousAllowed
public class SignupView extends Div {
    private final ServiceProvider services;

    SignupView(ServiceProvider services){
        this.services = services;

        setClassName("signupBody");


        Div test = new Div();
        test.setClassName("div-signupform");


        TextField firstName = new TextField("Username"); //det var ikke mig
        EmailField email = new EmailField("Email address");
        PasswordField password = new PasswordField("Password");
        PasswordField confirmPassword = new PasswordField("Confirm password");
        Button loginButton = new Button("Sign up", new Icon(VaadinIcon.ARROW_RIGHT), buttonClickEvent -> {
            String Firstname = firstName.getValue();
            String Email = email.getValue();
            String Password = password.getValue();
            String ConfirmPassword = confirmPassword.getValue();


            Boolean Result = services.getUserService().AuthenticateUser(Firstname, Email, Password,ConfirmPassword);

            if(Result){
                UI.getCurrent().navigate("login");
            }


            /*
            if(firstName.getValue().isEmpty() || email.getValue().isEmpty() || password.getValue().isEmpty() || confirmPassword.getValue().isEmpty()){
                System.out.println("the fields are empty");
                return;
            }

            if(password.getValue().equals(confirmPassword.getValue())) {
                System.out.println("it matches");


            }else{

                System.out.println("it does not match");
            }

             */

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
