package com.finalproject.airport.facilities.controller;

import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.facilities.dto.FacilitiesDTO;
import com.finalproject.airport.facilities.service.FacilitiesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/v1")
@Slf4j
public class FacilitiesController {
    @Autowired
    private final FacilitiesService facilitiesService;

    public FacilitiesController(FacilitiesService facilitiesService) {this.facilitiesService = facilitiesService;}
    @GetMapping("/facilities")
    public ResponseEntity<?> selectFacilitiesList() {

        List<FacilitiesDTO> facilitiesList = facilitiesService.selectAllFacilities();

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "편의 시설 목록 조회 성공", facilitiesList));
    }

    @GetMapping("/facilities/{facilitiesCode}")
    public ResponseEntity<?> selectByFacilitiesCode(@PathVariable String facilitiesCode) {
        FacilitiesDTO facilities = facilitiesService.getfailties(facilitiesCode);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK ,"편의 시설 상세목록 조회" , facilities));
    }


}
