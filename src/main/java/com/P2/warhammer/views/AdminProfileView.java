package com.P2.warhammer.views;

import com.P2.warhammer.Skills.Skill;
import com.P2.warhammer.Skills.SkillRepository;
import com.P2.warhammer.characters.Character;
import com.P2.warhammer.characters.CharacterService;
import com.P2.warhammer.layout.CharacterCard;
import com.P2.warhammer.users.User;
import com.P2.warhammer.users.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@PageTitle("User Page")
@RolesAllowed("ROLE_ADMIN")
@Route("admin-dashboard/UserProfile/:userID")
public class AdminProfileView extends Div implements BeforeEnterObserver {
    final UserService userService;
    final CharacterService characterService;
    final SkillRepository skillRepository;
    private String userID;
    public AdminProfileView(UserService userService, CharacterService characterService, SkillRepository skillRepository) {
        this.userService = userService;
        this.characterService = characterService;
        this.skillRepository = skillRepository;
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
        characterService.getCharactersByUser(userID).forEach(c -> {
            try{
                layout.add(new CharacterCard(c));

            }catch (NoSuchElementException e){
                e.printStackTrace();
            }

        });
        this.add(layout);

        this.add(new Button("hello", event -> {
            Character character = new Character("Testing",user,user);
            List<Skill> Usedskills =  skillRepository.findAll();
            character.setSkills(Usedskills);
            characterService.addCharacter(character);

        }));

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
