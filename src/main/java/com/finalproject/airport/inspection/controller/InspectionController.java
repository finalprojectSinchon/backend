package com.finalproject.airport.inspection.controller;

import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.inspection.dto.InspectionDTO;
import com.finalproject.airport.inspection.dto.StatusDTO;
import com.finalproject.airport.inspection.service.InspectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Operation(summary = "안전 점검 전체 조회", description = "모든 안전 점검의 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "안전 점검 전체 조회 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    @GetMapping("/inspection")
    public ResponseEntity<ResponseDTO> getInspection() {
        List<InspectionDTO> inspectionList = inspectionService.getInspectionList();
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "안전 점검 전체조회 성공", inspectionList));
    }

    @Operation(summary = "안전 점검 상세 조회", description = "안전 점검 코드를 사용하여 특정 안전 점검의 세부 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "안전 점검 상세 조회 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "안전 점검을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    @GetMapping("/inspection/{inspectionCode}")
    public ResponseEntity<ResponseDTO> getInspectionByStoreCode(@PathVariable int inspectionCode) {
        InspectionDTO inspection = inspectionService.getInspection(inspectionCode);
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "안전 점검 상세 조회 성공", inspection));
    }

    @Operation(summary = "안전 점검 등록", description = "새로운 안전 점검을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "안전 점검 등록 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    @PostMapping("/inspection")
    public ResponseEntity<ResponseDTO> addInspection(@RequestBody InspectionDTO inspectionDTO) {
        try {
            inspectionService.addInspection(inspectionDTO);
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.CREATED, "안전 점검 등록에 성공했습니다", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "오류가 발생하였습니다.", null));
        }
    }

    @Operation(summary = "안전 점검 수정", description = "기존 안전 점검의 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "안전 점검 수정 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    @PutMapping("/inspection/{inspectionCode}/update")
    public ResponseEntity<ResponseDTO> updateInspection(@PathVariable int inspectionCode, @RequestBody InspectionDTO inspectionDTO) {
        try {
            inspectionService.updateInspection(inspectionCode, inspectionDTO);
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "수정에 성공하셨습니다", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "수정 중 오류 발생", null));
        }
    }

    @Operation(summary = "안전 점검 삭제", description = "안전 점검을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "안전 점검 삭제 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    @PutMapping("/inspection/{inspectionCode}/delete")
    public ResponseEntity<ResponseDTO> deleteInspection(@PathVariable int inspectionCode) {
        try {
            inspectionService.softDeleteInspection(inspectionCode);
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "삭제에 성공했습니다.", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "삭제에 실패했습니다.", null));
        }
    }

    @Operation(summary = "점검 상태 카운트 조회", description = "안전 점검의 상태 카운트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "점검 상태 카운트 조회 성공",
                    content = @Content(schema = @Schema(implementation = StatusDTO.class)))
    })
    @GetMapping("/status-count")
    public ResponseEntity<List<StatusDTO>> getInspectionStatusCount() {
        List<StatusDTO> statusList = inspectionService.statusCount();
        return ResponseEntity.ok(statusList);
    }
}
