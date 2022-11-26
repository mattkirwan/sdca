package com.sdca.api.repository;

import com.sdca.api.model.World;
import org.springframework.data.repository.CrudRepository;

public interface WorldRepository extends CrudRepository<World, Integer> {
}
