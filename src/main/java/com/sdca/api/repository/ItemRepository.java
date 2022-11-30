package com.sdca.api.repository;

import com.sdca.api.model.item.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, Long> {
}
