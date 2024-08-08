package com.finalproject.airport.QRCode.dto;

import com.finalproject.airport.airplane.baggageclaim.dto.BaggageClaimDTO;
import com.finalproject.airport.store.dto.StoreDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class QRAllDTO {

    List<BaggageClaimQRDTO> baggageClaim;

    List<StoreQRDTO> store;

    List<GateQRDTO> gate;

    List<CheckInCounterQRDTO> checkInCounter;

    List<StorageQRDTO> storage;

    List<FacilityQRDTO> facilities;

}
