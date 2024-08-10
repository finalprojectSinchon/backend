package com.finalproject.airport.manager.controller;

import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.manager.dto.ManagerTypeCodeDTO;
import com.finalproject.airport.manager.dto.ManagerUpdateDTO;
import com.finalproject.airport.manager.service.ManagersService;
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
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ManagersController {

    private final ManagersService managersService;

    @Operation(summary = "관리자 조회", description = "특정 관리자의 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "관리자 조회 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    @PostMapping("/managers")
    public ResponseEntity<ResponseDTO> getAllManagers(@RequestBody ManagerTypeCodeDTO managerTypeCodeDTO) {
        try {
            Map<String, List<?>> findManager = managersService.findManagers(managerTypeCodeDTO);
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회에 성공하였습니다.", findManager));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        }
    }

    @Operation(summary = "관리자 업데이트", description = "관리자의 정보를 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "관리자 업데이트 성공",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청, 담당 직원 정보 없음",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류 발생",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    @PutMapping("/managers")
    public ResponseEntity<ResponseDTO> updateManagers(@RequestBody List<ManagerUpdateDTO> managerUpdateDTOListDTO) {
        try {
            if (managerUpdateDTOListDTO.isEmpty() || managerUpdateDTOListDTO.get(0).getAirportType() == null) {
                return ResponseEntity.badRequest().body(new ResponseDTO(HttpStatus.BAD_REQUEST, "담당 직원은 없을 수 없습니다. 관리자에게 문의해주세요", null));
            }

            managersService.updateManagers(managerUpdateDTOListDTO);
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "관리자 업데이트 성공", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        }
    }
}
