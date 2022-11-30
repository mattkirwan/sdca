package com.sdca.api.controller;

import com.sdca.api.model.Island;
import com.sdca.api.model.User;
import com.sdca.api.model.item.Item;
import com.sdca.api.repository.IslandRepository;
import com.sdca.api.repository.ItemRepository;
import com.sdca.api.repository.UserRepository;
import com.sdca.api.repository.WorldRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/worlds/{worldId}/islands/{islandId}/items")
public class ItemController {

    private final UserRepository userRepository;
    private final WorldRepository worldRepository;
    private final IslandRepository islandRepository;
    private final ItemRepository itemRepository;

    public ItemController(UserRepository userRepository, WorldRepository worldRepository, IslandRepository islandRepository,ItemRepository itemRepository) {
        this.userRepository = userRepository;
        this.worldRepository = worldRepository;
        this.islandRepository = islandRepository;
        this.itemRepository = itemRepository;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public @ResponseBody Item createItem(
            @PathVariable Long userId,
            @PathVariable Long worldId,
            @PathVariable Long islandId,
            @RequestParam String name) {

        // TODO validate item belongs to user, world and island

        User user = this.userRepository.findById(userId).orElse(null);

        if (user == null) {
            // TODO Exit.
        }

        Island island = user.getWorlds().stream()
                .filter(w -> w.getId().equals(worldId))
                .flatMap(w -> w.getIslands().stream()
                        .filter(i -> i.getId().equals(islandId)))
                .findFirst()
                .orElse(null);

        if (island == null) {
            // TODO Exit
        }

        Item item = new Item(name);
        island.getItems().add(item);
        return (Item) itemRepository.save(item);

    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Iterable<Item> getItemsByIslandId(
            @PathVariable Long userId,
            @PathVariable Long worldId,
            @PathVariable Long islandId
    ) {

        // TODO validate item belongs to user, world and island

        Island island = islandRepository.findById(islandId).orElse(null);

        if (island == null) {
            // TODO island might be null
        }

        return island.getItems();

    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{itemId}/depleted")
    public Item setItemDepletedStatus(
            @PathVariable Long userId,
            @PathVariable Long worldId,
            @PathVariable Long islandId,
            @PathVariable Long itemId,
            @RequestParam Boolean status
    ) {

        // TODO validate item belongs to user, world and island

        Item item = itemRepository.findById(itemId).orElse(null);

        if (item == null) {
            // TODO null item
        }

        item.setIsDepleted(status);

        return itemRepository.save(item);

    }
}
