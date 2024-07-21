package com.finalproject.airport.airplane.airplane.controller;

import com.finalproject.airport.airplane.airplane.DTO.AirplaneDTO;
import com.finalproject.airport.airplane.airplane.service.AirPlaneService;
import com.finalproject.airport.airplane.baggageclaim.dto.BaggageClaimDTO;
import com.finalproject.airport.common.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class AirplaneController {

    private final AirPlaneService airPlaneService;

    @Autowired
    public AirplaneController(AirPlaneService airPlaneService) {
        this.airPlaneService = airPlaneService;
    }

    // 비행기 전체 조회
    @GetMapping("/airplane")
    public ResponseEntity<ResponseDTO> getAirplane() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        List<AirplaneDTO> airplaneList = airPlaneService.findAll();
        Map<String,Object> responseMap = new HashMap<>();
        responseMap.put("airplaneList",airplaneList);


        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseDTO(HttpStatus.OK,"비행기 전체 조회 ",responseMap));

    }

    // 비행기 상세 조회
    @GetMapping("/airplane/{airplaneCode}")
    public ResponseEntity<ResponseDTO> airplaneDetail(@PathVariable int airplaneCode){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        AirplaneDTO airplane = airPlaneService.findByairplaneCode(airplaneCode);

        Map<String,Object> responseMap = new HashMap<>();
        responseMap.put("airplane",airplane);

        return ResponseEntity.ok().headers(headers)
                .body(new ResponseDTO(HttpStatus.OK,"비행기 상세 조회",responseMap));

    }
}
