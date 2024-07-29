package com.finalproject.airport.approval.controller;

import com.finalproject.airport.approval.entity.ApprovalEntity;
import com.finalproject.airport.approval.service.ApprovalService;
import com.finalproject.airport.common.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1")
public class ApprovalController {

    private final ApprovalService approvalService;

    @Autowired
    public ApprovalController(ApprovalService approvalService){
        this.approvalService = approvalService;
    }

    // 승인 전체 조회
    @GetMapping("/approve")
    public ResponseEntity<ResponseDTO> getApproval() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        List<ApprovalEntity> approvalEntityList = approvalService.findAll();
        Map<String, Object> approvalMap = new HashMap<>();
        approvalMap.put("approvalList", approvalEntityList);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseDTO(HttpStatus.OK, "승인 전체 조회 성공", approvalMap));
    }
    @PutMapping("/approve/{approvalCode}")
    public ResponseEntity<ResponseDTO> approve(@PathVariable Integer approvalCode) {
        if (approvalCode == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(HttpStatus.BAD_REQUEST, "Approval code must not be null", null));
        }

        boolean gateApproved = false;
        boolean checkInCounterApproved = false;

        try {
            // 게이트 코드 승인처리
            approvalService.approveGate(approvalCode);
            gateApproved = true;
        } catch (RuntimeException e) {
            e.printStackTrace();
            // 게이트 승인 처리 실패
        }

        try {
            // 체크인 카운트 승인처리
            approvalService.approveCheckInCounter(approvalCode);
            checkInCounterApproved = true;
        } catch (RuntimeException e) {
            e.printStackTrace();
            // 체크인 카운터 승인 처리 실패
        }

        try {
            // 수하물 수취대 승인처리
            approvalService.approveBaggageClaim(approvalCode);
            if (gateApproved && checkInCounterApproved) {
                return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK, "게이트, 체크인 카운터 및 수하물 수취대 승인 처리 성공", null));
            } else if (gateApproved) {
                return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK, "게이트 및 수하물 수취대 승인 처리 성공", null));
            } else if (checkInCounterApproved) {
                return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK, "체크인 카운터 및 수하물 수취대 승인 처리 성공", null));
            } else {
                return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK, "수하물 수취대 승인 처리 성공", null));
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            if (gateApproved || checkInCounterApproved) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "게이트 또는 체크인 카운터 승인 처리 성공, 수하물 수취대 승인 처리 실패", e.getMessage()));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "모든 승인 처리 실패", e.getMessage()));
            }
        }
    }




//    // 승인 isActive 를 N을 Y로 바꿔주기 11
//    @PutMapping("/approve/{approvalCode}")
//    public ResponseEntity<ResponseDTO> approve(@PathVariable Integer approvalCode) {
//        try {
//            approvalService.approve(approvalCode);
//            return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK, "승인 처리 성공", null));
//        } catch (RuntimeException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "승인 처리 실패", e.getMessage()));
//        }
//    }



    /* // 승인 isActive 를 N을 Y로 바꿔주기
    @PutMapping("/approve/{approvalCode}")
    public ResponseEntity<ResponseDTO> approve(@PathVariable Integer approvalCode) {
        try {
            approvalService.approve(approvalCode);
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK, "승인 처리 성공"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(HttpStatus.NOT_FOUND, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "처리 중 오류 발생"));
        }
    }*/

//    // 승인 상세 조회
//    @GetMapping("/approve/{approvalCode}")
//    public ResponseEntity<ResponseDTO> getApprovalByApprovalDetailInfo(@PathVariable int approvalCode) {
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
//        ApprovalDTO approvalDTO = approvalService.getApprovalById(approvalCode);
//
//        Map<String, Object> approvalMap = new HashMap<>();
//        approvalMap.put("approvalDetailInfo", approvalDTO);
//
//        return ResponseEntity.ok().headers(headers)
//                .body(new ResponseDTO(HttpStatus.OK,"승인 상세 조회 성공", approvalMap));
//    }


}
