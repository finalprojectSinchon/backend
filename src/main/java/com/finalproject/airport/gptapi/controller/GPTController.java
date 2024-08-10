package com.finalproject.airport.gptapi.controller;

import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.gptapi.dto.request.GPTRequestDTO;
import com.finalproject.airport.gptapi.dto.response.GPTResponseDTO;
import com.finalproject.airport.gptapi.service.GPTService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "GPT API", description = "GPT 서비스와 관련된 API")
public class GPTController {

    private final GPTService gptService;

    @PostMapping("/ask")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = @Content(
                            schema = @Schema(implementation = GPTResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public GPTResponseDTO getChatCompletion(
            @RequestBody GPTRequestDTO request) {

        String prompt = "한국어로 100글자 이상으로 만들어줘";

        return gptService.getChatCompletion(request, prompt);
    }

    @PostMapping("/ai/info/{airportType}")
    public ResponseEntity<?> aiAskInfo(@RequestBody GPTRequestDTO GPTRequestDTO, @PathVariable String airportType) {

        GPTResponseDTO gptResponse = gptService.aiAskInfo(GPTRequestDTO, airportType);


        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"응답 성공",gptResponse));
    }

}
