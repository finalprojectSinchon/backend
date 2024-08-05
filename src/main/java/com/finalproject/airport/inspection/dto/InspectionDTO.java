package com.finalproject.airport.inspection.dto;

import com.finalproject.airport.airplane.baggageclaim.dto.BaggageClaimDTO;
import com.finalproject.airport.airplane.checkincounter.dto.CheckinCounterDTO;
import com.finalproject.airport.airplane.gate.dto.GateDTO;
import com.finalproject.airport.facilities.dto.FacilitiesDTO;
import com.finalproject.airport.storage.dto.StorageDTO;
import com.finalproject.airport.store.dto.StoreDTO;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class InspectionDTO {

    private Integer inspectionCode;

    private String manager;

    private String location;

    private Date regularInspectionDate;

    private String type;

    private String status;

    private Integer phone;

    private String text;

    private String isActive;

    private String structure;

    private GateDTO gate;
    private CheckinCounterDTO checkinCounter;
    private BaggageClaimDTO baggageClaim;
    private StorageDTO storage;
    private FacilitiesDTO facilities;
    private StoreDTO store;
}
