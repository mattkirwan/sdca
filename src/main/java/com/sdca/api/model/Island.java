package com.sdca.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Island {

    @GeneratedValue @Id
    private Long id;

    public Island(Byte x, Byte y) {
        this.x = x;
        this.y = y;
    }

    @Getter @Setter
    private Byte x;

    @Getter @Setter
    private Byte y;

}
