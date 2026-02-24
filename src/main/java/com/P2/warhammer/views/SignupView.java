package com.P2.warhammer.views;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
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
@AnonymousAllowed
public class SignupView extends Div {
    SignupView(){

        getStyle().set("display", "flex").set("justify-content", "center");


        TextField firstName = new TextField("First name");
        TextField lastName = new TextField("Last name");
        EmailField email = new EmailField("Email address");
        PasswordField password = new PasswordField("Password");
        PasswordField confirmPassword = new PasswordField("Confirm password");
        Button loginButton = new Button("login", new Icon(VaadinIcon.ACADEMY_CAP), buttonClickEvent -> {
            UI.getCurrent().navigate("login");

        });


        FormLayout formLayout = new FormLayout();
        formLayout.setAutoResponsive(true);
        formLayout.addFormRow(firstName, lastName);
        formLayout.addFormRow(email);
        formLayout.addFormRow(password, confirmPassword);
        formLayout.addFormRow(loginButton);



        this.add(formLayout);
    }
}
