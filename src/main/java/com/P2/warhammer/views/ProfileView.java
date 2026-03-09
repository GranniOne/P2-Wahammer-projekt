package com.P2.warhammer.views;

import com.P2.warhammer.layout.Navigation;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;



@Route("profile")
@PermitAll
@PageTitle("Profile")
public class  ProfileView extends Div {

    ProfileView() {
        add(new Button("hererrsrs"));
        add(new Button("asdsd"));
        add(new Button("aDAadasdadadasdas"));
        add(new Button("asd"));
        add(new Button("asddadasdasdasd"));
    }
}
