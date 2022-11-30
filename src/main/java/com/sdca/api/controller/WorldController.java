package com.sdca.api.controller;

import com.sdca.api.model.Island;
import com.sdca.api.model.User;
import com.sdca.api.model.World;
import com.sdca.api.repository.UserRepository;
import com.sdca.api.repository.WorldRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

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
    public @ResponseBody Optional<World> create(@PathVariable Long userId, @RequestParam Integer saveSlot) {
        return this.userRepository.findById(userId).map(user -> {
            World world = new World(saveSlot);
            user.getWorlds().add(world);
            return worldRepository.save(world);
        });
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}/worlds")
    public @ResponseBody Iterable<World> getAllWorldsByUserId(@PathVariable Long userId) {
        User user = userRepository.findById(userId).get();
        return user.getWorlds();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}/worlds/{worldId}")
    @ResponseBody
    public Optional<World> getWorldById(@PathVariable Long userId, @PathVariable Long worldId) {

        // TODO validate world belongs to user

        return worldRepository.findById(worldId);

    }
}
