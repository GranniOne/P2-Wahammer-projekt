package com.P2.warhammer.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@PageTitle("login page")
@AnonymousAllowed
public class LoginView extends Div implements BeforeEnterObserver{

    private final LoginForm loginForm = new LoginForm();

    public LoginView() {
        loginForm.setAction("login"); // must match Spring Security endpoint
        getStyle()
                .set("display", "flex")
                .set("flex-direction", "column")   // vertical instead of horizontal
                .set("align-items", "center");     // center horizontally
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
