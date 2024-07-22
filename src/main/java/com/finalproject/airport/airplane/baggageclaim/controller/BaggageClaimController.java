package com.finalproject.airport.airplane.baggageclaim.controller;

import com.finalproject.airport.airplane.baggageclaim.dto.BaggageClaimDTO;
import com.finalproject.airport.airplane.baggageclaim.service.BaggageClaimService;
import com.finalproject.airport.common.ResponseDTO;
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

    // 수화물 수취대 상세 조회
    @GetMapping("/baggage-claim/{baggageClaimCode}")
    public ResponseEntity<ResponseDTO> baggageClaimDetail(@PathVariable int baggageClaimCode){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        BaggageClaimDTO baggageClaim = service.findBybaggageClaimCode(baggageClaimCode);

        Map<String,Object> responseMap = new HashMap<>();
        responseMap.put("baggageClaim",baggageClaim);

        return ResponseEntity.ok().headers(headers)
                .body(new ResponseDTO(HttpStatus.OK,"수화물 수취대 상세 조회",responseMap));

    }

    // 수화물 수취대 등록
    @PostMapping("/baggage-claim")
    public ResponseEntity<?> insertbaggageClaim(@RequestBody BaggageClaimDTO baggageClaim){

        service.insertBaggageClaim(baggageClaim);

        return ResponseEntity.ok().build();
    }

    // 수화물 수취대 수정
    @PutMapping("/baggage-claim/{baggageClaimCode}")
    public ResponseEntity<?> modifybaggageClaim(@PathVariable int baggageClaimCode, @RequestBody BaggageClaimDTO modifybaggageClaim){


        service.modifybaggageClaim(baggageClaimCode,modifybaggageClaim);

        return ResponseEntity.created(URI.create("/baggage-claim/"+baggageClaimCode)).build();
    }

    // 수화물 수취대 soft delete
    @PutMapping("/baggage-claim/{baggageClaimCode}/delete")
    public ResponseEntity<?> remodveBaggageClaimCode(@PathVariable int baggageClaimCode){


        service.softDelete(baggageClaimCode);

        return ResponseEntity.ok().build();

    }




}
