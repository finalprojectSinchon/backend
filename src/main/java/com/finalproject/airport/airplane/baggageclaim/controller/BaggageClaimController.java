package com.finalproject.airport.airplane.baggageclaim.controller;

import com.finalproject.airport.airplane.baggageclaim.dto.BaggageClaimDTO;
import com.finalproject.airport.airplane.baggageclaim.service.BaggageClaimService;
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

@Tag(name = "수화물 수취대 기능")
@RestController
@RequestMapping("/api/v1/airplane")
public class BaggageClaimController {

    private final BaggageClaimService service;

    @Autowired
    public BaggageClaimController(BaggageClaimService service){
        this.service = service;
    }

    // 수화물 수취대 전체 조회
    @Operation(summary = "수화물 수취대 조회", description = "수화물 수취대 전체 목록 조회")
    @GetMapping("/baggage-claim")
    public ResponseEntity<ResponseDTO> getGate() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        List<BaggageClaimDTO> baggageClaimdata = service.baggageClaimFetch();
        List<BaggageClaimDTO> baggageClaimList = service.findAll();
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("baggageClaimList", baggageClaimList);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseDTO(HttpStatus.OK, "수화물 수취대 전체 조회", responseMap));
    }

    // 수화물 수취대 상세 조회
    @Operation(summary = "수화물 수취대 상세 조회", description = "수화물 수취대 상세 정보 조회",
            parameters = {@Parameter(name = "baggageClaimCode", description = "사용자 화면에서 넘어오는 수화물 수취대의 pk")})
    @GetMapping("/baggage-claim/{baggageClaimCode}")
    public ResponseEntity<ResponseDTO> baggageClaimDetail(@PathVariable int baggageClaimCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        BaggageClaimDTO baggageClaim = service.findBybaggageClaimCode(baggageClaimCode);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("baggageClaim", baggageClaim);

        return ResponseEntity.ok().headers(headers)
                .body(new ResponseDTO(HttpStatus.OK, "수화물 수취대 상세 조회", responseMap));
    }

    // 수화물 수취대 등록
    @Operation(summary = "수화물 수취대 등록", description = "새로운 수화물 수취대를 등록")
    @PostMapping("/baggage-claim")
    public ResponseEntity<?> insertbaggageClaim(@RequestBody BaggageClaimDTO baggageClaim) {
        service.insertBaggageClaim(baggageClaim);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "수화물 수취대 수정", description = "수화물 수취대 정보를 수정",
            parameters = {@Parameter(name = "baggageClaimCode", description = "사용자 화면에서 넘어오는 수화물 수취대의 pk")})
    @PutMapping("/baggage-claim/{baggageClaimCode}")
    public ResponseEntity<?> modifyBaggageClaim(@RequestBody BaggageClaimDTO modifyBaggageClaimDTO) {
        System.out.println("컨트롤러 = " + modifyBaggageClaimDTO);
        try {
            // 서비스에서 수정 및 승인 처리
            String resultMessage = service.modifyBaggageClaim( modifyBaggageClaimDTO);
            // 성공적으로 수정되었음을 나타내는 HTTP 200 OK 응답 반환
            return ResponseEntity.ok(resultMessage);
        } catch (RuntimeException e) {
            // 수정 또는 승인 처리 중 오류 발생 시 HTTP 500 Internal Server Error 응답 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }




    // 수화물 수취대 soft delete
    @Operation(summary = "수화물 수취대 삭제", description = "수화물 수취대 soft delete",
            parameters = {@Parameter(name = "baggageClaimCode", description = "사용자 화면에서 넘어오는 수화물 수취대의 pk")})
    @PutMapping("/baggage-claim/{baggageClaimCode}/delete")
    public ResponseEntity<?> remodveBaggageClaimCode(@PathVariable int baggageClaimCode) {
        service.softDelete(baggageClaimCode);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/baggage/test")
    public void test() {
          service.insertdb();
    }
}
