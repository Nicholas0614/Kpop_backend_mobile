package com.kpop.kpopbackend.services;

import com.kpop.kpopbackend.models.User;
import com.kpop.kpopbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;


    public UserService(UserRepository repository){
        this.repository = repository;
    }


    public List<User> getAllUsers(){
        return repository.findAll();
    }


    public User register(User user){

        user.setRole("user");

        return repository.save(user);
    }


    public User login(String email, String password){

        User user = repository.findByEmail(email)
                .orElse(null);

        if(user == null)
            return null;


        if(!user.getPassword().equals(password))
            return null;


        return user;
    }


    public User getUserById(int id){

        return repository.findById(id)
                .orElse(null);
    }


    public void deleteUser(int id){

        repository.deleteById(id);
    }

}