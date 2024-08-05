package com.finalproject.airport.QRCode.controller;

import com.finalproject.airport.QRCode.service.QRService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class QRController {

    private final QRService qrService;

    @GetMapping("/qr/{qrType}")
    public ResponseEntity<?> getQRList(@PathVariable String qrType) {

        ResponseEntity<?> response = qrService.getQRList(qrType);


        return response;
    }

}
