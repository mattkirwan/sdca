package com.sdca.api.controller;

import com.sdca.api.model.User;
import com.sdca.api.repository.UserRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    @ResponseBody
    public EntityModel<User> getUserById(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElse(null);

        // TODO validate user / auth

        return EntityModel.of(user,
                linkTo(methodOn(UserController.class).getUserById(userId)).withSelfRel(),
                linkTo(methodOn(WorldController.class).getAllWorldsByUserId(userId)).withRel("worlds"));
    }

}
