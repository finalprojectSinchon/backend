package com.finalproject.airport.approval.dto;

import com.finalproject.airport.airplane.baggageclaim.dto.BaggageClaimDTO;
import com.finalproject.airport.airplane.checkincounter.dto.CheckinCounterDTO;
import com.finalproject.airport.airplane.gate.dto.GateDTO;
import com.finalproject.airport.approval.entity.ApprovalStatus;
import com.finalproject.airport.approval.entity.ApprovalType;
import com.finalproject.airport.store.dto.StoreDTO;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ApprovalDTO {

    private int approvalCode;
    private ApprovalType approvalType;
    private ApprovalStatus approvalStatus;
    private GateDTO gateDTO;
    private CheckinCounterDTO checkinCounterDTO;
    private BaggageClaimDTO baggageClaimDTO;
    private StoreDTO storeDTO;


}
