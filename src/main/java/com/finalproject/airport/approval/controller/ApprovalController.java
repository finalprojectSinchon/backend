package com.finalproject.airport.approval.controller;

import com.finalproject.airport.approval.dto.ApprovalDTO;
import com.finalproject.airport.approval.entity.ApprovalEntity;
import com.finalproject.airport.approval.service.ApprovalService;
import com.finalproject.airport.common.ResponseDTO;
import org.aspectj.lang.annotation.AfterThrowing;
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

    // 승인 isActive 를 N을 Y로 바꿔주기
    @PutMapping("/approve/{approvalCode}")
    public ResponseEntity<ResponseDTO> approve(@PathVariable Integer approvalCode) {
        if (approvalCode == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDTO(HttpStatus.BAD_REQUEST, "Approval code must not be null", null));
        }

        try {
            approvalService.approve(approvalCode);
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK, "승인 처리 성공", null));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "승인 처리 실패", e.getMessage()));
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
