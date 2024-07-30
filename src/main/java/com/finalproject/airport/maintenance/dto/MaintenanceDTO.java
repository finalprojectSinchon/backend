package com.finalproject.airport.maintenance.dto;

import com.finalproject.airport.maintenance.entity.MaintenanceStatus;
import lombok.*;

import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class MaintenanceDTO {

    private int maintenanceCode;

    private String structure;

    private String type;

    private String location;

    private Date maintenanceStartDate;

    private Date maintenanceEndDate;

    private String equipment;

    private long number;

    private int expense;

    private String maintenanceDetails;

    private String manager;

    private MaintenanceStatus status;
}
