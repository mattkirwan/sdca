package com.sdca.api.controller;

import com.sdca.api.model.Island;
import com.sdca.api.model.User;
import com.sdca.api.model.World;
import com.sdca.api.model.item.Item;
import com.sdca.api.repository.IslandRepository;
import com.sdca.api.repository.UserRepository;
import com.sdca.api.repository.WorldRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public @ResponseBody Island createIsland(@PathVariable Long userId, @PathVariable Long worldId, @RequestParam Byte x, @RequestParam Byte y) {

        User user = this.userRepository.findById(userId).orElse(null);

        if (user == null) {
            // TODO user might be null
        }

        World world = user.getWorlds().stream()
                .filter(w -> w.getId().equals(worldId))
                .findFirst()
                .orElse(null);

        if (world == null) {
            // TODO world might be null
        }

        Island island = new Island(x, y);
        world.getIslands().add(island);

        // Add guaranteed finite resources
        Item wood = new Item("wood");
        Item rocks = new Item("rocks");
        island.getItems().add(wood);
        island.getItems().add(rocks);

        return islandRepository.save(island);

    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @ResponseBody
    public Iterable<Island> getIslandsByWorldId(@PathVariable Long userId, @PathVariable Long worldId) {

        // TODO look into validation of this World actually belonging to the User

        return this.worldRepository.findById(worldId).get().getIslands();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{islandId}")
    @ResponseBody
    public Optional<Island> getIslandById(@PathVariable Long userId, @PathVariable Long worldId, @PathVariable Long islandId) {

        // TODO look into validation of this Island actually belonging to the World and the User

        return this.islandRepository.findById(islandId);
    }

}
