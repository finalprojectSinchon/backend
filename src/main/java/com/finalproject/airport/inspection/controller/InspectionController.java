package com.finalproject.airport.inspection.controller;

import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.inspection.dto.InspectionDTO;
import com.finalproject.airport.inspection.service.InspectionService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class InspectionController {


    private final InspectionService inspectionService;

    // 안전 점검 전체 조회
    @GetMapping("/inspection")
    public ResponseEntity<?> getInspection () {

        List<InspectionDTO> inspectionList = inspectionService.getInspectionList();

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"안전 점검 전체조회 성공", inspectionList));
    }

    //안전 점검 상세 조회
    @GetMapping("/inspection/{inspectionCode}")
    public ResponseEntity<?> getInspectionByStoreCode(@PathVariable int inspectionCode) {
        InspectionDTO inspection = inspectionService.getInspection(inspectionCode);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"안전 점검 상세 조회 성공", inspection ));
    }

    // 안전 점검 등록

    @PostMapping("/inspection")
    public ResponseEntity<?> addInspection(InspectionDTO inspectionDTO ) {
        try {
            inspectionService.addInspection(inspectionDTO);

            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.CREATED, "안전 점검 등록에 성공했습니다", null));

        }catch (Exception e){
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "오류가 발생하였습니다.", null));
        }

    }

    //안전 점검 수정

    @PutMapping("/inspection/{inspectionCode}")
    public ResponseEntity<?> updateInspection(@PathVariable int inspectionCode, @RequestBody InspectionDTO inspectionDTO) {
        try {
            inspectionService.updateInspection(inspectionCode, inspectionDTO);

            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.CREATED, "수정에 성공하셨습니다", null));


        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }

        //안전 점검 삭제
    }

    @PutMapping("/inspection/{inspectionCode}/delete")
    public ResponseEntity<?> deleteInspection(@PathVariable int inspectionCode) {
        try {
            inspectionService.softDeleteInspection(inspectionCode);

            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"삭제에 성공했습니다.", null));

        }catch (Exception e){
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, " 삭제에 실패했습니다.", null));

        }
    }
}
