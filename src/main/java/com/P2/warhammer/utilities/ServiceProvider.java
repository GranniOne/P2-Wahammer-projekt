package com.P2.warhammer.utilities;

import com.P2.warhammer.users.UserService;
import com.P2.warhammer.characters.CharacterService;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

@Component
@UIScope
public class ServiceProvider {

    private final UserService userService;
    private final CharacterService characterService;

    public ServiceProvider(UserService userService, CharacterService characterService) {
        this.userService = userService;
        this.characterService = characterService;
    }

    public UserService getUserService() {
        return userService;
    }

    public CharacterService getCharacterService() {
        return characterService;
    }
}