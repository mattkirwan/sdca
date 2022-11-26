package com.sdca.api.controller;

import com.sdca.api.model.User;
import com.sdca.api.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User create(User user) { return userRepository.save(user); }

    @GetMapping
    public @ResponseBody Iterable<User> findAll() { return userRepository.findAll(); }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}")
    public @ResponseBody Optional<User> getUserById(@PathVariable Long userId) {
        return userRepository.findById(userId);
    }
    
}
