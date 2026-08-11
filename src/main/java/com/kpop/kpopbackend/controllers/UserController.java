package com.kpop.kpopbackend.controllers;

import com.kpop.kpopbackend.models.User;
import com.kpop.kpopbackend.services.JwtService;
import com.kpop.kpopbackend.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.kpop.kpopbackend.services.CurrentUserService;
import org.springframework.security.core.Authentication;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@CrossOrigin("*")
public class UserController {

    private final UserService service;
    private final JwtService jwtService;
    private final CurrentUserService currentUserService;


    public UserController(UserService service, JwtService jwtService, CurrentUserService currentUserService) {
        this.service = service;
        this.jwtService = jwtService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public List<User> getUsers() {
        return service.getAllUsers();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        User registeredUser = service.register(user);

        if (registeredUser == null) return ResponseEntity.badRequest().body("Email already registered");

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User result = service.login(user.getEmail(), user.getPassword());

        if (result == null) return ResponseEntity.badRequest().body("Invalid email or password");

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("token", jwtService.generateToken(result));
        response.put("user", result);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable int id, Authentication authentication) {
        if (!currentUserService.canAccessUser(authentication, id)) {
            return ResponseEntity.status(403).body("You cannot access another user's profile");
        }

        User user = service.getUserById(id);

        if (user == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        service.deleteUser(id);

        return "User deleted";
    }
}