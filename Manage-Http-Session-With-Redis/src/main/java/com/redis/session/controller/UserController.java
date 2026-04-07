package com.redis.session.controller;

import com.redis.session.entity.User;
import com.redis.session.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/session")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody User user){
        return new ResponseEntity<User>(userService.saveUser(user), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id){
        return userService.getUserById(id);
    }

    @GetMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session){
        return userService.login(email, password, session);
    }

    @GetMapping("/logout")
    public void logout(HttpSession session){
        session.invalidate();
    }

    /*@GetMapping("/profile")
    public String profile(HttpSession session){
        return userService.getProfile(session);
    }*/

    @GetMapping("/profile")
    public String profile(HttpServletRequest httpServletRequest){
        return userService.getProfile(httpServletRequest.getSession(false));
    }


}
