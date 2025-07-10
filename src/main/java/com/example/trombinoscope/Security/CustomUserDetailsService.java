package com.example.trombinoscope.Security;

import com.example.trombinoscope.entities.User;
import com.example.trombinoscope.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Custom implementation of UserDetailsService.
 * This class tells Spring Security how to find a user and what authorities (roles) they have.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Spring Security calls this method when a user tries to log in.
     * It looks up the user by 'name' (which is your "username") and returns a UserDetails object.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // You are using 'name' as the login field instead of 'email' or 'username'
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),                        // This is the email for login
                user.getPassword(),                    // Spring will compare this with the submitted password
                Collections.singletonList(             // Authorities must be a non-empty collection
                        new SimpleGrantedAuthority("ROLE_" + user.getRole().name())  // e.g., ROLE_USER
                )
        );
    }
}
