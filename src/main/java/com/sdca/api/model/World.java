package com.sdca.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class World {

    private @GeneratedValue @Id Long id;

    public World(Integer saveSlot) {
        this.saveSlot = saveSlot;
    }

    @Getter
    @Setter
    private Integer saveSlot;

    @Getter
    @Setter
    private Long seed;
}
