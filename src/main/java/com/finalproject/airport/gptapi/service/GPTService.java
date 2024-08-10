package com.finalproject.airport.gptapi.service;

import com.finalproject.airport.airplane.airplane.repository.ArrivalAirplaneRepository;
import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaim;
import com.finalproject.airport.airplane.baggageclaim.repository.BaggageClaimRepository;
import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounter;
import com.finalproject.airport.airplane.checkincounter.repository.CheckinCounterRepository;
import com.finalproject.airport.airplane.gate.entity.Gate;
import com.finalproject.airport.airplane.gate.repository.GateRepository;
import com.finalproject.airport.equipment.entity.EquipmentEntity;
import com.finalproject.airport.equipment.repository.EquipmentRepository;
import com.finalproject.airport.facilities.entity.FacilitesType;
import com.finalproject.airport.facilities.entity.FacilitiesEntity;
import com.finalproject.airport.facilities.repository.FacilitiesRepository;
import com.finalproject.airport.gptapi.dto.request.GPTRequestDTO;
import com.finalproject.airport.gptapi.dto.response.GPTResponseDTO;
import com.finalproject.airport.storage.entity.StorageEntity;
import com.finalproject.airport.storage.repository.StorageRepository;
import com.finalproject.airport.store.entity.StoreEntity;
import com.finalproject.airport.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class GPTService {

    @Value("${gpt.api.key}")
    private String apiKey;

    @Value("${gpt.api.url}")
    private String apiUrl;

    @Value("${gpt.model}")
    private String apiModel;

    private final CheckinCounterRepository checkinCounterRepository;

    private final GateRepository gateRepository;

    private final BaggageClaimRepository baggageClaimRepository;

    private final StoreRepository storeRepository;

    private final StorageRepository storageRepository;

    private final FacilitiesRepository facilitiesRepository;

    private final EquipmentRepository equipmentRepository;

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

    public GPTResponseDTO aiAskInfo(GPTRequestDTO request, String airportType) {
        String prompt = generatePrompt(airportType);

        request.setModel(apiModel);
        enhanceUserMessage(request, prompt);

        return sendRequestToGPT(request);
    }

    private String generatePrompt(String airportType) {
        Map<String, Supplier<List<?>>> dataSuppliers = Map.of(
                "checkInCounter", () -> checkinCounterRepository.findByisActive("Y"),
                "gate", () -> gateRepository.findByisActive("Y"),
                "baggageClaim", () -> baggageClaimRepository.findByisActive("Y"),
                "store", () -> storeRepository.findByIsActive("Y"),
                "facilities", () -> facilitiesRepository.findAllByIsActive("Y"),
                "storage", () -> storageRepository.findByisActive("Y"),
                "equipment", () -> equipmentRepository.findByIsActive("Y")
        );

        List<?> data = dataSuppliers.getOrDefault(airportType, Collections::emptyList).get();
        return formatDataToPrompt(data, airportType);
    }

    private String formatDataToPrompt(List<?> data, String airportType) {
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("다음은 ").append(getKoreanAirportType(airportType)).append("에 대한 정보입니다:\n\n");

        for (Object item : data) {
            promptBuilder.append("- ").append(item.toString()).append("\n");
        }

        promptBuilder.append("\n이 정보를 바탕으로 질문에 답변해주세요. 답변은 다음 지침을 따라주세요:\n");
        promptBuilder.append("1. 마크다운 형식을 사용하여 구조화된 답변을 제공하세요.\n");
        promptBuilder.append("2. 필요한 경우 표나 목록을 사용하여 정보를 명확하게 전달하세요.\n");
        promptBuilder.append("3. 전문 용어는 가능한 쉽게 설명해주세요.\n");
        promptBuilder.append("4. 답변은 한국어로 작성해주세요.\n");

        return promptBuilder.toString();
    }

    private String getKoreanAirportType(String airportType) {
        Map<String, String> koreanTypes = Map.of(
                "checkInCounter", "체크인 카운터",
                "gate", "탑승구",
                "baggageClaim", "수하물 찾는 곳",
                "store", "상점",
                "facilities", "시설",
                "storage", "보관소",
                "equipment", "장비"
        );
        return koreanTypes.getOrDefault(airportType, airportType);
    }

    private void enhanceUserMessage(GPTRequestDTO request, String prompt) {
        for (GPTRequestDTO.Message message : request.getMessages()) {
            if ("user".equals(message.getRole())) {
                String updatedContent = message.getContent() + "\n\n" + prompt;
                message.setContent(updatedContent);
                break;  // 첫 번째 사용자 메시지만 수정
            }
        }
    }

    private GPTResponseDTO sendRequestToGPT(GPTRequestDTO request) {
        return webClient.post()
                .uri(apiUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GPTResponseDTO.class)
                .block();
    }
}
