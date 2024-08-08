package com.finalproject.airport.approval.dto;

import com.finalproject.airport.airplane.baggageclaim.dto.BaggageClaimDTO;
import com.finalproject.airport.airplane.checkincounter.dto.CheckinCounterDTO;
import com.finalproject.airport.airplane.gate.dto.GateDTO;
import com.finalproject.airport.approval.entity.ApprovalStatusEntity;
import com.finalproject.airport.approval.entity.ApprovalTypeEntity;
import com.finalproject.airport.facilities.dto.FacilitiesDTO;
import com.finalproject.airport.storage.dto.StorageDTO;
import com.finalproject.airport.store.dto.StoreDTO;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ApprovalDTO {

    private int approvalCode;
    private ApprovalTypeEntity type;
    private ApprovalStatusEntity status;
    private GateDTO gateDTO;
    private CheckinCounterDTO checkinCounterDTO;
    private BaggageClaimDTO baggageClaimDTO;
    private StoreDTO storeDTO;
    private StorageDTO storageDTO;
    private FacilitiesDTO facilitiesDTO;
    private Integer gateCode;
    private Integer checkinCounterCode;
    private Integer baggageClaimCode;
    private Integer storageCode;
    private Integer facilitiesCode;

    private Integer code;

    private Date createdDate;
    private Date modifiedDate;
    private String checked;

    public ApprovalDTO(ApprovalTypeEntity type, ApprovalStatusEntity status, Integer gatecode ,Integer checkincountercode,Integer baggageclaimcode, Integer storageCode, Integer facilitiesCode) {
        this.type = type;
        this.status = status;
        this.gateCode = gatecode;
        this.checkinCounterCode = checkincountercode;
        this.baggageClaimCode = baggageclaimcode;
        this.storageCode = storageCode;
        this.facilitiesCode = facilitiesCode;

    }

    public ApprovalDTO(ApprovalTypeEntity type, ApprovalStatusEntity status, Integer gatecode ,Integer checkincountercode,Integer baggageclaimcode, Integer storageCode, Integer facilitiesCode, Integer code) {
        this.type = type;
        this.status = status;
        this.gateCode = gatecode;
        this.checkinCounterCode = checkincountercode;
        this.baggageClaimCode = baggageclaimcode;
        this.storageCode = storageCode;
        this.facilitiesCode = facilitiesCode;
        this.code = code;

    }

}