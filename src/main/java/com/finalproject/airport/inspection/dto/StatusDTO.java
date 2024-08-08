package com.finalproject.airport.inspection.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StatusDTO {

    private String structure;
    private String status;
    private Long count;

}
