package com.finalproject.airport.QRCode.controller;

import com.finalproject.airport.QRCode.dto.JoinQRDTO;
import com.finalproject.airport.QRCode.service.QRService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class QRController {

    private final QRService qrService;

    @Operation(summary = "QR 코드 목록 조회", description = "특정 QR 코드 유형에 대한 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "QR 코드 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    @GetMapping("/admin/qr/{qrType}")
    public ResponseEntity<?> getQRList(@PathVariable String qrType) {
        ResponseEntity<?> response = qrService.getQRList(qrType);
        return response;
    }

    @Operation(summary = "QR 코드 상세 조회", description = "특정 유형과 코드에 대한 QR 코드의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "QR 코드 상세 조회 성공",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "QR 코드 없음",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    @GetMapping("/qr/{type}/{code}")
    public ResponseEntity<?> getQRDetail(@PathVariable String type, @PathVariable int code) {
        ResponseEntity<?> response = qrService.getQRDetail(type, code);
        return response;
    }

    @Operation(summary = "QR 코드 등록", description = "QR 코드를 기준으로 안전점검을 등록합니다..")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "QR 코드 생성 성공",
                    content = @Content(schema = @Schema(implementation = JoinQRDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = JoinQRDTO.class)))
    })
    @PostMapping("/qr")
    public ResponseEntity<?> joinQR(@RequestBody JoinQRDTO info) {

        ResponseEntity<?> response = qrService.joinQR(info);
        return response;
    }
}
