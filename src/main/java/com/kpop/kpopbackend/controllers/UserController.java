package com.kpop.kpopbackend.controllers;


import com.kpop.kpopbackend.models.User;
import com.kpop.kpopbackend.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
@CrossOrigin("*")
public class UserController {


    private final UserService service;


    public UserController(UserService service){
        this.service = service;
    }



    @GetMapping
    public List<User> getUsers(){

        return service.getAllUsers();

    }



    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody User user
    ){

        return ResponseEntity.ok(
                service.register(user)
        );

    }



    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody User user
    ){

        User result = service.login(
                user.getEmail(),
                user.getPassword()
        );


        if(result == null){

            return ResponseEntity
                    .badRequest()
                    .body("Invalid email or password");

        }


        return ResponseEntity.ok(result);

    }



    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(
            @PathVariable int id
    ){

        User user = service.getUserById(id);


        if(user == null)
            return ResponseEntity.notFound().build();


        return ResponseEntity.ok(user);

    }


    @DeleteMapping("/{id}")
    public String delete(
            @PathVariable int id
    ){

        service.deleteUser(id);

        return "User deleted";

    }

}