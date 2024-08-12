package com.finalproject.airport.equipment.controller;

import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.equipment.dto.EquipmentDTO;
import com.finalproject.airport.equipment.dto.EquipmentStorageDTO;
import com.finalproject.airport.equipment.service.EquipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    @Operation(summary = "모든 장비 조회", description = "등록된 모든 장비의 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "장비 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "장비 목록이 비어 있음")
    })
    @GetMapping("/equipment")
    public ResponseEntity<ResponseDTO> findAllEquipment() {
        List<EquipmentDTO> equipmentList = equipmentService.findAllEquipment();
        if(equipmentList.isEmpty()){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "전체 조회 성공", equipmentList));
        }
    }

    @Operation(summary = "장비 코드로 장비 조회", description = "장비 코드를 사용하여 특정 장비의 세부 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "장비 상세 조회 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "204", description = "장비를 찾을 수 없음")
    })
    @GetMapping("/equipment/{equipmentCode}")
    public ResponseEntity<ResponseDTO> findEquipmentByEquipmentCode(@PathVariable("equipmentCode") int equipmentCode) {
        EquipmentDTO equipment = equipmentService.findByCode(equipmentCode);
        if(equipment == null){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "상세 조회 성공", equipment));
        }
    }

    @Operation(summary = "새 장비 등록", description = "새 장비를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "장비 등록 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "등록 중 서버 오류 발생")
    })
    @PostMapping("/equipment")
    public ResponseEntity<ResponseDTO> addEquipment(@RequestBody EquipmentDTO equipmentDTO) {
        try {
            equipmentService.addEquipment(equipmentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(HttpStatus.CREATED, "등록에 성공하였습니다.", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "등록 실패", null));
        }
    }

    @Operation(summary = "장비 수정", description = "기존 장비의 세부 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "장비 수정 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PutMapping("/equipment/{equipmentCode}")
    public ResponseEntity<ResponseDTO> updateEquipment(@RequestBody EquipmentDTO equipmentDTO, @PathVariable int equipmentCode) {
        try {
            equipmentService.updateEquipment(equipmentCode, equipmentDTO);
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "수정 성공", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "장비 소프트 삭제", description = "장비를 소프트 삭제합니다. 삭제된 장비는 검색되지 않습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "장비 소프트 삭제 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PutMapping("/equipment/{equipmentCode}/delete")
    public ResponseEntity<ResponseDTO> softDeleteEquipment(@PathVariable int equipmentCode) {
        try {
            equipmentService.softDeleteEquipment(equipmentCode);
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "삭제 성공하였습니다.", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/equipment/storage")
    public ResponseEntity<?> getStorage() {
        try {
            List<EquipmentStorageDTO> lists = equipmentService.getStorage();
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회에 성공하였습니다.", lists));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
