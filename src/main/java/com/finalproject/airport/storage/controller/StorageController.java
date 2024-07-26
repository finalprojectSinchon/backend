package com.finalproject.airport.storage.controller;

import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.storage.dto.StorageDTO;
import com.finalproject.airport.storage.dto.StorageRegistDTO;
import com.finalproject.airport.storage.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
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
    @Autowired
    private final StorageService storageService;


    public StorageController(StorageService storageService) {this.storageService = storageService;}

    @Operation(summary = "모든 창고 조회", description = "모든 창고의 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "창고 목록을 성공적으로 조회",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class)))
    })
    @GetMapping("/storage") // 전체조회
    public ResponseEntity<?> selectStorageList() {
        System.out.println("111");

        List<StorageDTO> storageList = storageService.selectAllStorage();

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "창고 목록 조회 성공", storageList));
    }

    @Operation(summary = "창고 코드로 창고 조회", description = "창고 코드를 사용하여 특정 창고의 세부 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "창고 세부 정보를 성공적으로 조회",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "창고를 찾을 수 없음")
    })
    @GetMapping("/storage/{storageCode}")   // 상세조회
    public ResponseEntity<?> selectByStorageCode(@PathVariable String storageCode) {
        StorageDTO storage = storageService.getStorage(storageCode);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "창고 상세목록 조회", storage));
    }

    @Operation(summary = "새 창고 추가", description = "제공된 세부 정보로 새 창고를 등록 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "새 창고를 성공적으로 등록",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "등록 중 서버 오류 발생")
    })
    @PostMapping("/storage")    // 등록
    public ResponseEntity<?> addStorage(@RequestBody StorageRegistDTO storageRegistDTO) {
        try{
            storageService.addStorage(storageRegistDTO);

            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.CREATED,"창고 등록에 성공하였습니다",null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"등록에 오류가 발생하였습니다",null));
        }
    }

    @Operation(summary = "창고 수정", description = "기존 창고의 세부 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "창고를 성공적으로 수정",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "수정 중 서버 오류 발생")
    })
    @PutMapping("/storage/{storageCode}")   // 수정
    public ResponseEntity<?> updateStorage(@PathVariable int storageCode, @RequestBody StorageDTO storageDTO) {
            try {
                storageService.updateStorage(storageCode, storageDTO);

                return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "수정에 성공했습니다.", null));
            } catch (Exception e) {

                return ResponseEntity.internalServerError().build();
            }
    }


    @Operation(summary = "창고 삭제", description = "코드를 사용하여 창고를 소프트 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "창고를 성공적으로 삭제",
                    content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "삭제 중 서버 오류 발생")
    })
    @PutMapping("/storage/{storageCode}/delete")    // 삭제
    public ResponseEntity<?> deleteStorage(@PathVariable int storageCode) {
        System.out.println("storageCode = " + storageCode);
        try {
            storageService.softDeleteStorage(storageCode);

            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "삭제에 성공했습니다.", null));

        } catch (Exception e) {

            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "삭제에 오류가 발생했습니다.", null));
        }
    }



}
