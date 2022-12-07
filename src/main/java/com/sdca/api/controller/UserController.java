package com.sdca.api.controller;

import com.sdca.api.model.User;
import com.sdca.api.repository.UserRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.StreamSupport;

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
    public EntityModel<User> create(User user) {

        userRepository.save(user);

        return EntityModel.of(user, linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel());

    }

    @GetMapping
    @ResponseBody
    public CollectionModel<EntityModel<User>> findAll() {

        List<EntityModel<User>> users = StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .map(u -> EntityModel.of(u,
                            linkTo(methodOn(UserController.class).getUserById(u.getId()))
                                    .withRel("user")))
                .toList();

        return CollectionModel.of(users, linkTo(methodOn(UserController.class).findAll()).withSelfRel());
    }

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
