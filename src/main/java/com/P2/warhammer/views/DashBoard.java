package com.P2.warhammer.views;



import com.P2.warhammer.users.UserService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@PageTitle("dashboard Page")
@Route("")
@PermitAll
public class DashBoard extends Div implements BeforeEnterObserver {
    private final UserService userService;

    DashBoard(UserService userService){
        this.userService = userService;
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();

        Span label = new Span("welcome " + auth.getName());
        label.getStyle().set("font-family", "Arial").setFontSize("xxx-large").setJustifyContent(Style.JustifyContent.CENTER).setAlignItems(Style.AlignItems.CENTER).setDisplay(Style.Display.FLEX);;
        add(label);

    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("continue")){

        }
    }
}
