package com.finalproject.airport.location.controller;

import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.location.dto.ZoneDTO;
import com.finalproject.airport.location.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping("/location/new")
    public ResponseEntity<?> newLocation() {

        locationService.newLocation();

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"위치 기본 정보 등록 성공",null));
    }

    @GetMapping("/location/region")
    public ResponseEntity<?> getRegion() {

        ResponseEntity<?> response = locationService.getRegion();

        return response;
    }

    @GetMapping("/location/region/{region}/floors")
    public ResponseEntity<?> getFloor(@PathVariable String region) {
        ResponseEntity<?> response = locationService.getFloor(region);
        return response;
    }

    @GetMapping("/location/floors/{region}/{floor}/locations")
    public ResponseEntity<?> getLocation(@PathVariable(value = "region") String region,
                                         @PathVariable(value = "floor") String floor) {

        ResponseEntity<?> response = locationService.getLocation(region,floor);

        return response;
    }

    @PostMapping("/location")
    public ResponseEntity<?> addLocation(@RequestBody ZoneDTO zone) {
        System.out.println("zone = " + zone);
        ResponseEntity<?> response = locationService.addLocation(zone);


        return response;
    }


    @GetMapping("/location/{type}/{code}")
    public ResponseEntity<?> getLocation(@PathVariable(value = "type") String type,
                                         @PathVariable(value = "code") int code) {

        ResponseEntity<?> response = locationService.getTypeOfLocation(type,code);

        return response;
    }

    @GetMapping("/location/storage")
    public ResponseEntity<?> getStorageLocation(){

        ResponseEntity<?> response = locationService.getStorageLocation();

        return response;
    }


}
