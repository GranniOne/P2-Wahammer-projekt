package com.P2.warhammer.views;

import com.P2.warhammer.characters.Character;
import com.P2.warhammer.characters.CharacterService;
import com.P2.warhammer.users.User;
import com.P2.warhammer.users.UserRepository;
import com.P2.warhammer.users.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.theme.lumo.LumoIcon;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.text.ComponentView;
import java.util.List;
import java.util.Map;

@PageTitle("User Page")
@RolesAllowed("ROLE_ADMIN")
@Route("admin-dashboard/UserProfile/:userID")
public class AdminProfileView extends Div implements BeforeEnterObserver {
    final UserService userService;
    final CharacterService characterService;
    private String userID;
    public AdminProfileView(UserService uSerService, CharacterService characterService) {
        this.userService = uSerService;
        this.characterService = characterService;
        System.out.println("first");

    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        userID = beforeEnterEvent.getRouteParameters().get("userID").get();
        User user = userService.getRepository().findDistinctById(userID);
        Div layout = new Div();
        layout.getStyle().set("display", "grid")
                .set("grid-template-columns",
                        "repeat(auto-fill, minmax(190px, 1fr))")
                .set("gap", "1em");

        // Card with image media
        Card imageCard = new Card();
        imageCard.add(
                "Lapland is the northern-most region of Finland and an active outdoor destination.");

        // Card with icon media
        Card iconCard = new Card();
        Icon icon = LumoIcon.PHOTO.create();
        iconCard.setMedia(icon);
        iconCard.add(
                "Lapland is the northern-most region of Finland and an active outdoor destination.");

        // Card with avatar media
        Card avatarCard = new Card();
        Avatar avatar = new Avatar("Lapland");
        avatarCard.setMedia(avatar);
        avatarCard.add(
                "Lapland is the northern-most region of Finland and an active outdoor destination.");

        layout.add(imageCard, iconCard, avatarCard);
        add(layout);



        RouteParameters params = new RouteParameters(
                Map.of("userID", userID, "characterID", "GranniCharacter")
        );

        String url = RouteConfiguration.forSessionScope()
                .getUrl(AdminCharacterView.class, params);

        add(new Button("Edit", e -> {
            UI.getCurrent().navigate(url);
        }));

        // The generated url is `item/123/edit`
        Anchor link = new Anchor(url, "Button Api");
        add(link);



    }
}
