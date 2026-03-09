package com.P2.warhammer.utilities;

import com.vaadin.flow.spring.security.VaadinSavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContextHolder;

public class Utilities {

    Utilities(){

    }
    public static boolean authentication(){
        return SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
    }

    public static void postLoggedIn(HttpSecurity http, String string){
        VaadinSavedRequestAwareAuthenticationSuccessHandler successHandler = new VaadinSavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setDefaultTargetUrl(string);
        successHandler.setAlwaysUseDefaultTargetUrl(true);
        http.setSharedObject(VaadinSavedRequestAwareAuthenticationSuccessHandler.class, successHandler);

    }
}
