package com.sdca.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
public class World extends RepresentationModel<World> {

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

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    private List<Island> islands;

}
