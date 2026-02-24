package com.P2.warhammer.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.PermitAll;

@Route("login")
@PageTitle("login page")
@PermitAll
public class LoginView extends Div implements BeforeEnterObserver{

    private final LoginForm loginForm = new LoginForm();

    public LoginView() {
        loginForm.setAction("login"); // must match Spring Security endpoint
        getStyle().set("display", "flex").set("justify-content", "center");
        add(loginForm);


    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            loginForm.setError(true); // shows error if login fails
        }
    }


}
