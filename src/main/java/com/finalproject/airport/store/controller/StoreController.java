package com.finalproject.airport.store.controller;

import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.store.dto.StoreAPIDTO;
import com.finalproject.airport.store.dto.StoreDTO;
import com.finalproject.airport.store.dto.StoreRegistDTO;
import com.finalproject.airport.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StoreController {


    private final StoreService storeService;

    @GetMapping("/store")
    public ResponseEntity<?> getStore () {

        List<StoreDTO> storeList = storeService.getStoreList();

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"점포 전체조회 성공", storeList));
    }

    @GetMapping("/store/{storeCode}")
    public ResponseEntity<?> getStoreByStoreCode(@PathVariable String storeCode) {

        StoreDTO store = storeService.getStore(storeCode);
        if(store == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "점포 상세 조회 성공", store));
        }
    }

    @PostMapping("/store")
    public ResponseEntity<?> addStore(StoreRegistDTO storeRegistDTO) {
        try {
            storeService.addStore(storeRegistDTO);

            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.CREATED,"점포 등록에 성공하였습니다",null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"등록에 오류가 발생하였습니다",null));
        }
    }

    @PutMapping("/store/{storeCode}/delete")
    public ResponseEntity<?> deleteStore(@PathVariable int storeCode) {
        System.out.println("storeCode = " + storeCode);
        try {
            storeService.softDeleteStore(storeCode);

            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"삭제에 성공하였습니다.",null));
        } catch (Exception e) {

            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"삭제에 오류가 발생하였습니다",null));
        }
    }

    @PutMapping("/store/{storeCode}")
    public ResponseEntity<?> updateStore(@PathVariable int storeCode, @RequestBody StoreDTO storeDTO) {
            try {
                storeService.updateStore(storeCode,storeDTO);

                return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.CREATED,"수정에 성공하였습니다.",null));
            } catch (Exception e) {

                return ResponseEntity.internalServerError().build();
            }
    }

    @PostMapping("/store/api")
    public ResponseEntity<?> insertApi(@RequestBody List<StoreAPIDTO> storeDTO) {

        System.out.println("storeDTO = " + storeDTO);
        storeService.updateApi(storeDTO);

        return ResponseEntity.ok().build();
    }

}
