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

import java.util.Arrays;
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
        if ("store".equals(airportType)) {
            return handleStoreInfo(request);
        }

        if ("airport".equals(airportType)) {
            return handleAirportInfo(request);
        }
        String prompt = generatePrompt(airportType);
        enhanceRequest(request, prompt);
        return sendRequestToGPT(request);
    }

    private GPTResponseDTO handleAirportInfo(GPTRequestDTO request) {
        String prompt = "아래에 제공된 인천국제공항에 관련된 데이터만을 사용하여 다음 질문에 답변해주세요. 제공된 정보 외의 다른 정보는 사용하지 마세요.\n\n" +
                "질문: " + request.getMessages().get(request.getMessages().size() - 1).getContent() + "\n\n" +
                "답변 시 다음 사항을 반드시 지켜주세요:\n" +
                "1. 한국어로 답변해주세요.\n" +
                "2. 친절하고 상세하게 답변해주세요.\n" +
                "3. 제공된 인천국제공항 정보만을 사용하여 답변하세요. 이 정보 외의 내용은 언급하지 마세요.\n" +
                "4. 정보가 부족하거나 확실하지 않은 경우, '제공된 정보로는 확실히 알 수 없습니다'라고 명시해주세요.\n\n" +
                "제공된 인천국제공항 정보:\n\n";

        GPTRequestDTO finalRequest = new GPTRequestDTO();
        finalRequest.setModel(apiModel);
        finalRequest.setMessages(Arrays.asList(new GPTRequestDTO.Message("user", prompt)));

        return sendRequestToGPT(finalRequest);
    }

    private GPTResponseDTO handleStoreInfo(GPTRequestDTO request) {
        List<StoreEntity> stores = storeRepository.findByIsActive("Y");
        StringBuilder allInfo = new StringBuilder();

        for (int i = 0; i < stores.size(); i += 30) {
            int end = Math.min(i + 30, stores.size());
            List<StoreEntity> storeSubset = stores.subList(i, end);

            String prompt = formatDataToPrompt(storeSubset, "store");
            GPTRequestDTO tempRequest = new GPTRequestDTO();
            tempRequest.setModel(apiModel);
            tempRequest.setMessages(Arrays.asList(new GPTRequestDTO.Message("user", prompt)));

            GPTResponseDTO response = sendRequestToGPT(tempRequest);
            allInfo.append(response.getChoices().get(0).getMessage().getContent()).append("\n\n");
        }

        String finalPrompt = "아래에 제공된 점포 정보만을 사용하여 다음 질문에 답변해주세요. 제공된 정보 외의 다른 정보는 사용하지 마세요.\n\n" +
                "질문: " + request.getMessages().get(request.getMessages().size() - 1).getContent() + "\n\n" +
                "답변 시 다음 사항을 반드시 지켜주세요:\n" +
                "1. 한국어로 답변해주세요.\n" +
                "2. 친절하고 상세하게 답변해주세요.\n" +
                "3. 제공된 점포 정보만을 사용하여 답변하세요. 이 정보 외의 내용은 언급하지 마세요.\n" +
                "4. 정보가 부족하거나 확실하지 않은 경우, '제공된 정보로는 확실히 알 수 없습니다'라고 명시해주세요.\n\n" +
                "제공된 점포 정보:\n" + allInfo.toString() + "\n\n" +
                "이제 위 정보만을 사용하여 질문에 답변해주세요.";

        GPTRequestDTO finalRequest = new GPTRequestDTO();
        finalRequest.setModel(apiModel);
        finalRequest.setMessages(Arrays.asList(new GPTRequestDTO.Message("user", finalPrompt)));

        return sendRequestToGPT(finalRequest);
    }

    private String generatePrompt(String airportType) {
        List<?> data = getDataByAirportType(airportType);
        return formatDataToPrompt(data, airportType);
    }

    private List<?> getDataByAirportType(String airportType) {
        switch (airportType) {
            case "checkInCounter":
                return checkinCounterRepository.findByisActive("Y");
            case "gate":
                return gateRepository.findByisActive("Y");
            case "baggageClaim":
                return baggageClaimRepository.findByisActive("Y");
            case "store":
                return storeRepository.findByIsActive("Y");
            case "facilities":
                return facilitiesRepository.findAllByIsActive("Y");
            case "storage":
                return storageRepository.findByisActive("Y");
            case "equipment":
                return equipmentRepository.findByIsActive("Y");
            default:
                return Collections.emptyList();
        }
    }

    private String formatDataToPrompt(List<?> data, String airportType) {
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("다음은 ").append(getKoreanAirportType(airportType)).append("에 대한 정보입니다:\n\n");

        for (Object item : data) {
            promptBuilder.append("- ").append(item.toString()).append("\n");
        }

        promptBuilder.append("\n이 정보를 친절하게 대답 해주세요. 대답은 사용자가 볼 것 이므로 깔끔하게 대답해주세요. 답변은 한국어로 작성해주세요.");
        return promptBuilder.toString();
    }

    private String getKoreanAirportType(String airportType) {
        switch (airportType) {
            case "checkInCounter": return "체크인 카운터";
            case "gate": return "탑승구";
            case "baggageClaim": return "수하물 찾는 곳";
            case "store": return "상점";
            case "facilities": return "시설";
            case "storage": return "보관소";
            case "equipment": return "장비";
            default: return airportType;
        }
    }

    private void enhanceRequest(GPTRequestDTO request, String prompt) {
        request.setModel(apiModel);
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
