package com.voyage.backend.controller;

import com.voyage.backend.model.User;
import com.voyage.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> registerRequest) {
        try {
            User user = authService.registerUser(
                registerRequest.get("username"),
                registerRequest.get("email"),
                registerRequest.get("password")
            );

            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully!");
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("token", authService.generateToken(user)); // Ajout du token

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        try {
            User user = authService.findByUsername(username);
            if (!authService.getPasswordEncoder().matches(password, user.getPassword())) {
                return ResponseEntity.status(401).body("Mot de passe incorrect");
            }
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Connexion réussie");
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("token", authService.generateToken(user)); // Ajout du token
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}