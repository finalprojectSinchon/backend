package com.finalproject.airport.airplane.airplane.service;

import com.finalproject.airport.airplane.airplane.DTO.AirplaneDTO;
import com.finalproject.airport.airplane.airplane.DTO.ArrivalAirplaneDTO;
import com.finalproject.airport.airplane.airplane.DTO.DepartureAirplaneDTO;
import com.finalproject.airport.airplane.airplane.Entity.ArrivalAirplane;
import com.finalproject.airport.airplane.airplane.Entity.DepartureAirplane;
import com.finalproject.airport.airplane.airplane.repository.ArrivalAirplaneRepository;
import com.finalproject.airport.airplane.airplane.repository.DepartureAirplaneRepository;
import com.finalproject.airport.airplane.gate.repository.GateRepository;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
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

@Service
@Slf4j
public class AirPlaneService {

    private final DepartureAirplaneRepository departureAirplaneRepository;
    private final ArrivalAirplaneRepository airplaneRepository;
    private final ModelMapper modelMapper;
    private final String apiKey;
    private final String ArrivalApiUrl;
    private final String DepartureApiUrl;
    private final GateRepository gateRepository;

    @Autowired
    public AirPlaneService(DepartureAirplaneRepository departureAirplaneRepository, ArrivalAirplaneRepository airplaneRepository, ModelMapper modelMapper , GateRepository gateRepository ) {
        this.departureAirplaneRepository = departureAirplaneRepository;
        this.airplaneRepository = airplaneRepository;
        this.modelMapper = modelMapper;
        Dotenv dotenv = Dotenv.load();
        this.apiKey = dotenv.get("API_KEY");
        this.ArrivalApiUrl = dotenv.get("ARRIVAL_API_URL");
        this.DepartureApiUrl = dotenv.get("DEPARTURE_API_URL");
        this.gateRepository = gateRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
//    @PostConstruct
    public void fetchArrivalAirplane() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = today.format(formatter);

        String requestUrl = ArrivalApiUrl +"serviceKey=" +apiKey+ "&type=json" + "&numOfRows=10000" + "&searchday=" + formattedDate;
        log.info("Request URL for arrival airplanes: {}", requestUrl);


        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1)) // to unlimited memory size
                .build();

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
            log.info("No data received for arrival airplanes.");
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void fetchDepartureAirplane() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = today.format(formatter);

        String requestUrl = DepartureApiUrl +"serviceKey=" +apiKey+ "&type=json" + "&numOfRows=10000" + "&searchday=" + formattedDate;
        log.info("Request URL for departure airplanes: {}", requestUrl);


        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1)) // to unlimited memory size
                .build();

        WebClient webClient = WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
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
            log.info("No data received for departure airplanes.");
        }
    }

    private void insertArrivalAirplaneItems(ArrivalAirplaneDTO dto) {
        if (dto != null && dto.getResponse() != null && dto.getResponse().getBody() != null) {
            List<ArrivalAirplaneDTO.Response.Body.Item> items = dto.getResponse().getBody().getItems();
            if (items != null && !items.isEmpty()) {
                for (ArrivalAirplaneDTO.Response.Body.Item item : items) {
                    if (item.getGatenumber() == null || item.getGatenumber().isEmpty()) {
                        item.setGatenumber("999");
                    }
                    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
                    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime localDateTime = LocalDateTime.parse(item.getScheduleDateTime(), inputFormatter);
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
                                    item.getTerminalid(),
                                    item.getChkinrange()
                            );
                    ArrivalAirplane arrivalAirplane = modelMapper.map(airplaneDTO, ArrivalAirplane.class);
                    airplaneRepository.save(arrivalAirplane);
                }
            } else {
                log.info("No items available for arrival airplanes.");
            }
        } else {
            log.info("Invalid data structure for arrival airplanes.");
        }
    }

    private void insertDepartureAirplaneItems(DepartureAirplaneDTO dto) {
        if (dto != null && dto.getResponse() != null && dto.getResponse().getBody() != null) {
            List<DepartureAirplaneDTO.Response.Body.Item> items = dto.getResponse().getBody().getItems();
            if (items != null && !items.isEmpty()) {
                for (DepartureAirplaneDTO.Response.Body.Item item : items) {
                    if (item.getGatenumber() == null || item.getGatenumber().isEmpty()) {
                        item.setGatenumber("999");
                    }
                    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
                    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime localDateTime = LocalDateTime.parse(item.getScheduleDateTime(), inputFormatter);
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
                    DepartureAirplane departureAirplane = modelMapper.map(airplaneDTO, DepartureAirplane.class);
                    departureAirplaneRepository.save(departureAirplane);
                }
            } else {
                log.info("No items available for departure airplanes.");
            }
        } else {
            log.info("Invalid data structure for departure airplanes.");
        }
    }

    public List<AirplaneDTO> findAll() {
        List<DepartureAirplane> departureAirplaneList = departureAirplaneRepository.findByisActive("Y");

        return departureAirplaneList.stream()
                .map(airplane -> modelMapper.map(airplane, AirplaneDTO.class))
                .collect(Collectors.toList());
    }

    public AirplaneDTO findByairplaneCode(int airplaneCode) {
        DepartureAirplane departureAirplane = departureAirplaneRepository.findByairplaneCode(airplaneCode);
        return modelMapper.map(departureAirplane, AirplaneDTO.class);
    }

    @Transactional
    public void modifybAirplane(int airplaneCode, AirplaneDTO modifyairplane) {
        DepartureAirplane departureAirplane = departureAirplaneRepository.findByairplaneCode(airplaneCode);
        departureAirplane = departureAirplane.toBuilder()
                .airline(modifyairplane.getAirline())
                .scheduleDateTime(modifyairplane.getScheduleDateTime())
                .remark(modifyairplane.getRemark())
                .airport(modifyairplane.getAirport())
                .flightId(modifyairplane.getFlightId())
                .gatenumber(modifyairplane.getGatenumber())
                .terminalid(modifyairplane.getTerminalid())
                .chkinrange(modifyairplane.getChkinrange())
                .build();
        departureAirplaneRepository.save(departureAirplane);
    }

    @Transactional
    public void softDelete(int airplaneCode) {
        DepartureAirplane departureAirplane = departureAirplaneRepository.findByairplaneCode(airplaneCode);
        departureAirplane = departureAirplane.toBuilder().isActive("N").build();
        departureAirplaneRepository.save(departureAirplane);
    }
}
