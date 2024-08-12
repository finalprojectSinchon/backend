package com.finalproject.airport.storage.controller;

import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.storage.dto.StorageDTO;
import com.finalproject.airport.storage.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@Tag(name = "창고 관리", description = "창고 관리와 관련된 작업들")
public class StorageController {
    private final StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @Operation(summary = "모든 창고 조회", description = "모든 창고의 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "창고 목록을 성공적으로 조회",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    @GetMapping("/storage")
    public ResponseEntity<ResponseDTO> selectStorageList() {
        List<StorageDTO> storageList = storageService.selectAllStorage();
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "창고 목록 조회 성공", storageList));
    }

    @Operation(summary = "창고 코드로 창고 조회", description = "창고 코드를 사용하여 특정 창고의 세부 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "창고 세부 정보를 성공적으로 조회",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "창고를 찾을 수 없음")
    })
    @GetMapping("/storage/{storageCode}")
    public ResponseEntity<ResponseDTO> selectByStorageCode(
            @Parameter(description = "조회할 창고의 코드", required = true) @PathVariable String storageCode) {
        StorageDTO storage = storageService.getStorage(storageCode);
        if (storage != null) {
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "창고 상세목록 조회 성공", storage));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(HttpStatus.NOT_FOUND, "창고를 찾을 수 없습니다.", null));
        }
    }

    @Operation(summary = "새 창고 추가", description = "제공된 세부 정보로 새 창고를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "새 창고를 성공적으로 등록",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "등록 중 서버 오류 발생")
    })
    @PostMapping("/storage")
    public ResponseEntity<ResponseDTO> addStorage(
            @Parameter(description = "등록할 창고의 세부 정보", required = true) @RequestBody StorageDTO storageDTO) {

        System.out.println("123 " + storageDTO);
        try {
            storageService.addStorage(storageDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(HttpStatus.CREATED, "창고 등록에 성공하였습니다", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "등록에 오류가 발생하였습니다", null));
        }
    }

    @Operation(summary = "창고 수정", description = "기존 창고의 세부 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "창고를 성공적으로 수정",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "수정 중 서버 오류 발생")
    })
    @PutMapping("/storage/{storageCode}")
    public ResponseEntity<ResponseDTO> updateStorage(
            @Parameter(description = "수정할 창고의 코드", required = true) @PathVariable String storageCode,
            @Parameter(description = "수정할 창고의 세부 정보", required = true) @RequestBody StorageDTO storageDTO) {
        try {
            String resultMessage = storageService.updateStorage(storageDTO);
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "창고 수정 성공", resultMessage));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "수정 중 오류 발생", e.getMessage()));
        }
    }

    @Operation(summary = "창고 삭제", description = "코드를 사용하여 창고를 소프트 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "창고를 성공적으로 삭제",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "삭제 중 서버 오류 발생")
    })
    @PutMapping("/storage/{storageCode}/delete")
    public ResponseEntity<ResponseDTO> deleteStorage(
            @Parameter(description = "삭제할 창고의 코드", required = true) @PathVariable int storageCode) {
        try {
            storageService.softDeleteStorage(storageCode);
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "삭제에 성공했습니다.", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "삭제에 오류가 발생했습니다.", null));
        }
    }
}
