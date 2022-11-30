package com.sdca.api.model.item;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Item {

    @GeneratedValue @Id @Getter
    private Long id;

    @Getter
    private String name;

    @Getter @Setter
    public Boolean isDepleted = false;

    public Item(String name) {
        this.name = name;
    }

}
