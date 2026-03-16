package com.P2.warhammer.utilities;

import com.vaadin.flow.spring.security.VaadinSavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * class providing utility methods that the program needs
 */
public class Utilities {

    Utilities(){

    }

    /**
     * methods is used to check whether the user is authenticated or not
     * @return returns the authentication status of the user as a boolean
     */
    public static boolean authentication(){
        return SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
    }

    /**
     * used to redirect to a specific after logging in as a user-
     * @param http
     * @param string
     */
    public static void postLoggedIn(HttpSecurity http, String string){
        VaadinSavedRequestAwareAuthenticationSuccessHandler successHandler = new VaadinSavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setDefaultTargetUrl(string);
        successHandler.setAlwaysUseDefaultTargetUrl(true);
        http.setSharedObject(VaadinSavedRequestAwareAuthenticationSuccessHandler.class, successHandler);

    }
}
