package com.kpop.kpopbackend.services;

import com.kpop.kpopbackend.models.User;
import com.kpop.kpopbackend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User register(User user) {
        String email = user.getEmail().trim().toLowerCase();

        if (repository.findByEmail(email).isPresent()) return null;

        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("user");

        return repository.save(user);
    }

    public User login(String email, String password) {
        User user = repository.findByEmail(email.trim().toLowerCase()).orElse(null);

        if (user == null) return null;
        if (!passwordMatches(user, password)) return null;

        return user;
    }

    private boolean passwordMatches(User user, String rawPassword) {
        String storedPassword = user.getPassword();

        if (storedPassword == null) return false;

        if (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$") || storedPassword.startsWith("$2y$")) {
            return passwordEncoder.matches(rawPassword, storedPassword);
        }

        if (storedPassword.equals(rawPassword)) {
            user.setPassword(passwordEncoder.encode(rawPassword));
            repository.save(user);
            return true;
        }

        return false;
    }

    public User getUserById(int id) {
        return repository.findById(id).orElse(null);
    }

    public void deleteUser(int id) {
        repository.deleteById(id);
    }
}