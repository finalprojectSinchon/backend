package com.finalproject.airport.gptapi.controller;

import com.finalproject.airport.gptapi.dto.request.GPTRequestDTO;
import com.finalproject.airport.gptapi.dto.response.GPTResponseDTO;
import com.finalproject.airport.gptapi.service.GPTService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class GPTController {

    private final GPTService gptService;


    @PostMapping("/ask")
    public GPTResponseDTO getChatCompletion(@RequestBody GPTRequestDTO request) {

        String q = "한국어로 100글자 이상으로 만들어줘";

        return gptService.getChatCompletion(request,q);
    }

}
