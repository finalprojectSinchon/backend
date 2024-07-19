package com.finalproject.airport.airplane.baggageclaim.controller;

import com.finalproject.airport.airplane.baggageclaim.dto.BaggageClaimDTO;
import com.finalproject.airport.airplane.baggageclaim.service.BaggageClaimService;
import com.finalproject.airport.airplane.gate.dto.GateDTO;
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
@RequestMapping("/api/v1/airplane")
public class BaggageClaimController {

    private final BaggageClaimService service;

    @Autowired
    public BaggageClaimController(BaggageClaimService service){
        this.service = service;
    }

    // 수화물 수취대 전체 조회
    @GetMapping("/baggage-claim")
    public ResponseEntity<ResponseDTO> getGate(){


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        List<BaggageClaimDTO> baggageClaimList = service.findAll();
        Map<String,Object> responseMap = new HashMap<>();
        responseMap.put("baggageClaimList",baggageClaimList);


        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseDTO(HttpStatus.OK,"수화물 수취대 전체 조회 ",responseMap));

    }

    // 탑승구 상세 조회
    @GetMapping("baggage-claim/{baggageClaimCode}")
    public ResponseEntity<ResponseDTO> gateDetail(@PathVariable int baggageClaimCode){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        BaggageClaimDTO baggageClaim = service.findBybaggageClaimCode(baggageClaimCode);

        Map<String,Object> responseMap = new HashMap<>();
        responseMap.put("baggageClaim",baggageClaim);

        return ResponseEntity.ok().headers(headers)
                .body(new ResponseDTO(HttpStatus.OK,"수화물 수취대 상세 조회",responseMap));

    }



}
