package com.finalproject.airport.QRCode.controller;

import com.finalproject.airport.QRCode.dto.JoinQRDTO;
import com.finalproject.airport.QRCode.service.QRService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class QRController {

    private final QRService qrService;

    @GetMapping("/admin/qr/{qrType}")
    public ResponseEntity<?> getQRList(@PathVariable String qrType) {

        ResponseEntity<?> response = qrService.getQRList(qrType);


        return response;
    }

    @GetMapping("/qr/{type}/{code}")
    public ResponseEntity<?> getQRDetail(@PathVariable String type, @PathVariable int code) {

        ResponseEntity<?> response = qrService.getQRDetail(type,code);

        return response;
    }

    @PostMapping("/qr")
    public ResponseEntity<?> joinQR(@RequestBody JoinQRDTO info) {

        System.out.println("info = " + info);
        ResponseEntity<?> response = qrService.joinQR(info);


        return response;
    }

}
