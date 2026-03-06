

package com.P2.warhammer.security;

import com.P2.warhammer.users.User;
import com.P2.warhammer.utilities.ServiceProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

@Service
public class MongoAuthUserDetailService implements UserDetailsService {

    private final ServiceProvider services;

    public MongoAuthUserDetailService(ServiceProvider services) {
        this.services = services;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = services.getUserService().findFromEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + email);
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));

        System.out.println(user.getEmail());
        System.out.println(user.getPassword());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                grantedAuthorities
        );
    }
}

