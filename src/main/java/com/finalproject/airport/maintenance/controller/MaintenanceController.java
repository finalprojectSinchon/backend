package com.finalproject.airport.maintenance.controller;

import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.maintenance.dto.MaintenanceDTO;
import com.finalproject.airport.maintenance.dto.MaintenanceEquipmentDTO;
import com.finalproject.airport.maintenance.service.MaintenanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @Operation(summary = "정비 전체 조회", description = "모든 정비의 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정비 전체 조회 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    @GetMapping("/maintenance")
    public ResponseEntity<ResponseDTO> getMaintenance() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        List<MaintenanceDTO> maintenanceDTOList = maintenanceService.findAll();
        Map<String, Object> maintenanceMap = new HashMap<>();
        maintenanceMap.put("maintenanceList", maintenanceDTOList);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseDTO(HttpStatus.OK, "정비 전체 조회", maintenanceMap));
    }

    @Operation(summary = "정비 상세 조회", description = "정비 코드를 사용하여 특정 정비의 세부 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정비 상세 조회 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "정비를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    @GetMapping("/maintenance/{maintenanceCode}")
    public ResponseEntity<ResponseDTO> getMaintenanceDetailInfo(@PathVariable int maintenanceCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        MaintenanceDTO maintenanceDTO = maintenanceService.getMaintenanceById(maintenanceCode);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("maintenanceDTO", maintenanceDTO);

        return ResponseEntity.ok().headers(headers)
                .body(new ResponseDTO(HttpStatus.OK, "정비 상세 조회 성공", responseMap));
    }

    @Operation(summary = "정비 수정", description = "기존 정비 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "정비 수정 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    @PutMapping("/maintenance/{maintenanceCode}")
    public ResponseEntity<Void> modifyMaintenance(@PathVariable int maintenanceCode, @RequestBody MaintenanceDTO maintenanceDTO) {
        maintenanceService.updateMaintenance(maintenanceCode, maintenanceDTO);
        return ResponseEntity.created(URI.create("/maintenance/" + maintenanceCode)).build();
    }

    @Operation(summary = "정비 삭제", description = "정비를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정비 삭제 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "정비를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    @PutMapping("/maintenance/{maintenanceCode}/delete")
    public ResponseEntity<Void> deleteMaintenance(@PathVariable int maintenanceCode) {
        maintenanceService.softDelete(maintenanceCode);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "정비 등록", description = "새로운 정비를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "정비 등록 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    @PostMapping("/maintenance")
    public ResponseEntity<Void> insertMaintenance(@RequestBody MaintenanceDTO maintenanceDTO) {
        maintenanceService.insertMaintenance(maintenanceDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "시설물 위치 조회", description = "시설물의 위치를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "위치 조회 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    @GetMapping("/structure")
    public ResponseEntity<ResponseDTO> getStructureLocation(@RequestParam("structure") String structure) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        List<Object> locationList = maintenanceService.findlocation(structure);
        Map<String, Object> response = new HashMap<>();
        response.put("locationList", locationList);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseDTO(HttpStatus.OK, "위치 조회", response));
    }

    @Operation(summary = "정비 장비 등록", description = "정비 장비를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정비 장비 등록 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    @PutMapping("/maintenanceEquipment")
    public ResponseEntity<Void> maintenanceEquipment(@RequestBody MaintenanceEquipmentDTO maintenanceEquipment) {
        maintenanceService.maintenanceEquipment(maintenanceEquipment);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "정비 장비 데이터 조회", description = "정비 장비 데이터를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정비 장비 데이터 조회 성공",
                    content = @Content(schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    @GetMapping("/maintenanceEquipment")
    public ResponseEntity<Integer> getMaintenanceEquipment(@RequestParam("maintenanceCode") String maintenanceCode) {
        int result = maintenanceService.getMaintenanceEquipment(Integer.parseInt(maintenanceCode));
        return ResponseEntity.ok(result);
    }
}
