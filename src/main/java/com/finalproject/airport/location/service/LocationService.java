package com.finalproject.airport.location.service;


import com.finalproject.airport.location.dto.LocationAPIDTO;
import com.finalproject.airport.location.entity.LocationEntity;
import com.finalproject.airport.location.entity.ZoneEntity;
import com.finalproject.airport.location.repository.LocationRepository;
import com.finalproject.airport.location.repository.ZoneRepository;

import io.github.cdimascio.dotenv.Dotenv;

import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import io.netty.channel.ChannelOption;
import reactor.core.publisher.Mono;


import java.util.Arrays;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    private final ZoneRepository zoneRepository;

    private final String storeApiUrl;

    private final String apiKey;

    @Autowired
    public LocationService(LocationRepository locationRepository, ZoneRepository zoneRepository){
        this.locationRepository = locationRepository;
        this.zoneRepository = zoneRepository;
        Dotenv dotenv = Dotenv.load();
        this.storeApiUrl = dotenv.get("STORE_API_URL");
        this.apiKey = dotenv.get("API_KEY2");
    }

    public void newLocation() {

        String requestUrl = storeApiUrl + "serviceKey=" + apiKey + "&type=json" + "&numOfRows=3000";
        System.out.println("requestUrl = " + requestUrl);

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
                .build();

        // 타임아웃을 설정하는 HttpClient 생성
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 90000) // 50초 연결 타임아웃
                .responseTimeout(Duration.ofSeconds(90)) // 50초 응답 타임아웃
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(50))
                                .addHandlerLast(new WriteTimeoutHandler(50)));

        // WebClient는 Builder 패턴 처럼 사용
        WebClient webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .uriBuilderFactory(factory)
                .exchangeStrategies(exchangeStrategies)
                .build();

        // API 요청 수행
        Mono<LocationAPIDTO> locationAPIDTOMono = webClient
                .get()
                .uri(requestUrl)
                .retrieve()
                .bodyToMono(LocationAPIDTO.class);

        // 블록 호출 및 예외 처리
        LocationAPIDTO locationAPIDTO = locationAPIDTOMono
                .doOnError(error -> System.out.println("Error occurred: " + error.getMessage()))
                .block();

        if (locationAPIDTO != null) {
            for (LocationAPIDTO.Response.Body.Item a : locationAPIDTO.getResponse().getBody().getItems()) {
                String locationName = a.getLckoreannm();
                String[] parts = locationName.split(" ");

                if (parts.length < 4) {
                    continue;
                }
                String region = parts[0];  // 제1여객터미널 등
                String floor = parts[1];   // 1층, 2층, 3층 등
                String location = String.join(" ", Arrays.copyOfRange(parts, 2, parts.length)); // 나머지 부분을 위치로 간주
                System.out.println("지역 : \n" + region + "\n 층수 : \n" + floor + "\n 위치 : \n" + location);
                ZoneEntity zone = new ZoneEntity(null, region, floor, location);
                zoneRepository.save(zone);
            }

        } else {
            System.out.println("데이터 못불러옴!");
        }
    }
}
