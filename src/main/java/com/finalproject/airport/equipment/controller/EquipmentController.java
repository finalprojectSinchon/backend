package com.finalproject.airport.equipment.controller;


import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.equipment.dto.EquipmentDTO;
import com.finalproject.airport.equipment.service.EquipmentService;
import com.finalproject.airport.inspection.dto.InspectionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    @GetMapping("/equipment")
    public ResponseEntity<?> findAllEquipment() {


        List<EquipmentDTO> equipmentList = equipmentService.findAllEquipment();

        if(equipmentList.isEmpty()){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"전체 조회 성공", equipmentList));
        }
    }

    @GetMapping("/equipment/{equipmentCode}")
    public ResponseEntity<?> findEquipmentByEquipmentCode(@PathVariable("equipmentCode") int equipmentCode) {

        EquipmentDTO equipment = equipmentService.findByCode(equipmentCode);
        if(equipment == null){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"상세 조회 성공", equipment));
        }
    }

    @PostMapping("/equipment")
    public ResponseEntity<?> addEquipment(@RequestBody EquipmentDTO equipmentDTO) {
        try {
            System.out.println("equipmentDTO = " + equipmentDTO);
            equipmentService.addEquipment(equipmentDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(HttpStatus.CREATED,"등록에 성공하였습니다.",null));
        } catch (Exception e) {
           return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"등록 실패",null));
        }
    }

    @PutMapping("/equipment/{equipmentCode}")
    public ResponseEntity<?> updateEquipment(@RequestBody  EquipmentDTO equipmentDTO, @PathVariable int equipmentCode) {
        System.out.println("equipmentDTO = " + equipmentDTO);
        try {
            equipmentService.updateEquipment(equipmentCode,equipmentDTO);

            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"수정 성공",null));
        } catch ( Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/equipment/{equipmentCode}/delete")
    public ResponseEntity<?> softDeleteEquipment(@PathVariable int equipmentCode) {

        try {
            equipmentService.softDeleteEquipment(equipmentCode);

            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"삭제 성공하였습니다.",null));
        } catch ( Exception e) {

            return ResponseEntity.badRequest().build();
        }
    }







}
