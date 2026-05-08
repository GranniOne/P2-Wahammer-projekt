package com.P2.warhammer.views;

import com.P2.warhammer.users.User;
import com.P2.warhammer.users.UserService;
import com.P2.warhammer.utilities.Utilities;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.router.*;

@Route(value = "login", autoLayout = false)
@PageTitle("login page")
@StyleSheet("css/loginStyle.css")
public class LoginView extends Div implements BeforeEnterObserver{

    private final LoginForm loginForm = new LoginForm();
    private final UserService userService;




    public LoginView(UserService userService) {
        this.userService = userService;


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


        Button RegisterButton = new Button("signup", new Icon(VaadinIcon.ARROW_RIGHT), buttonClickEvent -> {
            UI.getCurrent().navigate("signup");
        });
        RegisterButton.setClassName("signup");
        RegisterButton.getStyle().setMarginTop("100px");
        loginformDiv.add(RegisterButton);




        add(loginformDiv);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            loginForm.setError(true); // shows error if login fails
        }
        if(Utilities.authentication()){
            beforeEnterEvent.forwardTo(DashBoard.class);
        }

    }


}
