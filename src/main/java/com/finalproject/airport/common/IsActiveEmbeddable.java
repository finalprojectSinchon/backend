package com.finalproject.airport.common;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class IsActiveEmbeddable {

    @Column(name = "ISACTIVE")
    private String isActive;

}
