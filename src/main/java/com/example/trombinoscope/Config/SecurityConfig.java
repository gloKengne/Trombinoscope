package com.example.trombinoscope.Config;

import com.example.trombinoscope.Security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Optional, or configure for forms
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll() //pour ouvrir toutes les route de l'application et ne pas considere srping security
                        //.requestMatchers("/auth/register", "/auth/login", "/css/**", "/js/**", "/images/**").permitAll() // Allow access to login/register
                        .anyRequest().authenticated() // Secure all other endpoints
                )
                .formLogin(form -> form
                        //.loginPage("/auth/login") // Your custom Thymeleaf login page (GET /login)
                        //.loginProcessingUrl("/auth/login") // The POST endpoint to submit credentials
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/auth/home", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }


}

