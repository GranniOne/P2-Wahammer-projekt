package com.P2.warhammer.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed("ROLE_ADMIN")
@Route("admin-dashboard/UserProfile/:userID/:characterID")
public class AdminCharacterView extends Div implements BeforeEnterObserver {
    public AdminCharacterView() {

    }
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {

    }
}
