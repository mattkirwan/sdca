package com.sdca.api.controller;

import com.sdca.api.exception.ItemNotFoundException;
import com.sdca.api.exception.UserNotFoundException;
import com.sdca.api.model.Island;
import com.sdca.api.model.User;
import com.sdca.api.model.item.Item;
import com.sdca.api.repository.IslandRepository;
import com.sdca.api.repository.ItemRepository;
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
    public EntityModel<Item> createItem(
            @PathVariable Long userId,
            @PathVariable Long worldId,
            @PathVariable Long islandId,
            @RequestParam String name) {

        // TODO validate item belongs to user, world and island

        User user = this.userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

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
        itemRepository.save(item);

        return EntityModel.of(item, linkTo(methodOn(ItemController.class).getItemById(userId, worldId, islandId, item.getId())).withSelfRel());

    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{itemId}")
    public EntityModel<Item> getItemById(
            @PathVariable Long userId,
            @PathVariable Long worldId,
            @PathVariable Long islandId,
            @PathVariable Long itemId
    ) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));

        if (item == null) {
            // TODO if item null
        }

        return EntityModel.of(item, linkTo(methodOn(ItemController.class).getItemById(userId, worldId, islandId, itemId)).withSelfRel());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public CollectionModel<EntityModel<Item>> getItemsByIslandId(
            @PathVariable Long userId,
            @PathVariable Long worldId,
            @PathVariable Long islandId
    ) {

        // TODO validate item belongs to user, world and island

        Island island = islandRepository.findById(islandId).orElse(null);

        if (island == null) {
            // TODO island might be null
        }

        List<EntityModel<Item>> items = island.getItems().stream()
                .map(i -> EntityModel.of(i, linkTo(methodOn(ItemController.class).getItemById(userId, worldId, islandId, i.getId())).withSelfRel()))
                .toList();

        return CollectionModel.of(items, linkTo(methodOn(ItemController.class).getItemsByIslandId(userId, worldId, islandId)).withSelfRel());

    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{itemId}/depleted")
    public EntityModel<Item> setItemDepletedStatus(
            @PathVariable Long userId,
            @PathVariable Long worldId,
            @PathVariable Long islandId,
            @PathVariable Long itemId,
            @RequestParam Boolean status
    ) {

        // TODO validate item belongs to user, world and island

        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));

        if (item == null) {
            // TODO null item
        }

        item.setIsDepleted(status);

        itemRepository.save(item);

        return EntityModel.of(item, linkTo(methodOn(ItemController.class).getItemById(userId, worldId, islandId, itemId)).withSelfRel());

    }
}
