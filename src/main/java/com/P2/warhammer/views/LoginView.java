package com.P2.warhammer.views;

import com.P2.warhammer.characters.CharacterRepository;
import com.P2.warhammer.characters.CharacterService;
import com.P2.warhammer.users.UserRepository;
import com.P2.warhammer.users.UserService;
import com.P2.warhammer.utilities.ServiceProvider;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.server.streams.DownloadHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

@Route("login")
@PageTitle("login page")
@StyleSheet("css/loginStyle.css")
@AnonymousAllowed
public class LoginView extends Div implements BeforeEnterObserver{

    private final LoginForm loginForm = new LoginForm();
    private final ServiceProvider services;




    public LoginView(ServiceProvider services) {
        this.services = services;


        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setForm(new LoginI18n.Form());
        i18n.getForm().setTitle("Warhammer Portal");
        i18n.getForm().setUsername("Email");
        i18n.getForm().setPassword("Password");
        i18n.getForm().setSubmit("Enter");
        i18n.setAdditionalInformation("Warhammer is a really insane roleplaying game, so login at your own discretion (～￣▽￣)～ ～(￣▽￣～)");

        loginForm.setI18n(i18n);

        loginForm.setClassName("loginform");

        setClassName("div-page");


        Div loginformDiv = new Div();
        loginformDiv.setClassName("div-login");

        loginForm.setAction("login"); // must match Spring Security endpoint

        loginformDiv.add(loginForm);





        Button RegisterButton = new Button("Log in", new Icon(VaadinIcon.ARROW_RIGHT), buttonClickEvent -> {
            UI.getCurrent().navigate("signup");
        });


        add(loginformDiv);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            loginForm.setError(true); // shows error if login fails
        }

    }


}
