package com.finalproject.airport.airplane.gate.controller;

import com.finalproject.airport.airplane.gate.dto.GateDTO;
import com.finalproject.airport.airplane.gate.service.GateService;
import com.finalproject.airport.common.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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

@Tag(name = "탑승구 기능", description = "탑승구 관련 API")
@RestController
@RequestMapping("/api/v1/airplane")
public class GateController {

    private final GateService gateService;

    @Autowired
    public GateController(GateService gateService){
        this.gateService = gateService;
    }

    // 탑승구 등록
    @Operation(summary = "탑승구 등록", description = "새로운 탑승구를 등록합니다.")
    @PostMapping("/gate")
    public ResponseEntity<?> insertGate(@RequestBody GateDTO gate){
        System.out.println("gate = " + gate);
        gateService.insertGate(gate);
        return ResponseEntity.ok().build();
    }

    // 탑승구 전체 조회
    @Operation(summary = "탑승구 전체 조회", description = "모든 탑승구의 목록을 조회합니다.")
    @GetMapping("/gate")
    public ResponseEntity<ResponseDTO> getGate(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
        List<GateDTO> gatedata = gateService.fetch();
        List<GateDTO> gateList = gateService.findAll();
        System.out.println("여행간당"+gateList);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("gateList", gateList);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseDTO(HttpStatus.OK, "탑승구 전체 조회", responseMap));
    }

    @Operation(summary = "탑승구 1구역 조회", description = "탑승구 1구역 목록을 조회합니다.")
    @GetMapping("/gate1")
    public ResponseEntity<ResponseDTO> getGate1(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
        List<GateDTO> gateList1 = gateService.gateList1();

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("gateList", gateList1);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseDTO(HttpStatus.OK, "탑승구 1구역 조회", responseMap));
    }
    @Operation(summary = "탑승구 2구역 조회", description = "탑승구 2구역 목록을 조회합니다.")
    @GetMapping("/gate2")
    public ResponseEntity<ResponseDTO> getGate2(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
        List<GateDTO> gateList2 = gateService.gateList2();

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("gateList", gateList2);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseDTO(HttpStatus.OK, "탑승구 2구역 조회", responseMap));
    }

    @Operation(summary = "탑승구 3구역 조회", description = "탑승구 3구역 목록을 조회합니다.")
    @GetMapping("/gate3")
    public ResponseEntity<ResponseDTO> getGate3(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
        List<GateDTO> gateList3 = gateService.gateList3();

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("gateList", gateList3);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseDTO(HttpStatus.OK, "탑승구 3구역 조회", responseMap));
    }
    // 탑승구 상세 조회
    @Operation(summary = "탑승구 상세 조회", description = "특정 탑승구의 세부 정보를 조회합니다.",
            parameters = {@Parameter(name = "gateCode", description = "조회할 탑승구의 코드")})
    @GetMapping("/gate/{gateCode}")
    public ResponseEntity<ResponseDTO> gateDetail(@PathVariable int gateCode){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        GateDTO gate = gateService.findByGateCode(gateCode);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("gate", gate);

        return ResponseEntity.ok().headers(headers)
                .body(new ResponseDTO(HttpStatus.OK, "탑승구 상세 조회", responseMap));
    }

    // 탑승구 수정
    @Operation(summary = "탑승구 수정", description = "특정 탑승구의 정보를 수정합니다.",
            parameters = {@Parameter(name = "gateCode", description = "수정할 탑승구의 코드")})
    @PutMapping("/gate/{gateCode}")
    public ResponseEntity<?> modifyGate(@RequestBody GateDTO modifyGateDTO){
        try {
            String resultMessage = gateService.modifyGate(modifyGateDTO);
            return ResponseEntity.ok(resultMessage);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    // 탑승구 soft delete
    @Operation(summary = "탑승구 삭제", description = "특정 탑승구를 soft delete 합니다.",
            parameters = {@Parameter(name = "gateCode", description = "soft delete 할 탑승구의 코드")})
    @PutMapping("/gate/{gateCode}/delete")
    public ResponseEntity<?> remodveGate(@PathVariable int gateCode){
        gateService.softDelete(gateCode);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/gate/test")
    public void getTest() {
        gateService.getTest();
    }
}
