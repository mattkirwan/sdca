package com.sdca.api.controller;

import com.sdca.api.exception.IslandNotFoundException;
import com.sdca.api.exception.UserNotFoundException;
import com.sdca.api.exception.WorldNotFoundException;
import com.sdca.api.model.Island;
import com.sdca.api.model.User;
import com.sdca.api.model.World;
import com.sdca.api.model.item.Item;
import com.sdca.api.repository.IslandRepository;
import com.sdca.api.repository.UserRepository;
import com.sdca.api.repository.WorldRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users/{userId}/worlds/{worldId}/islands")
public class IslandController {

    private final UserRepository userRepository;
    private final WorldRepository worldRepository;
    private final IslandRepository islandRepository;

    public IslandController(UserRepository userRepository, WorldRepository worldRepository, IslandRepository islandRepository) {
        this.userRepository = userRepository;
        this.worldRepository = worldRepository;
        this.islandRepository = islandRepository;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public EntityModel<Island> createIsland(@PathVariable Long userId, @PathVariable Long worldId, @RequestParam Byte x, @RequestParam Byte y) {

        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        World world = user.getWorlds().stream()
                .filter(w -> w.getId().equals(worldId))
                .findFirst()
                .orElseThrow(() -> new WorldNotFoundException(worldId));

        Island island = new Island(x, y);
        world.getIslands().add(island);

        // Add guaranteed finite resources
        island.getItems().add(new Item("wood"));
        island.getItems().add(new Item("rocks"));

        islandRepository.save(island);

        return EntityModel.of(island,
                linkTo(methodOn(IslandController.class).getIslandById(userId, worldId, island.getId())).withSelfRel()
        );

    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public CollectionModel<EntityModel<Island>> getIslandsByWorldId(@PathVariable Long userId, @PathVariable Long worldId) {

        // TODO look into validation of this World actually belonging to the User

        World world = worldRepository.findById(worldId).orElseThrow(() -> new WorldNotFoundException(worldId));

        List<EntityModel<Island>> islands = world.getIslands().stream()
                .map(i -> EntityModel.of(i,
                    linkTo(methodOn(IslandController.class).getIslandById(userId, worldId, i.getId())).withRel("island"),
                    linkTo(methodOn(ItemController.class).getItemsByIslandId(userId, worldId, i.getId())).withRel("items")))
                .toList();

        return CollectionModel.of(islands, linkTo(methodOn(IslandController.class).getIslandsByWorldId(userId, worldId)).withSelfRel());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{islandId}")
    public EntityModel<Island> getIslandById(@PathVariable Long userId, @PathVariable Long worldId, @PathVariable Long islandId) {

        // TODO look into validation of this Island actually belonging to the World and the User

        Island island = this.islandRepository.findById(islandId)
                .orElseThrow(() -> new IslandNotFoundException(islandId));

        return EntityModel.of(island,
                linkTo(methodOn(IslandController.class).getIslandById(userId, worldId, islandId)).withSelfRel(),
                linkTo(methodOn(ItemController.class).getItemsByIslandId(userId, worldId, islandId)).withRel("items")
        );

    }

}
