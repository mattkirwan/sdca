package com.sdca.api.model;

import com.sdca.api.model.item.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
public class Island {

    @GeneratedValue @Id @Getter
    private Long id;

    public Island(Byte x, Byte y) {
        this.x = x;
        this.y = y;
    }

    @Getter @Setter
    private Byte x;

    @Getter @Setter
    private Byte y;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    private List<Item> items = new ArrayList<>();
}
