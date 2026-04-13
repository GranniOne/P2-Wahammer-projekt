package com.P2.warhammer.views;



import com.P2.warhammer.users.UserService;
import com.vaadin.flow.component.dependency.StyleSheet;
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

@StyleSheet("css/dashboardPage.css")
@PageTitle("dashboard Page")
@Route("")
@PermitAll
public class DashBoard extends Div implements BeforeEnterObserver {
    private final UserService userService;

    DashBoard(UserService userService){
        this.userService = userService;
        setClassName("dashboard");
        Div dashboard = new Div();

        Div characterContent = new Div();
        Span CharacterTitle = new Span("Characters");
        Div CharacterCards = new Div();

        Div CampaignContent = new Div();
        Span CampaignTitle = new Span("Campaigns");
        Div CampaignCards = new Div();

        dashboard.setClassName("dashboardContent");
        characterContent.setClassName("characterContent");
        CampaignContent.setClassName("CampaignContent");
        CharacterTitle.setClassName("CharacterTitle");
        CampaignTitle.setClassName("CampaignTitle");
        CampaignCards.setClassName("CampaignCards");
        CharacterCards.setClassName("CharacterCards");


        characterContent.add(CharacterTitle,CharacterCards);
        CampaignContent.add(CampaignTitle,CampaignCards);

        dashboard.add(characterContent,CampaignContent);
        add(dashboard);





    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("continue")){

        }

    }
}
