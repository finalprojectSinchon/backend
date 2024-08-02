package com.finalproject.airport.maintenance.controller;

import com.finalproject.airport.airplane.gate.dto.GateDTO;
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

import java.net.URI;
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
    public ResponseEntity<ResponseDTO> getMaintenance() {
        System.out.println("maintenance");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        List<MaintenanceDTO> maintenanceDTOList = maintenanceService.findAll();
        Map<String, Object> maintenanceMap = new HashMap<>();
        maintenanceMap.put("maintenanceList", maintenanceDTOList);
        System.out.println("maintenanceList: " + maintenanceDTOList);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseDTO(HttpStatus.OK,"정비 전체 조회", maintenanceMap));
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

    // 정비 수정
    @PutMapping("/maintenance/{maintenanceCode}")
    public ResponseEntity<?> modifyMaintenance(@PathVariable int maintenanceCode, @RequestBody MaintenanceDTO maintenanceDTO) {

        maintenanceService.updateMaintenance(maintenanceCode, maintenanceDTO);

        return ResponseEntity.created(URI.create("/gate" + maintenanceCode)).build();

    }

    // 정비 삭제
    @PutMapping("/maintenance/{maintenanceCode}/delete")
    public ResponseEntity<?> deleteMaintenance(@PathVariable int maintenanceCode) {
        System.out.println("maintenanceCode: " + maintenanceCode);

        maintenanceService.softDelete(maintenanceCode);

        return ResponseEntity.ok().build();
    }


    //정비 등록
    @PostMapping("/maintenance")
    public ResponseEntity<?> insertMaintenance(@RequestBody MaintenanceDTO maintenanceDTO) {
        System.out.println("maintenanceDTO = " + maintenanceDTO);
        maintenanceService.insertMaintenance(maintenanceDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/structure")
    public ResponseEntity<ResponseDTO> getStructureLocation(@RequestParam("structure") String structure) {

        System.out.println("structure = " + structure);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        List<Object> locationList = maintenanceService.findlocation(structure);
        Map<String, Object> response = new HashMap<>();
        response.put("locationList", locationList);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseDTO(HttpStatus.OK,"위치 조회", response));
    }
}
