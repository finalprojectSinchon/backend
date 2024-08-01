package com.finalproject.airport.maintenance.dto;

import com.finalproject.airport.airplane.baggageclaim.dto.BaggageClaimDTO;
import com.finalproject.airport.airplane.checkincounter.dto.CheckinCounterDTO;
import com.finalproject.airport.airplane.gate.dto.GateDTO;
import com.finalproject.airport.airplane.gate.entity.Gate;
import com.finalproject.airport.facilities.dto.FacilitiesDTO;
import com.finalproject.airport.maintenance.entity.MaintenanceStatus;
import com.finalproject.airport.storage.dto.StorageDTO;
import com.finalproject.airport.storage.entity.StorageEntity;
import com.finalproject.airport.store.dto.StoreDTO;
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

    private Integer quantity;
    private MaintenanceStatus status;
    private GateDTO gate;
    private CheckinCounterDTO checkinCounter;
    private BaggageClaimDTO baggageClaim;
    private StorageDTO storage;
    private FacilitiesDTO facilities;
    private StoreDTO store;

    private Integer gateCode;
    private Integer checkinCounterCode;
    private Integer baggageClaimCode;
    private Integer storageCode;
    private Integer facilitiesCode;
    private Integer storeCode;
}
