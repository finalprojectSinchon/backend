package com.finalproject.airport.storage.controller;

import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.storage.dto.StorageDTO;
import com.finalproject.airport.storage.dto.StorageRegistDTO;
import com.finalproject.airport.storage.service.StorageService;
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
public class StorageController {
    @Autowired
    private final StorageService storageService;

    public StorageController(StorageService storageService) {this.storageService = storageService;}
    @GetMapping("/storage")  // 전체조회
    public ResponseEntity<?> selectStorageList() {

        List<StorageDTO> storageList = storageService.selectAllStorage();

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "창고 목록 조회 성공", storageList));
    }

    @GetMapping("/storage/{storageCode}") // 상세조회
    public ResponseEntity<?> selectByStorageCode(@PathVariable String storageCode) {
        StorageDTO storage = storageService.getStorage(storageCode);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "창고 상세목록 조회", storage));
    }

    @PostMapping("/storage")  // 등록
    public ResponseEntity<?> addStorage(StorageRegistDTO storageRegistDTO) {
        try{
            storageService.addStorage(storageRegistDTO);

            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.CREATED,"창고 등록에 성공하였습니다",null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"등록에 오류가 발생하였습니다",null));
        }
    }




}
