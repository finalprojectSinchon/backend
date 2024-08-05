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

    /*
    *         if(facilities.store) {
            setNewFacility(facilities.store);
        } else if (facilities.baggageClaim) {
            setNewFacility(facilities.baggageClaim);
        } else if (facilities.gate) {
            setNewFacility(facilities.gate);
        } else if (facilities.checkInCounter) {
            setNewFacility(facilities.checkInCounter);
        } else if (facilities.showAll) {
            setNewFacility(facilities.showAll);
        } else if (facilities.storage) {
            setNewFacility(facilities.storage);
        } else if (facilities.facilities) {
            setNewFacility(facilities.facilities);
        }
    * */

    List<BaggageClaimQRDTO> baggageClaim;

    List<StoreQRDTO> store;

    List<GateQRDTO> gate;

    List<CheckInCounterQRDTO> checkInCounter;

    List<StorageQRDTO> storage;

    List<FacilityQRDTO> facilities;

}
