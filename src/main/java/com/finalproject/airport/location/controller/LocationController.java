package com.finalproject.airport.location.controller;

import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.location.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
