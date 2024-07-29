package com.finalproject.airport.facilities.controller;

import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.facilities.dto.FacilitiesDTO;
import com.finalproject.airport.facilities.service.FacilitiesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1")
@Slf4j
public class FacilitiesController {
    @Autowired
    private final FacilitiesService facilitiesService;

    public FacilitiesController(FacilitiesService facilitiesService) {this.facilitiesService = facilitiesService;}
    @GetMapping("/facilities")
    public ResponseEntity<?> selectFacilitiesList() {

        List<FacilitiesDTO> facilitiesList = facilitiesService.selectAllFacilities();

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "편의 시설 목록 조회 성공", facilitiesList));
    }

 @GetMapping("/facilities/{facilitiesCode}")
    public ResponseEntity<?> findFacilities(@PathVariable int facilitiesCode) {

        try {
            FacilitiesDTO findFacilities = facilitiesService.findFacilities(facilitiesCode);
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "편의 시설 상세 조회", findFacilities));
        }catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(HttpStatus.NOT_FOUND , "번호를 찾지 못하였습니다." ,null  ));
        }
 }

 // 등록
@PostMapping("/facilities")
    public ResponseEntity<?> insertfacilities(@RequestBody FacilitiesDTO facilitiesDTO){

    System.out.println("facilitiesDTO = " + facilitiesDTO);
    facilitiesService.insertFacilities(facilitiesDTO);

    return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK ,"등록에 성공했습니다." , null ));
}
@PutMapping("/facilities/{facilitiesCode}")
    public ResponseEntity<?> updateFacilities(@RequestBody FacilitiesDTO facilitiesDTO , @PathVariable int facilitiesCode) {

    System.out.println(facilitiesCode + " 11111111111111111111111111111111111111111111111111111111111111111111111111111111111");
    System.out.println(facilitiesDTO + " 12231231323213125446564545646564564546564564564564545456454545454564646545646465");
        facilitiesService.updateFacilities(facilitiesCode , facilitiesDTO);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK , "수정 완료되었습니다" , null));
}

@PutMapping("/facilities/{facilitiesCode}/delete")
    public ResponseEntity<?> deleteFacilities(@PathVariable int facilitiesCode) {

        facilitiesService.deleteFacilities(facilitiesCode);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK , "삭제가 완료되었습니다. " ,null));
}

}
