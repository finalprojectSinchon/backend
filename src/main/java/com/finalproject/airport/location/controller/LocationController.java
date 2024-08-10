package com.finalproject.airport.location.controller;

import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.location.dto.ZoneDTO;
import com.finalproject.airport.location.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @Operation(summary = "위치 기본 정보 등록", description = "위치에 대한 기본 정보를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "위치 기본 정보 등록 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    @GetMapping("/location/new")
    public ResponseEntity<ResponseDTO> newLocation() {
        locationService.newLocation();
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "위치 기본 정보 등록 성공", null));
    }

    @Operation(summary = "지역 조회", description = "모든 지역 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "지역 조회 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    @GetMapping("/location/region")
    public ResponseEntity<?> getRegion() {
        ResponseEntity<?> response = locationService.getRegion();
        return response;
    }

    @Operation(summary = "층 조회", description = "특정 지역의 층 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "층 조회 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    @GetMapping("/location/region/{region}/floors")
    public ResponseEntity<?> getFloor(@PathVariable String region) {
        ResponseEntity<?> response = locationService.getFloor(region);
        return response;
    }

    @Operation(summary = "위치 조회", description = "특정 지역과 층의 위치 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "위치 조회 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    @GetMapping("/location/floors/{region}/{floor}/locations")
    public ResponseEntity<?> getLocation(@PathVariable(value = "region") String region,
                                         @PathVariable(value = "floor") String floor) {
        ResponseEntity<?> response = locationService.getLocation(region, floor);
        return response;
    }

    @Operation(summary = "위치 추가", description = "새로운 위치 정보를 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "위치 추가 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    @PostMapping("/location")
    public ResponseEntity<?> addLocation(@RequestBody ZoneDTO zone) {
        ResponseEntity<?> response = locationService.addLocation(zone);
        return response;
    }

    @Operation(summary = "위치 조회 (유형과 코드 기반)", description = "위치 유형과 코드를 사용하여 특정 위치 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "위치 조회 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    @GetMapping("/location/{type}/{code}")
    public ResponseEntity<?> getLocation(@PathVariable(value = "type") String type,
                                         @PathVariable(value = "code") int code) {
        ResponseEntity<?> response = locationService.getTypeOfLocation(type, code);
        return response;
    }

    @Operation(summary = "저장 위치 조회", description = "저장 위치 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저장 위치 조회 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    @GetMapping("/location/storage")
    public ResponseEntity<?> getStorageLocation() {
        ResponseEntity<?> response = locationService.getStorageLocation();
        return response;
    }
}
