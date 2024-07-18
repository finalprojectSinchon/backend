package com.finalproject.airport.airplane.checkincounter.controller;


import com.finalproject.airport.airplane.checkincounter.dto.CheckinCounterDTO;
import com.finalproject.airport.airplane.checkincounter.service.CheckinCounterService;
import com.finalproject.airport.airplane.gate.dto.GateDTO;
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
public class CheckinCounterController {

    private final CheckinCounterService service;

    @Autowired
    public CheckinCounterController(CheckinCounterService service){
        this.service = service;
    }

    // 체크인카운터 등록
    @PostMapping("/checkin-counter")
    public ResponseEntity<?> insertchkinCounter(@ModelAttribute CheckinCounterDTO chkinCounter){

        service.insertchkinCounter(chkinCounter);

        return ResponseEntity.ok().build();
    }

    // 체크인카운터 전체 조회
    @GetMapping("/checkin-counter")
    public ResponseEntity<ResponseDTO> getChkinCounter(){


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        List<CheckinCounterDTO> chkinCounterList = service.findAll();
        Map<String,Object> responseMap = new HashMap<>();
        responseMap.put("chkinCounterList",chkinCounterList);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseDTO(HttpStatus.OK,"체크인카운터 전체 조회 ",responseMap));

    }

    // 체크인카운터 상세 조회
    @GetMapping("/checkin-counter/{checkinCounterCode}")
    public ResponseEntity<ResponseDTO> chkinCounterDetail(@PathVariable int checkinCounterCode){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        CheckinCounterDTO chkinCounter = service.findBycheckinCounterCode(checkinCounterCode);

        Map<String,Object> responseMap = new HashMap<>();
        responseMap.put("chkinCounter",chkinCounter);

        return ResponseEntity.ok().headers(headers)
                .body(new ResponseDTO(HttpStatus.OK,"체크인카운터 조회",responseMap));

    }



}
