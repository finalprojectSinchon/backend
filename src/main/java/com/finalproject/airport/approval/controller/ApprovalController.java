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
    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    // 승인 전체 조회
    @GetMapping("/approve")
    public ResponseEntity<ResponseDTO> getApproval() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        List<ApprovalEntity> approvalEntityList = approvalService.findAll();
        System.out.println("approvalEntityList = " + approvalEntityList);
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
        boolean baggageClaimApproved = false;
        boolean storageApproved = false;
        boolean facilitiesApproved = false;
        String errorMessage = null;

        try {
            // 게이트 코드 승인처리
            approvalService.approveGate(approvalCode);
            gateApproved = true;
        } catch (RuntimeException e) {
            e.printStackTrace();
            errorMessage = e.getMessage();
        }

        try {
            // 체크인 카운트 승인처리
            approvalService.approveCheckInCounter(approvalCode);
            checkInCounterApproved = true;
        } catch (RuntimeException e) {
            e.printStackTrace();
            errorMessage = e.getMessage();
        }

        try {
            // 수하물 수취대 승인처리
            approvalService.approveBaggageClaim(approvalCode);
            baggageClaimApproved = true;
        } catch (RuntimeException e) {
            e.printStackTrace();
            errorMessage = e.getMessage();
        }

        try {
            // 창고 승인처리
            approvalService.approveStorage(approvalCode);
            storageApproved = true;
        } catch (RuntimeException e) {
            e.printStackTrace();
            errorMessage = e.getMessage();
        }

        try{
            // 편의시설 승인 처리
            approvalService.approveFacilities(approvalCode);
            facilitiesApproved = true;
        } catch (RuntimeException e){
            e.printStackTrace();
            errorMessage = e.getMessage();
        }

        boolean anyApproved = gateApproved || checkInCounterApproved || baggageClaimApproved || storageApproved || facilitiesApproved;
        HttpStatus status = anyApproved ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
        String message = anyApproved ? "승인 처리된 시설물은 다음과 같다 :  " : "승인 처리 실패";


        // 승인 처리 성공한 시설물 대한 메시지 추가
        if (gateApproved) message += " (Gate)";
        if (checkInCounterApproved) message += " (Check-in Counter)";
        if (baggageClaimApproved) message += " (Baggage Claim)";
        if (storageApproved) message += " (Storage)";
        if (facilitiesApproved) message += " (Facilities)";

        return ResponseEntity.status(status)
                .body(new ResponseDTO(status, message, errorMessage));
    }


    @GetMapping("/noti-checked")
    public void notiChecked(@RequestParam("approveCode") String approveCode){
        System.out.println("approveCode = " + approveCode);
        approvalService.notiChecked(Integer.parseInt(approveCode));

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



