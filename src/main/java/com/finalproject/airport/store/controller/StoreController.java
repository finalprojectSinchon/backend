package com.finalproject.airport.store.controller;

import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.store.dto.StoreAPIDTO;
import com.finalproject.airport.store.dto.StoreDTO;
import com.finalproject.airport.store.dto.StoreRegistDTO;
import com.finalproject.airport.store.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "점포 관리", description = "점포 관리와 관련된 작업들")
public class StoreController {

    private final StoreService storeService;

    @Operation(summary = "모든 점포 조회", description = "모든 점포의 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "점포 목록을 성공적으로 조회",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    @GetMapping("/store")
    public ResponseEntity<?> getStore() {
        List<StoreDTO> storeList = storeService.getStoreList();
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "점포 전체조회 성공", storeList));
    }

    @Operation(summary = "점포 코드로 점포 조회", description = "점포 코드를 사용하여 특정 점포의 세부 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "점포 세부 정보를 성공적으로 조회",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "점포를 찾을 수 없음")
    })
    @GetMapping("/store/{storeCode}")
    public ResponseEntity<?> getStoreByStoreCode(@PathVariable String storeCode) {
        StoreDTO store = storeService.getStore(storeCode);
        if (store == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "점포 상세 조회 성공", store));
        }
    }

    @Operation(summary = "새 점포 추가", description = "제공된 세부 정보로 새 점포를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "새 점포를 성공적으로 등록",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "등록 중 서버 오류 발생")
    })
    @PostMapping("/store")
    public ResponseEntity<?> addStore(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "등록할 점포의 세부 정보", required = true) @RequestBody StoreRegistDTO storeRegistDTO) {
        try {
            storeService.addStore(storeRegistDTO);
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.CREATED, "점포 등록에 성공하였습니다", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "등록에 오류가 발생하였습니다", null));
        }
    }

    @Operation(summary = "점포 삭제", description = "코드를 사용하여 점포를 소프트 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "점포를 성공적으로 삭제",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "삭제 중 서버 오류 발생")
    })
    @PutMapping("/store/{storeCode}/delete")
    public ResponseEntity<?> deleteStore(@PathVariable int storeCode) {
        try {
            storeService.softDeleteStore(storeCode);
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "삭제에 성공하였습니다.", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "삭제에 오류가 발생하였습니다", null));
        }
    }

    @Operation(summary = "점포 수정", description = "기존 점포의 세부 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "점포를 성공적으로 수정",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "수정 중 서버 오류 발생")
    })
    @PutMapping("/store/{storeCode}")
    public ResponseEntity<?> updateStore(@PathVariable int storeCode, @RequestBody StoreDTO storeDTO) {
        try {
            storeService.updateStore(storeCode, storeDTO);
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.CREATED, "수정에 성공하였습니다.", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "API 데이터 삽입", description = "공공데이터포털 OPENAPI 데이터를 이용하여 점포 정보를 갱신합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API 데이터를 성공적으로 삽입",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    @PostMapping("/store/api")
    public ResponseEntity<?> insertApi(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "API를 통해 갱신할 점포 데이터", required = true) @RequestBody List<StoreAPIDTO> storeDTO) {
        storeService.updateApi(storeDTO);
        return ResponseEntity.ok().build();
    }

}
