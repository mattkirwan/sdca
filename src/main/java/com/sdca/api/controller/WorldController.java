package com.sdca.api.controller;

import com.sdca.api.model.User;
import com.sdca.api.model.World;
import com.sdca.api.repository.UserRepository;
import com.sdca.api.repository.WorldRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class WorldController {

    private final WorldRepository worldRepository;
    private final UserRepository userRepository;

    public WorldController(UserRepository userRepository, WorldRepository worldRepository) {
        this.userRepository = userRepository;
        this.worldRepository = worldRepository;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}/worlds")
    public @ResponseBody EntityModel<World> create(
            @PathVariable Long userId,
            @RequestParam Integer saveSlot,
            @RequestParam(required = false) Long seed
    ) {

        User user = this.userRepository.findById(userId).orElse(null);

        // TODO Check user is good

        List<World> worlds = user.getWorlds();

        if (worlds == null) {

        }

        World world = new World(saveSlot, seed);
        user.getWorlds().add(world);

        worldRepository.save(world);

        return EntityModel.of(
                world, linkTo(
                        methodOn(WorldController.class)
                                .getWorldById(userId, world.getId()))
                        .withSelfRel());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}/worlds")
    @ResponseBody
    public CollectionModel<EntityModel<World>> getAllWorldsByUserId(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElse(null);

        // TODO validate user

        List<EntityModel<World>> worlds = user.getWorlds().stream().map(w -> EntityModel.of(w,
                linkTo(methodOn(WorldController.class).getWorldById(userId, w.getId())).withRel("world")))
                .collect(Collectors.toList());

        return CollectionModel.of(worlds, linkTo(methodOn(WorldController.class).getAllWorldsByUserId(userId)).withSelfRel());
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}/worlds/{worldId}")
    @ResponseBody
    public EntityModel<World> getWorldById(@PathVariable Long userId, @PathVariable Long worldId) {

        World world = worldRepository.findById(worldId).orElse(null);

        // TODO validate world belongs to user

        return EntityModel.of(world,
                linkTo(methodOn(WorldController.class).getWorldById(userId, worldId)).withSelfRel(),
                linkTo(methodOn(IslandController.class).getIslandsByWorldId(userId, worldId)).withRel("islands"));
    }
}
