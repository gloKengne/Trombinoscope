package com.example.trombinoscope.services;

import com.example.trombinoscope.repositories.UserRepository;
import com.example.trombinoscope.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        public User register(String username, String email, String password) {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            return userRepository.save(user);
        }

        public User authenticate(String email, String rawPassword) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Invalid email or password"));

            if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
                throw new RuntimeException("Invalid email or password");
            }

            return user;
        }

    public User registerUser(User user) throws UserAlreadyExistsException {
        // Check if username already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        // Create new user
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(User.Role.USER);

        return userRepository.save(newUser);
    }

    public User validateUser(String username, String password) {
            return new User();
//        return userRepository.findByUsername(username)
//                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
//                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
    }

    public Optional<User> findByUsername(String username) {
            return userRepository.findByUsername(username);
    }
}

