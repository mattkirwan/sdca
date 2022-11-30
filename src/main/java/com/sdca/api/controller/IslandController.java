package com.sdca.api.controller;

import com.sdca.api.model.Island;
import com.sdca.api.model.World;
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
    public @ResponseBody Optional<Island> createIsland(@PathVariable Long userId, @PathVariable Long worldId, @RequestParam Byte x, @RequestParam Byte y) {

        return this.userRepository.findById(userId).map(user -> {
            List<World> worldBelongsToUser = user.getWorlds().stream()
                    .filter(w -> w.getId().equals(worldId))
                    .toList();

            if (worldBelongsToUser.stream().count() != 1) {
                // TODO exit. Not sure how to do this? exception?
            }

            Island island = new Island(x, y);
            // TODO Also feels sketchy assuming there's 1 item (yes it's from the DB but still...)
            // Yep as predicted: Request processing failed: java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0
            // when trying to create an island on a world that doesn't exist
            worldBelongsToUser.get(0).getIslands().add(island);
            return islandRepository.save(island);

            // TODO this whole thing can be better no doubt but I wanna keep moving on
        });
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
