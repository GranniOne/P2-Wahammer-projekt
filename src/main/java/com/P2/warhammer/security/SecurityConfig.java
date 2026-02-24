package com.P2.warhammer.security;

import com.P2.warhammer.views.LoginView;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /*
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin-only/**").hasAnyRole("ADMIN")
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/error").permitAll());

         */
        // Let Vaadin handle all authentication & route protection
        return http.with(VaadinSecurityConfigurer.vaadin(), configurer -> {
            configurer.loginView(LoginView.class); // automatically allows login page
        }).build();
    }


    // In-memory user
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        LoggerFactory.getLogger(SecurityConfig.class)
                .warn("NOT FOR PRODUCTION: Using in-memory user details manager!");
        UserDetails user = User.withUsername("admin")
                .password("admin") // plain text for testing
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    // Plain text password encoder (testing only)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}