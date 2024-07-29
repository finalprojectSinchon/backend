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

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ApprovalDTO {

    private int approvalCode;
    private ApprovalTypeEntity approvalType;
    private ApprovalStatusEntity approvalStatus;
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

    public ApprovalDTO(ApprovalTypeEntity approvalType, ApprovalStatusEntity approvalStatus, Integer gatecode ,Integer checkincountercode,Integer baggageclaimcode, Integer storageCode) {
        this.approvalType = approvalType;
        this.approvalStatus = approvalStatus;
        this.gateCode = gatecode;
        this.checkinCounterCode = checkincountercode;
        this.baggageClaimCode = baggageclaimcode;
        this.storageCode = storageCode;
    }

}
