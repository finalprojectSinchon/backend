package com.finalproject.airport.maintenance.controller;

import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.maintenance.dto.MaintenanceDTO;
import com.finalproject.airport.maintenance.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
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
public class MaintenanceController {


    private final MaintenanceService maintenanceService;

    @Autowired
    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    //정비 전체 조회
    @GetMapping("/maintenance")
    public ResponseEntity<?> getMaintenance() {
        List<MaintenanceDTO> maintenanceDTOList = maintenanceService.getMaintenanceList();
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "정비 전체 조회 성공", maintenanceDTOList));
    }

    //정비 상세 조회
    @GetMapping("/maintenance/{maintenanceCode}")
    public ResponseEntity<ResponseDTO> getMaintenanceDetailInfo(@PathVariable int maintenanceCode) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        MaintenanceDTO maintenanceDTO = maintenanceService.getMaintenanceById(maintenanceCode);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("maintenanceDTO", maintenanceDTO);

        return ResponseEntity.ok().headers(headers)
                .body(new ResponseDTO(HttpStatus.OK,"정비 상세 조회 성공", responseMap));
    }



//    //정비 등록
//    @PostMapping("/maintenance")
//    public ResponseEntity<?> addMaintenance(@RequestBody MaintenanceDTO maintenanceDTO) {
//        MaintenanceDTO createdMaintenanceDTO = maintenanceService.addMaintenance(maintenanceDTO);
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(new ResponseDTO(HttpStatus.CREATED, "정비 등록 성공", createdMaintenanceDTO));
//    }
//
//    // 정비 수정
//    @PutMapping("/maintenance/{maintenanceCode}")
//    public ResponseEntity<?> updateMaintenance(@PathVariable int maintenanceCode, @RequestBody MaintenanceDTO maintenanceDTO) {
//        MaintenanceDTO updatedMaintenanceDTO = maintenanceService.updateMaintenance(maintenanceCode, maintenanceDTO);
//        if (updatedMaintenanceDTO != null) {
//            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "정비 수정 성공", updatedMaintenanceDTO));
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ResponseDTO(HttpStatus.NOT_FOUND, "해당 코드의 정비 항목을 찾을 수 없습니다.", null));
//        }
//    }
//
//    // 정비 삭제
//    @PutMapping("/maintenance/{maintenanceCode}/delete")
//    public ResponseEntity<?> deleteMaintenance(@PathVariable int maintenanceCode) {
//        boolean isDeleted = maintenanceService.deleteMaintenance(maintenanceCode);
//        if (isDeleted) {
//            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "정비 삭제 성공", null));
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ResponseDTO(HttpStatus.NOT_FOUND, "해당 코드의 정비 항목을 찾을 수 없습니다.", null));
//        }
//    }

}
