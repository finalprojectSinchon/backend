package com.finalproject.airport.gptapi.service;

import com.finalproject.airport.gptapi.dto.request.GPTRequestDTO;
import com.finalproject.airport.gptapi.dto.response.GPTResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GPTService {

    @Value("${gpt.api.key}")
    private String apiKey;

    @Value("${gpt.api.url}")
    private String apiUrl;

    @Value("${gpt.model}")
    private String apiModel;

    private final WebClient webClient;

    public GPTResponseDTO getChatCompletion(GPTRequestDTO request, String extraQuestion) {

        request.setModel(apiModel);

        for (GPTRequestDTO.Message message : request.getMessages()) {
            if ("user".equals(message.getRole())) {
                String updatedContent = message.getContent() + " " + extraQuestion;
                message.setContent(updatedContent);
            }
        }


        Mono<GPTResponseDTO> responseMono = webClient.post()
                .uri(apiUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GPTResponseDTO.class);


        return responseMono.block();
    }
}
