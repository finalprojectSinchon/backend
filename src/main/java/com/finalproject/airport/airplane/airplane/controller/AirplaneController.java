package com.finalproject.airport.airplane.airplane.controller;

import com.finalproject.airport.airplane.airplane.DTO.AirplaneDTO;
import com.finalproject.airport.airplane.airplane.service.AirPlaneService;
import com.finalproject.airport.airplane.baggageclaim.dto.BaggageClaimDTO;
import com.finalproject.airport.common.ResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "비행기 기능")
@RestController
@RequestMapping("/api/v1")
public class AirplaneController {

    private final AirPlaneService airPlaneService;

    @Autowired
    public AirplaneController(AirPlaneService airPlaneService) {
        this.airPlaneService = airPlaneService;
    }

    // 비행기 전체 조회
    @Operation(summary = "비행기 조회",description = "비행기 전체 목록 조회")
    @GetMapping("/airplane")
    public ResponseEntity<ResponseDTO> getAirplane() {

//        airPlaneService.fetchArrivalAirplane();
//        airPlaneService.fetchDepartureAirplane();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        List<AirplaneDTO> airplaneList = airPlaneService.findAll();
        Map<String,Object> responseMap = new HashMap<>();
        responseMap.put("airplaneList",airplaneList);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseDTO(HttpStatus.OK,"비행기 전체 조회 ",responseMap));

    }

//    @Operation(summary = "항공 정보만 가져오기 " , description = "항공 정보만")
//    @GetMapping("/airplanedata")
//    public ResponseEntity<ResponseDTO> getairdata() {
//
//
//    }



    @Operation(summary = "비행기 api 불러오기" ,description = "비행기 api")
    @GetMapping("/apiair")
    public ResponseEntity<ResponseDTO> getapi() {

        airPlaneService.fetchArrivalAirplane();

        return ResponseEntity.ok()
                .body(new ResponseDTO(HttpStatus.OK,"비행기 api 조회 ",null));

    }

    // 비행기 상세 조회
    @Operation(summary = "비행기 조회" , description = "비행기 상세 정보 조회"
            ,parameters = {@Parameter(name = "airplaneCode",description = "사용자 화면에서 넘어오는 airplane 의 pk")})
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

    // 비행기 수정
    @Operation(summary = "비행기 수정",description = "비행기 정보 수정"
            ,parameters = {@Parameter(name = "airplaneCode",description = "사용자 화면에서 넘어오는 airplane 의 pk")})
    @PutMapping("/airplane/{airplaneCode}")
    public ResponseEntity<?> modifybAirplane(@PathVariable int airplaneCode, @RequestBody AirplaneDTO modifyairplane){


        airPlaneService.modifybAirplane(airplaneCode,modifyairplane);

        return ResponseEntity.created(URI.create("/airplane//"+airplaneCode)).build();
    }

    // 비행기 soft delete
    @Operation(summary = "비행기 삭제",description = "비행기 삭제 soft delete"
            ,parameters = {@Parameter(name = "airplaneCode",description = "사용자 화면에서 넘어오는 airplane 의 pk")})
    @PutMapping("/airplane/{airplaneCode}/delete")
    public ResponseEntity<?> remodveBaggageClaimCode(@PathVariable int airplaneCode){


        airPlaneService.softDelete(airplaneCode);

        return ResponseEntity.ok().build();

    }

    @GetMapping("/airplane/test")
    public ResponseEntity<?> getAirplaneAPI() {

        airPlaneService.fetchDepartureAirplane();

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"api 요청 성공",null));
    }
}
