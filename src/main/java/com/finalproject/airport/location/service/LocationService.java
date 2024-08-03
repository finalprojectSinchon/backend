package com.finalproject.airport.location.service;


import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.location.dto.FindZoneDTO;
import com.finalproject.airport.location.dto.LocationAPIDTO;
import com.finalproject.airport.location.dto.ZoneDTO;
import com.finalproject.airport.location.entity.LocationEntity;
import com.finalproject.airport.location.entity.ZoneEntity;
import com.finalproject.airport.location.repository.LocationRepository;
import com.finalproject.airport.location.repository.ZoneRepository;

import io.github.cdimascio.dotenv.Dotenv;

import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import io.netty.channel.ChannelOption;
import reactor.core.publisher.Mono;


import java.util.*;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    private final ZoneRepository zoneRepository;

    private final String storeApiUrl;

    private final String apiKey;

    private final ModelMapper modelMapper;

    @Autowired
    public LocationService(LocationRepository locationRepository, ZoneRepository zoneRepository, ModelMapper modelMapper){
        this.locationRepository = locationRepository;
        this.zoneRepository = zoneRepository;
        Dotenv dotenv = Dotenv.load();
        this.storeApiUrl = dotenv.get("STORE_API_URL");
        this.apiKey = dotenv.get("API_KEY2");
        this.modelMapper = modelMapper;
    }

    public void newLocation() {

        String requestUrl = storeApiUrl + "serviceKey=" + apiKey + "&type=json" + "&numOfRows=3000";

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
                ZoneEntity zone = new ZoneEntity(null, region, floor, location);
                zoneRepository.save(zone);
            }

        } else {
            System.out.println("데이터 못불러옴!");
        }
    }

    public ResponseEntity<?> getRegion() {
        try {
            List<ZoneEntity> regionList = zoneRepository.findAll();
            Set<String> uniqueRegions = new HashSet<>();
            List<Map<String, Object>> regions = new ArrayList<>();

            for (ZoneEntity region : regionList) {
                if (uniqueRegions.add(region.getRegion())) {
                    Map<String, Object> regionMap = new HashMap<>();
                    regionMap.put("region", region.getRegion());
                    regions.add(regionMap);
                }
            }

            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "정상적으로 지역을 조회 하였습니다.", regions));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    public ResponseEntity<?> getFloor(String region) {

        try {
            List<ZoneEntity> zoneList = zoneRepository.findAllByRegion(region);

            Set<String> uniqueFloors = new HashSet<>();
            List<Map<String, Object>> floors = new ArrayList<>();

            for (ZoneEntity zone : zoneList) {
                if (uniqueFloors.add(zone.getFloor())) {
                    Map<String, Object> floorMap = new HashMap<>();
                    floorMap.put("floor", zone.getFloor());
                    floors.add(floorMap);
                }
            }

            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "층수 불러오기에 성공하였습니다.", floors));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    public ResponseEntity<?> getLocation(String region, String floor) {

        try {
            List<ZoneEntity> zoneList = zoneRepository.findAllByRegionAndFloor(region, floor);

            Set<String> uniqueLocations = new HashSet<>();
            List<Map<String, Object>> locations = new ArrayList<>();
            for (ZoneEntity zone : zoneList) {
                if (uniqueLocations.add(zone.getLocation())) {
                    Map<String, Object> locationMap = new HashMap<>();
                    locationMap.put("locationOne", zone.getLocation());
                    locations.add(locationMap);
                }
            }
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "위치 불러오기에 성공하였습니다.", locations));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    public ResponseEntity<?> addLocation(ZoneDTO zone) {

        String zoneType = zone.getZoneType();
        List<ZoneEntity> zoneEntities = zoneRepository.findByRegionAndFloorAndLocation(zone.getRegion(), zone.getFloor(), zone.getLocation());


        ZoneEntity zoneEntity;
        if (zoneEntities.size() == 1) {
            zoneEntity = zoneEntities.get(0);
        } else if (zoneEntities.isEmpty()) {
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"위치 코드를 찾을 수 없음",null));
        } else {
            zoneEntity = zoneEntities.get(0);
        }


        switch (zoneType) {
            case "facilities" :
                Boolean isFacilities = locationRepository.existsByFacilitiesCode(zone.getAirportCode());
                if (isFacilities) {
                    LocationEntity locationEntity = locationRepository.findByFacilitiesCode(zone.getAirportCode());
                    if (locationEntity != null) {
                        ZoneEntity zoneEntity1 = new ZoneEntity(null, zone.getRegion(), zone.getFloor(), zone.getLocation());
                        locationEntity = locationEntity.toBuilder()
                                .zone(zoneEntity1)
                                .build();
                        locationRepository.save(locationEntity);
                    } else {
                        return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"위치를 찾을 수 없음",null));
                    }
                } else {
                    LocationEntity locationEntity = new LocationEntity();
                    locationEntity = locationEntity.toBuilder()
                            .zone(zoneEntity)
                            .facilitiesCode(zone.getAirportCode())
                            .build();
                    locationRepository.save(locationEntity);
                }
                break;
            case "storage" :
                Boolean isStorage = locationRepository.existsByStorageCode(zone.getAirportCode());
                if (isStorage) {
                    LocationEntity locationEntity = locationRepository.findByStorageCode(zone.getAirportCode());
                    if (locationEntity != null) {
                        ZoneEntity zoneEntity1 = new ZoneEntity(null, zone.getRegion(), zone.getFloor(), zone.getLocation());
                        locationEntity = locationEntity.toBuilder()
                                .zone(zoneEntity1)
                                .build();

                        locationRepository.save(locationEntity);
                    } else {
                        return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"위치를 찾을 수 없음",null));
                    }
                } else {
                    LocationEntity locationEntity = new LocationEntity();
                    locationEntity = locationEntity.toBuilder()
                            .storageCode(zone.getAirportCode())
                            .zone(zoneEntity)
                            .build();
                    locationRepository.save(locationEntity);
                }
                break;
            case "baggageClaim" :
                Boolean isBaggageClaim = locationRepository.existsByBaggageClaimCode(zone.getAirportCode());
                if (isBaggageClaim) {
                    LocationEntity locationEntity = locationRepository.findByBaggageClaimCode(zone.getAirportCode());
                    if (locationEntity != null) {

                        ZoneEntity zoneEntity1 = new ZoneEntity(null, zone.getRegion(), zone.getFloor(), zone.getLocation());

                        ZoneEntity savedZoneEntity = zoneRepository.save(zoneEntity1);

                        locationEntity = locationEntity.toBuilder()
                                .zone(savedZoneEntity)
                                .build();

                        locationRepository.save(locationEntity);
                    } else {
                        return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR,"위치를 찾을 수 없음",null));
                    }
                } else {
                    LocationEntity locationEntity = LocationEntity.builder()
                            .baggageClaimCode(zone.getAirportCode())
                            .zone(zoneEntity)
                            .build();
                    locationRepository.save(locationEntity);
                }
                break;
        }


        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> getTypeOfLocation(String type, int code) {

        FindZoneDTO zone = null;
        switch (type){
            case "facilities" :
                LocationEntity locationForFacilities = locationRepository.findByFacilitiesCode(code);
                zone = modelMapper.map(locationForFacilities.getZone(), FindZoneDTO.class);
                break;
            case "storage" :
                LocationEntity locationForStorage = locationRepository.findByStorageCode(code);
                zone = modelMapper.map(locationForStorage.getZone(), FindZoneDTO.class);
                break;
            case "baggageClaim" :
                LocationEntity locationForBaggageClaim = locationRepository.findByBaggageClaimCode(code);
                zone = modelMapper.map(locationForBaggageClaim.getZone(), FindZoneDTO.class);
                break;
        }


        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"조회 성공",zone));
    }

    public ResponseEntity<?> getStorageLocation() {

        List<LocationEntity> locations = locationRepository.findAllWithStorageCode();

        List<Map<String,Object>> zones = new ArrayList<>();
        for(LocationEntity location : locations) {
            String zone = location.getZone().getRegion() + " " + location.getZone().getFloor() + " " + location.getZone().getLocation();
            int zoneCode = location.getZone().getZoneCode();
            Map<String, Object> locationMap = new HashMap<>();
            locationMap.put("zoneCode", zoneCode);
            locationMap.put("zone", zone);
            zones.add(locationMap);
        }

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"창고 조회 성공",zones));
    }

    public void registEquipment(Integer equipmentId, int zoneCode) {
        ZoneEntity zone = zoneRepository.findById(zoneCode).orElseThrow();

        LocationEntity locationEntity = new LocationEntity();
        locationEntity = locationEntity.toBuilder()
                .equipmentCode(equipmentId)
                .zone(zone)
                .build();

        locationRepository.save(locationEntity);
    }
}
