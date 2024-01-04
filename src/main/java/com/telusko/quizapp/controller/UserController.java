package com.telusko.quizapp.controller;

import com.telusko.quizapp.dao.UserDao;
import com.telusko.quizapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserDao userDao;

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        User existingUser = userDao.findByUsernameAndPassword(user.getUsername(), user.getPassword());

        if (existingUser != null) {
            // Implement JWT generation and return it
            // For simplicity, just returning a dummy token
            return "{\"token\":\"user_found\"}";
        } else {
            // Return an error response
            return "{\"error\":\"Invalid credentials\"}";
        }
    }
    @PostMapping("/register")
    public String register(@RequestBody User user) {
        User existingUser = userDao.findByUsername(user.getUsername());
        if(existingUser!=null){
            return "{\"error\":\"User with this username already exist\"}";
        }
        else {
            userDao.save(user);
            return "{\"token\":\"User_Created_Successfully\"}";
        }
    }
}
