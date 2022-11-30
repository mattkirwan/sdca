package com.sdca.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
public class World {

    @Getter
    private @GeneratedValue @Id Long id;

    public World(Integer saveSlot, Long seed) {
        this.saveSlot = saveSlot;
        this.seed = seed;
    }

    @Getter
    @Setter
    private Integer saveSlot;

    @Getter
    @Setter
    private Long seed;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    private List<Island> islands = new ArrayList<>();

}
