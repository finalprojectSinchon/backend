package com.finalproject.airport.inspection.controller;

import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.inspection.dto.InspectionDTO;
import com.finalproject.airport.inspection.service.InspectionService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor


public class InspectionController {

    private final InspectionService inspectionService;

    @GetMapping("/inspection")
    public ResponseEntity<?> getInspection () {

        List<InspectionDTO> inspectionList = inspectionService.getInspectionList();

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"안전 점검 전체조회 성공", inspectionList));
    }

    @GetMapping("/inspection/{inspectionCode}")
    public ResponseEntity<?> getInspectionByStoreCode(@PathVariable String inspectionCode) {
        InspectionDTO inspection = inspectionService.getInspection(inspectionCode);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"안전 점검 조회 성공", inspection ));
    }

}
