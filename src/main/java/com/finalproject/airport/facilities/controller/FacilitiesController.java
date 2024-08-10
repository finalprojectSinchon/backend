package com.finalproject.airport.facilities.controller;

import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.facilities.dto.FacilitiesDTO;
import com.finalproject.airport.facilities.service.FacilitiesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class FacilitiesController {

    @Autowired
    private final FacilitiesService facilitiesService;

    public FacilitiesController(FacilitiesService facilitiesService) {
        this.facilitiesService = facilitiesService;
    }

    @Operation(summary = "편의 시설 목록 조회", description = "모든 편의 시설의 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "편의 시설 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @GetMapping("/facilities")
    public ResponseEntity<ResponseDTO> selectFacilitiesList() {
        List<FacilitiesDTO> facilitiesList = facilitiesService.selectAllFacilities();
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "편의 시설 목록 조회 성공", facilitiesList));
    }

    @Operation(summary = "편의 시설 상세 조회", description = "편의 시설 코드를 사용하여 특정 편의 시설의 세부 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "편의 시설 상세 조회 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "편의 시설을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    @GetMapping("/facilities/{facilitiesCode}")
    public ResponseEntity<ResponseDTO> findFacilities(@PathVariable int facilitiesCode) {
        try {
            FacilitiesDTO findFacilities = facilitiesService.findFacilities(facilitiesCode);
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "편의 시설 상세 조회", findFacilities));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(HttpStatus.NOT_FOUND, "번호를 찾지 못하였습니다.", null));
        }
    }

    @Operation(summary = "편의 시설 등록", description = "새로운 편의 시설을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "편의 시설 등록 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @PostMapping("/facilities")
    public ResponseEntity<ResponseDTO> insertFacilities(@RequestBody FacilitiesDTO facilitiesDTO) {
        facilitiesService.insertFacilities(facilitiesDTO);
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "등록에 성공했습니다.", null));
    }

    @Operation(summary = "편의 시설 수정", description = "기존 편의 시설의 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "편의 시설 수정 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    @PutMapping("/facilities/{facilitiesCode}")
    public ResponseEntity<ResponseDTO> modifyFacilities(@RequestBody FacilitiesDTO modifyFacilitiesDTO) {
        try {
            String resultMessage = facilitiesService.modifyFacilities(modifyFacilitiesDTO);
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, resultMessage, null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        }
    }

    @Operation(summary = "편의 시설 삭제", description = "편의 시설을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "편의 시설 삭제 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
    })
    @PutMapping("/facilities/{facilitiesCode}/delete")
    public ResponseEntity<ResponseDTO> deleteFacilities(@PathVariable int facilitiesCode) {
        facilitiesService.deleteFacilities(facilitiesCode);
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "삭제가 완료되었습니다.", null));
    }
}
