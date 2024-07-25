package com.finalproject.airport.manager.controller;

import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.manager.dto.ManagerTypeCodeDTO;
import com.finalproject.airport.manager.dto.ManagerUpdateDTO;
import com.finalproject.airport.manager.service.ManagersService;
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

    @PostMapping("/managers")
    public ResponseEntity<?> getAllManagers(@RequestBody ManagerTypeCodeDTO managerTypeCodeDTO) {

        try {
            Map<String, List<?>> findManger =  managersService.findManagers(managerTypeCodeDTO);
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"조회에 성공하였습니다.",findManger));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage(),null));
        }

    }

    @PutMapping("/managers")
    public ResponseEntity<?> updateManagers(@RequestBody List<ManagerUpdateDTO> managerUpdateDTOListDTO) {
        try {
            String airportType = managerUpdateDTOListDTO.get(0).getAirportType();
        } catch (Exception e){
            return ResponseEntity.badRequest().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"담당 직원은 없을 수 없습니다. 관리자에게 문의해주세요",null));
        }

        managersService.updateManagers(managerUpdateDTOListDTO);

        return ResponseEntity.ok().build();
    }


}
