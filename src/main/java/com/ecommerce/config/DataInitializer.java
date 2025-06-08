package com.ecommerce.config;

import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = Logger.getLogger(DataInitializer.class.getName());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Encode passwords for all users in the database
        userRepository.findAll().forEach(user -> {
            // Check if the password is not already encoded
            if (!user.getPassword().startsWith("$2a")) {
                logger.info("Encoding password for user: " + user.getUsername());
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);
            }
        });
        
        // Verify admin user exists
        userRepository.findByUsername("admin").ifPresentOrElse(
            user -> logger.info("Admin user exists with role: " + user.getRole()),
            () -> logger.warning("Admin user does not exist!")
        );
    }
}
