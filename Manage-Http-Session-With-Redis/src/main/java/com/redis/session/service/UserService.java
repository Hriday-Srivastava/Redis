package com.redis.session.service;

import com.redis.session.entity.User;
import com.redis.session.entity.UserRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User saveUser(User user) {
        return userRepo.save(user);
    }

    public User getUserById(int id) {
        return userRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found with ID : " + id));
    }

    public String login(String emailId, String password, HttpSession httpSession) {
        return userRepo.findByEmailIgnoreCase(emailId).map(user -> {
                    if (!user.getPassword().equalsIgnoreCase(password))
                        return "Invalid Password";

                    httpSession.setAttribute("email", user.getEmail());
                    httpSession.setAttribute("role", user.getRole());
                    httpSession.setAttribute("userId", user.getId());

                    return "Login Successful with SESSION_ID : " + httpSession.getId();
                }
        ).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found With ID: " + emailId));
    }

    public String getProfile(HttpSession session) {

        if (session == null)
            return "Not Logged in yet";

        Object email = session.getAttribute("email");
        Object role = session.getAttribute("role");

        return "You already logged in with EMAIL ID : " + email + " and with ROLE : " + role;

    }
}
