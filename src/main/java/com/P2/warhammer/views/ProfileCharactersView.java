package com.P2.warhammer.views;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;


@PermitAll
@PageTitle("Profile Character page")
@Route("profileCharacters")
@StyleSheet("css/profileCharacterStyle.css")
public class ProfileCharactersView extends Div {


}
