package com.finalproject.airport.storage.controller;

import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.storage.dto.StorageDTO;
import com.finalproject.airport.storage.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class StorageController {
    @Autowired
    private final StorageService storageService;

    public StorageController(StorageService storageService) {this.storageService = storageService;}
    @GetMapping("/storage")
    public ResponseEntity<?> selectStorageList() {

        List<StorageDTO> storageList = storageService.selectAllStorage();

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "창고 목록 조회 성공", storageList));
    }

    @GetMapping("/storage/{storageCode}")
    public ResponseEntity<?> selectByStorageCode(@PathVariable String storageCode) {
        StorageDTO storage = storageService.getstorage(storageCode);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "창고 상세목록 조회", storage));
    }
}
