package com.P2.warhammer.views;

import com.P2.warhammer.characters.CharacterRepository;
import com.P2.warhammer.characters.CharacterService;
import com.P2.warhammer.users.UserRepository;
import com.P2.warhammer.users.UserService;
import com.P2.warhammer.utilities.ServiceProvider;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Route("login")
@PageTitle("login page")
@AnonymousAllowed
public class LoginView extends Div implements BeforeEnterObserver{

    private final LoginForm loginForm = new LoginForm();
    private final ServiceProvider services;




    public LoginView(ServiceProvider services) {
        this.services = services;
        loginForm.setAction("login"); // must match Spring Security endpoint
        getStyle()
                .set("display", "flex")
                .set("flex-direction", "column")   // vertical instead of horizontal
                .set("align-items", "center")
                .setHeight("100%")
                .set("justify-content","center");     // center horizontally
        add(loginForm);


        Button RegisterButton = new Button("Sign up", new Icon(VaadinIcon.ACADEMY_CAP), buttonClickEvent -> {
            UI.getCurrent().navigate("signup");
        });

        add(RegisterButton);


    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            loginForm.setError(true); // shows error if login fails
        }
    }


}
