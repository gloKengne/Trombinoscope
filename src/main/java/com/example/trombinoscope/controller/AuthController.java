package com.example.trombinoscope.controller;

import com.example.trombinoscope.entities.User;
import com.example.trombinoscope.services.UserAlreadyExistsException;
import com.example.trombinoscope.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "registrationSuccess", required = false) String registrationSuccess,
                                Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password.");
        }
        if (registrationSuccess != null) {
            model.addAttribute("registrationSuccess", "Registration successful. Please login.");
        }
        return "auth/login";
    }

    @GetMapping("/register")
    public String showSignupPage(Model model) {
        model.addAttribute("user", new User());
        return "auth/registration";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if  (bindingResult.hasErrors()) {
            return "auth/registration";
        }
        try {
            // Use the UserService to register the user
            userService.registerUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please login.");
            return "redirect:/auth/login?registrationSuccess=true";
        } catch (UserAlreadyExistsException e) {
            // Handle specific user already exists exception
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/registration";
        } catch (Exception e) {
            // Handle other exceptions
            model.addAttribute("errorMessage", "An error occurred during registration: " + e.getMessage());
            return "auth/registration";
        }

    }


    @GetMapping("/logout")
    public String logout() {
        SecurityContextHolder.clearContext();
        return "redirect:/auth/login";
    }

    @GetMapping("/home")
    public String homePage(Model model, HttpSession session) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();

        model.addAttribute("currentUser", currentUser);
        return "auth/home";
    }

//    @PostMapping("/login")
//    public String login(
//            @RequestParam String username,
//            @RequestParam String password,
//            HttpSession session,
//            RedirectAttributes redirectAttributes
//    ) {
//        try {
//            log.info("Received request to login with username: {} and password: {}", username, password);
//
//            User user = userService.validateUser(username, password); // Implement this in UserService
//
//            if (user != null) {
//                session.setAttribute("currentUser", "gloria"); // Store user in session
//                return "redirect:/auth/home"; // Redirect to home
//            } else {
//                redirectAttributes.addAttribute("error", "Invalid credentials");
//                return "redirect:/auth/login";
//            }
//        } catch (Exception e) {
//            redirectAttributes.addAttribute("error", "Login failed");
//            return "redirect:/auth/login";
//        }
//    }
}

