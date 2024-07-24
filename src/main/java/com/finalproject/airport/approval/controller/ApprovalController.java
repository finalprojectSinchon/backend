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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // 승인 상세 조회
    @GetMapping("/approve/{approvalCode}")
    public ResponseEntity<ResponseDTO> getApprovalByApprovalDetailInfo(@PathVariable int approvalCode) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        ApprovalDTO approvalDTO = approvalService.getApprovalById(approvalCode);

        Map<String, Object> approvalMap = new HashMap<>();
        approvalMap.put("approvalDetailInfo", approvalDTO);

        return ResponseEntity.ok().headers(headers)
                .body(new ResponseDTO(HttpStatus.OK,"승인 상세 조회 성공", approvalMap));
    }


}
