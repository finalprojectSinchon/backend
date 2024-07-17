package com.finalproject.airport.maintenance.controller;

import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.maintenance.dto.MaintenanceDTO;
import com.finalproject.airport.maintenance.service.MaintenanceService;
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
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @GetMapping("/maintenance")
    public ResponseEntity<?> getMaintenance() {
        List<MaintenanceDTO> maintenanceDTOList = maintenanceService.getMaintenanceList();
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "정비 전체 조회 성공", maintenanceDTOList));
    }

    @GetMapping("/maintenance/{maintenanceCode}")
    public ResponseEntity<?> getMaintenanceById(@PathVariable int maintenanceCode) {
        MaintenanceDTO maintenanceDTO = maintenanceService.getMaintenanceById(maintenanceCode);
        if (maintenanceDTO != null) {
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "정비 상세 조회 성공", maintenanceDTO));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(HttpStatus.NOT_FOUND, "해당 코드의 정비 항목을 찾을 수 없습니다.", null));
        }
    }
}
