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
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/users/{userId}/worlds/{worldId}")
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
    @PostMapping("/islands")
    public @ResponseBody Optional<Island> createIsland(@PathVariable Long userId, @PathVariable Long worldId, @RequestParam Byte x, @RequestParam Byte y) {

        return this.userRepository.findById(userId).map(user -> {
            List<World> worldBelongsToUser = user.getWorlds().stream()
                    .filter(w -> w.getId().equals(worldId))
                    .toList();

            if (worldBelongsToUser.stream().count() != 1) {
                // TODO exit. Not sure how to do this? exception?
            }

            Island island = new Island(x, y);
            worldBelongsToUser.get(0).getIslands().add(island); // TODO Also feels sketchy assuming there's 1 item (yes it's from the DB but still...)
            return islandRepository.save(island);

            // TODO this whole thing can be better no doubt but I wanna keep moving on
        });
    }

}
