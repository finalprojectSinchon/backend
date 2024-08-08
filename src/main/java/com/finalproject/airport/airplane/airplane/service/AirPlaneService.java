package com.finalproject.airport.airplane.airplane.service;

import com.finalproject.airport.airplane.airplane.DTO.AirplaneDTO;
import com.finalproject.airport.airplane.airplane.DTO.ArrivalAirplaneDTO;
import com.finalproject.airport.airplane.airplane.DTO.DepartureAirplaneDTO;
import com.finalproject.airport.airplane.airplane.Entity.Airplane;
import com.finalproject.airport.airplane.airplane.repository.AirplaneRepository;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;


import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.List;

import java.util.stream.Collectors;

import static java.security.Timestamp.*;

@Service
public class AirPlaneService {

    private final AirplaneRepository airplaneRepository;
    private final ModelMapper modelMapper;

    private final String apiKey;
    private final String ArrivalApiUrl;
    private final String DepartureApiUrl;



    @Autowired
    public AirPlaneService(AirplaneRepository airplaneRepository , ModelMapper modelMapper ) {
        this.airplaneRepository = airplaneRepository;
        this.modelMapper = modelMapper;
        Dotenv dotenv = Dotenv.load();
        this.apiKey = dotenv.get("API_KEY");
        this.ArrivalApiUrl = dotenv.get("ARRIVAL_API_URL");
        this.DepartureApiUrl = dotenv.get("DEPARTURE_API_URL");

    }


    public void fetchArrivalAirplane() {

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = today.format(formatter);
        String requestUrl = ArrivalApiUrl +"serviceKey=" +apiKey+ "&type=json" + "&numOfRows=10000" + "&searchday=" + formattedDate; ;
        System.out.println("requestUrl = " + requestUrl);

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1)) // to unlimited memory size
                .build();

        // WebClient는 Builder 패턴 처럼 사용
        WebClient webClient = WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
                .build();



        ArrivalAirplaneDTO arrivalAirplaneDTO = webClient.get()
                .uri(requestUrl)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .retrieve()
                .bodyToMono(ArrivalAirplaneDTO.class)
                .block();


        if (arrivalAirplaneDTO != null) {
            insertArrivalAirplaneItems(arrivalAirplaneDTO);
        } else {
            System.out.println("No data received");
        }

    }


    public void fetchDepartureAirplane() {

        String requestUrl = DepartureApiUrl +"serviceKey=" +apiKey+ "&type=json" ;
        System.out.println("requestUrl = " + requestUrl);

        // WebClient는 Builder 패턴 처럼 사용
        WebClient webClient = WebClient.builder()
                .build();

        DepartureAirplaneDTO departureAirplaneDTO = webClient.get()
                .uri(requestUrl)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .retrieve()
                .bodyToMono(DepartureAirplaneDTO.class)
                .block();


        if (departureAirplaneDTO != null) {
            insertDepartureAirplaneItems(departureAirplaneDTO);
        } else {
            System.out.println("No data received");
        }

    }

    private void insertArrivalAirplaneItems(ArrivalAirplaneDTO dto) {

        if (dto != null && dto.getResponse() != null && dto.getResponse().getBody() != null) {
            List<ArrivalAirplaneDTO.Response.Body.Item> items = dto.getResponse().getBody().getItems();
            if (items != null && !items.isEmpty()) {
                for (ArrivalAirplaneDTO.Response.Body.Item item : items) {

                    if (item.getGatenumber() == " " || item.getGatenumber().isEmpty()){
                        item.setGatenumber("999");
                    }
                    // 원본 문자열을 LocalDateTime으로 변환하기 위한 포맷터
                    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

                    // 변환할 목표 형식
                    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                    // 문자열을 LocalDateTime으로 파싱
                    LocalDateTime localDateTime = LocalDateTime.parse(item.getScheduleDateTime(), inputFormatter);

                    // LocalDateTime을 Timestamp로 변환
                    Timestamp timestamp = Timestamp.valueOf(localDateTime);
                    AirplaneDTO airplaneDTO =
                            new AirplaneDTO(
                                    item.getAirline(),
                                    item.getFlightId(),
                                    timestamp,
                                    item.getAirport(),
                                    item.getRemark(),
                                    item.getCarousel(),
                                    item.getGatenumber(),
                                    item.getTerminalid()
                                    );
                    Airplane airplane = modelMapper.map(airplaneDTO, Airplane.class);
                    airplaneRepository.save(airplane);

                }
            } else {
                System.out.println("No items available.");
            }
        } else {
            System.out.println("Invalid data structure.");
        }
    }
    private void insertDepartureAirplaneItems(DepartureAirplaneDTO dto) {

        if (dto != null && dto.getResponse() != null && dto.getResponse().getBody() != null) {
            List<DepartureAirplaneDTO.Response.Body.Item> items = dto.getResponse().getBody().getItems();
            if (items != null && !items.isEmpty()) {
                for (DepartureAirplaneDTO.Response.Body.Item item : items) {
                    System.out.println(item.getScheduleDateTime());
                    // 원본 문자열을 LocalDateTime으로 변환하기 위한 포맷터
                    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

                    // 변환할 목표 형식
                    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                    // 문자열을 LocalDateTime으로 파싱
                    LocalDateTime localDateTime = LocalDateTime.parse(item.getScheduleDateTime(), inputFormatter);

                    // LocalDateTime을 Timestamp로 변환
                    Timestamp timestamp = Timestamp.valueOf(localDateTime);
                    AirplaneDTO airplaneDTO =
                            new AirplaneDTO(
                                    item.getAirline(),
                                    item.getFlightId(),
                                    item.getChkinrange(),
                                    timestamp,
                                    item.getAirport(),
                                    item.getRemark(),
                                    item.getGatenumber(),
                                    item.getTerminalid()
                            );
                    Airplane airplane = modelMapper.map(airplaneDTO, Airplane.class);
                    airplaneRepository.save(airplane);

                }
            } else {
                System.out.println("No items available.");
            }
        } else {
            System.out.println("Invalid data structure.");
        }
    }



    public List<AirplaneDTO> findAll() {
        List<Airplane> AirplaneList = airplaneRepository.findByisActive("Y");

        return AirplaneList.stream()
                .map(airplane -> modelMapper.map(airplane, AirplaneDTO.class))
                .collect(Collectors.toList());
    }

    public AirplaneDTO findByairplaneCode(int airplaneCode) {

        Airplane airplane  = airplaneRepository.findByairplaneCode(airplaneCode);
        return modelMapper.map(airplane, AirplaneDTO.class);
    }

    @Transactional
    public void modifybAirplane(int airplaneCode, AirplaneDTO modifyairplane) {

        Airplane airplane = airplaneRepository.findByairplaneCode(airplaneCode);

        airplane =  airplane.toBuilder()
                .airline(modifyairplane.getAirline())
                .scheduleDateTime(modifyairplane.getScheduleDateTime())
                .remark(modifyairplane.getRemark())
                .airport(modifyairplane.getAirport())
                .flightId(modifyairplane.getFlightId())
                .carousel(modifyairplane.getCarousel())
                .gatenumber(modifyairplane.getGatenumber())
                .terminalid(modifyairplane.getTerminalid())
                .chkinrange(modifyairplane.getChkinrange())
                .build();

        airplaneRepository.save(airplane);

    }

    @Transactional
    public void softDelete(int airplaneCode) {
        Airplane airplane = airplaneRepository.findByairplaneCode(airplaneCode);
        airplane = airplane.toBuilder().isActive("N").build();

        airplaneRepository.save(airplane);

    }
}
