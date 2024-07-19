package com.finalproject.airport.airplane.gate.controller;

import com.finalproject.airport.airplane.gate.dto.GateDTO;
import com.finalproject.airport.airplane.gate.service.GateService;
import com.finalproject.airport.common.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/airplane")
public class GateController {

    private final GateService gateService;

    @Autowired
    public GateController(GateService gateService){
        this.gateService = gateService;
    }

    // 탑승구 등록
    @PostMapping("/gate")
    public ResponseEntity<?> insertGate(@ModelAttribute GateDTO gate){

        gateService.insertGate(gate);

        return ResponseEntity.ok().build();
    }

    // 탑승구 전체 조회
    @GetMapping("/gate")
    public ResponseEntity<ResponseDTO> getGate(){

        System.out.println("gate");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        List<GateDTO> gateList = gateService.findAll();
        Map<String,Object> responseMap = new HashMap<>();
        responseMap.put("gateList",gateList);

        System.out.println("gateList:"+gateList);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseDTO(HttpStatus.OK,"탑승구 전체 조회 ",responseMap));

    }

    // 탑승구 상세 조회
    @GetMapping("/gate/{gateCode}")
    public ResponseEntity<ResponseDTO> gateDetail(@PathVariable int gateCode){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        GateDTO gate = gateService.findBygateCode(gateCode);

        Map<String,Object> responseMap = new HashMap<>();
        responseMap.put("gate",gate);

        return ResponseEntity.ok().headers(headers)
                .body(new ResponseDTO(HttpStatus.OK,"탑승구 상세 조회",responseMap));

    }


    // 탑승구 수정
    @PutMapping("/gate/{gateCode}")
    public ResponseEntity<?> modifyGate(@PathVariable int gateCode, @RequestBody GateDTO modifyGate){

        System.out.println("modifyGate "+modifyGate);

        gateService.modifyGate(gateCode,modifyGate);

        return ResponseEntity.created(URI.create("/gate/"+gateCode)).build();
    }

    // 탑승구 soft delete
    @PutMapping("/gate/{gateCode}/delete")
    public ResponseEntity<?> remodveGate(@PathVariable int gateCode){


        gateService.softDelete(gateCode);

        return ResponseEntity.ok().build();

    }






}
