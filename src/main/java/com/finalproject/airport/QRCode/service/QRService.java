package com.finalproject.airport.QRCode.service;

import com.finalproject.airport.QRCode.dto.*;
import com.finalproject.airport.airplane.baggageclaim.dto.BaggageClaimDTO;
import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaim;
import com.finalproject.airport.airplane.baggageclaim.repository.BaggageClaimRepository;
import com.finalproject.airport.airplane.checkincounter.entity.CheckinCounter;
import com.finalproject.airport.airplane.checkincounter.repository.CheckinCounterRepository;
import com.finalproject.airport.airplane.gate.entity.Gate;
import com.finalproject.airport.airplane.gate.repository.GateRepository;
import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.facilities.entity.FacilitiesEntity;
import com.finalproject.airport.facilities.repository.FacilitiesRepository;
import com.finalproject.airport.gptapi.dto.request.GPTRequestDTO;
import com.finalproject.airport.gptapi.dto.response.GPTResponseDTO;
import com.finalproject.airport.gptapi.service.GPTService;
import com.finalproject.airport.inspection.entity.InspectionEntity;
import com.finalproject.airport.inspection.respository.InspectionRepository;
import com.finalproject.airport.location.entity.LocationEntity;
import com.finalproject.airport.location.repository.LocationRepository;
import com.finalproject.airport.storage.entity.StorageEntity;
import com.finalproject.airport.storage.repository.StorageRepository;
import com.finalproject.airport.store.dto.StoreDTO;
import com.finalproject.airport.store.entity.StoreEntity;
import com.finalproject.airport.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class QRService {

    private final ModelMapper modelMapper;

    private final BaggageClaimRepository baggageClaimRepository;

    private final StoreRepository storeRepository;

    private final GateRepository gateRepository;

    private final CheckinCounterRepository checkinCounterRepository;

    private final StorageRepository storageRepository;

    private final FacilitiesRepository facilitiesRepository;

    private final LocationRepository locationRepository;

    private final InspectionRepository inspectionRepository;

    private final GPTService gptService;

    public ResponseEntity<?> getQRList(String qrType) {

        log.info("Request received to get QR list for type: {}", qrType);

        QRAllDTO qrAllDTO = new QRAllDTO();

        switch (qrType) {
            case "baggage_claim":
                List<BaggageClaim> baggageClaimList = baggageClaimRepository.findByisActive("Y");
                List<BaggageClaimQRDTO> baggageClaimDTOList = new ArrayList<>();
                for (BaggageClaim baggageClaim : baggageClaimList) {
                    BaggageClaimQRDTO baggageClaimQRDTO = new BaggageClaimQRDTO(baggageClaim.getBaggageClaimCode(), baggageClaim.getType(), baggageClaim.getBaggageClaimCode() + "번 수화물 수취대");
                    baggageClaimDTOList.add(baggageClaimQRDTO);
                }
                qrAllDTO.setBaggageClaim(baggageClaimDTOList);
                log.info("Retrieved {} baggage claims.", baggageClaimDTOList.size());
                break;

            case "gate":
                List<Gate> gateList = gateRepository.findByisActive("Y");
                List<GateQRDTO> gateQRDTOList = new ArrayList<>();
                for (Gate gate : gateList) {
                    GateQRDTO gateQRDTO = new GateQRDTO(gate.getGateCode(), gate.getGateCode() + "번 탑승구", gate.getType());
                    gateQRDTOList.add(gateQRDTO);
                }
                qrAllDTO.setGate(gateQRDTOList);
                log.info("Retrieved {} gates.", gateQRDTOList.size());
                break;

            case "checkin_counter":
                List<CheckinCounter> checkinCounterList = checkinCounterRepository.findByisActive("Y");
                List<CheckInCounterQRDTO> checkinCounterQRDTOList = new ArrayList<>();
                for (CheckinCounter checkinCounter : checkinCounterList) {
                    CheckInCounterQRDTO checkInCounterQRDTO = new CheckInCounterQRDTO(checkinCounter.getCheckinCounterCode(), checkinCounter.getLocation(), checkinCounter.getType());
                    checkinCounterQRDTOList.add(checkInCounterQRDTO);
                }
                qrAllDTO.setCheckInCounter(checkinCounterQRDTOList);
                log.info("Retrieved {} check-in counters.", checkinCounterQRDTOList.size());
                break;

            case "store":
                List<StoreEntity> storeList = storeRepository.findByIsActive("Y");
                List<StoreQRDTO> storeDTOList = new ArrayList<>();
                for (StoreEntity storeEntity : storeList) {
                    StoreQRDTO storeQRDTO = new StoreQRDTO(storeEntity.getStoreId(), storeEntity.getStoreName(), storeEntity.getStoreLocation());
                    storeDTOList.add(storeQRDTO);
                }
                qrAllDTO.setStore(storeDTOList);
                log.info("Retrieved {} stores.", storeDTOList.size());
                break;

            case "storage":
                List<StorageEntity> storageList = storageRepository.findByisActive("Y");
                List<StorageQRDTO> storageQRDTOList = new ArrayList<>();
                for (StorageEntity storageEntity : storageList) {
                    StorageQRDTO storageQRDTO = new StorageQRDTO(storageEntity.getStorageCode(), storageEntity.getLocation(), storageEntity.getType());
                    storageQRDTOList.add(storageQRDTO);
                }
                qrAllDTO.setStorage(storageQRDTOList);
                log.info("Retrieved {} storage entities.", storageQRDTOList.size());
                break;

            case "facilities":
                List<FacilitiesEntity> facilitiesEntityList = facilitiesRepository.findAllByIsActive("Y");
                List<FacilityQRDTO> facilityQRDTOList = new ArrayList<>();
                for (FacilitiesEntity facilitiesEntity : facilitiesEntityList) {
                    FacilityQRDTO facilityQRDTO = new FacilityQRDTO(facilitiesEntity.getFacilitiesCode(), facilitiesEntity.getLocation(), facilitiesEntity.getFacilitiesName());
                    facilityQRDTOList.add(facilityQRDTO);
                }
                qrAllDTO.setFacilities(facilityQRDTOList);
                log.info("Retrieved {} facilities.", facilityQRDTOList.size());
                break;

            case "show_all":
                List<BaggageClaim> allBaggageClaimList = baggageClaimRepository.findByisActive("Y");
                List<BaggageClaimQRDTO> allBaggageClaimDTOList = new ArrayList<>();
                for (BaggageClaim baggageClaim : allBaggageClaimList) {
                    BaggageClaimQRDTO baggageClaimQRDTO = new BaggageClaimQRDTO(
                            baggageClaim.getBaggageClaimCode(),
                            baggageClaim.getType(),
                            baggageClaim.getBaggageClaimCode() + "번 수화물 수취대"
                    );
                    allBaggageClaimDTOList.add(baggageClaimQRDTO);
                }
                qrAllDTO.setBaggageClaim(allBaggageClaimDTOList);

                List<Gate> allGateList = gateRepository.findByisActive("Y");
                List<GateQRDTO> allGateQRDTOList = new ArrayList<>();
                for (Gate gate : allGateList) {
                    GateQRDTO gateQRDTO = new GateQRDTO(
                            gate.getGateCode(),
                            gate.getGateCode() + "번 탑승구",
                            gate.getType()
                    );
                    allGateQRDTOList.add(gateQRDTO);
                }
                qrAllDTO.setGate(allGateQRDTOList);

                List<CheckinCounter> allCheckinCounterList = checkinCounterRepository.findByisActive("Y");
                List<CheckInCounterQRDTO> allCheckinCounterQRDTOList = new ArrayList<>();
                for (CheckinCounter checkinCounter : allCheckinCounterList) {
                    CheckInCounterQRDTO checkInCounterQRDTO = new CheckInCounterQRDTO(
                            checkinCounter.getCheckinCounterCode(),
                            checkinCounter.getLocation(),
                            checkinCounter.getType()
                    );
                    allCheckinCounterQRDTOList.add(checkInCounterQRDTO);
                }
                qrAllDTO.setCheckInCounter(allCheckinCounterQRDTOList);

                List<StoreEntity> allStoreList = storeRepository.findByIsActive("Y");
                List<StoreQRDTO> allStoreDTOList = new ArrayList<>();
                for (StoreEntity storeEntity : allStoreList) {
                    StoreQRDTO storeQRDTO = new StoreQRDTO(
                            storeEntity.getStoreId(),
                            storeEntity.getStoreName(),
                            storeEntity.getStoreLocation()
                    );
                    allStoreDTOList.add(storeQRDTO);
                }
                qrAllDTO.setStore(allStoreDTOList);

                List<StorageEntity> allStorageList = storageRepository.findByisActive("Y");
                List<StorageQRDTO> allStorageQRDTOList = new ArrayList<>();
                for (StorageEntity storageEntity : allStorageList) {
                    StorageQRDTO storageQRDTO = new StorageQRDTO(
                            storageEntity.getStorageCode(),
                            storageEntity.getLocation(),
                            storageEntity.getType()
                    );
                    allStorageQRDTOList.add(storageQRDTO);
                }
                qrAllDTO.setStorage(allStorageQRDTOList);

                List<FacilitiesEntity> allFacilitiesEntityList = facilitiesRepository.findAllByIsActive("Y");
                List<FacilityQRDTO> allFacilityQRDTOList = new ArrayList<>();
                for (FacilitiesEntity facilitiesEntity : allFacilitiesEntityList) {
                    FacilityQRDTO facilityQRDTO = new FacilityQRDTO(
                            facilitiesEntity.getFacilitiesCode(),
                            facilitiesEntity.getLocation(),
                            facilitiesEntity.getFacilitiesName()
                    );
                    allFacilityQRDTOList.add(facilityQRDTO);
                }
                qrAllDTO.setFacilities(allFacilityQRDTOList);
                log.info("Retrieved all QR codes for all types.");
                break;

            default:
                log.error("Invalid QR type: {}", qrType);
                return ResponseEntity.badRequest().body(new ResponseDTO(HttpStatus.BAD_REQUEST, "Invalid QR type", null));
        }

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "QR코드 생성 시설물 조회성공", qrAllDTO));
    }

    public ResponseEntity<?> getQRDetail(String type, int code) {
        log.info("Request received to get QR detail for type: {}, code: {}", type, code);

        FacilityDetailDTO location = new FacilityDetailDTO();
        LocationEntity locationEntity = null;
        switch (type) {
            case "store":
                StoreEntity store = storeRepository.findById(code).orElse(null);
                if (store == null) {
                    log.error("Store with code {} not found.", code);
                    return ResponseEntity.notFound().build();
                }
                location.setLocation(store.getStoreLocation());
                break;

            case "baggage_claim":
                locationEntity = locationRepository.findByBaggageClaimCode(code);
                if (locationEntity == null) {
                    log.error("Baggage claim location with code {} not found.", code);
                    return ResponseEntity.notFound().build();
                }
                location.setLocation(locationEntity.getZone().getRegion() + " " + locationEntity.getZone().getFloor() + " " + locationEntity.getZone().getLocation());
                break;

            case "gate":
                location.setLocation(code + "번 게이트");
                break;

            case "checkin_counter":
                locationEntity = locationRepository.findByCheckinCounterCode(code);
                if (locationEntity == null) {
                    log.error("Check-in counter location with code {} not found.", code);
                    return ResponseEntity.notFound().build();
                }
                location.setLocation(locationEntity.getZone().getRegion() + " " + locationEntity.getZone().getFloor());
                break;

            case "storage":
                locationEntity = locationRepository.findByStorageCode(code);
                if (locationEntity == null) {
                    log.error("Storage location with code {} not found.", code);
                    return ResponseEntity.notFound().build();
                }
                location.setLocation(locationEntity.getZone().getRegion() + " " + locationEntity.getZone().getFloor());
                break;

            case "facilities":
                locationEntity = locationRepository.findByFacilitiesCode(code);
                if (locationEntity == null) {
                    log.error("Facilities location with code {} not found.", code);
                    return ResponseEntity.notFound().build();
                }
                location.setLocation(locationEntity.getZone().getRegion() + " " + locationEntity.getZone().getFloor());
                break;

            default:
                log.error("Invalid QR type: {}", type);
                return ResponseEntity.badRequest().body(new ResponseDTO(HttpStatus.BAD_REQUEST, "Invalid QR type", null));
        }

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "QR코드 시설물 위 조회성공", location));
    }

    public ResponseEntity<?> joinQR(JoinQRDTO info) {
        log.info("Request received to join QR with info: {}", info);

        // 날짜 형식 정의 및 파싱
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parseDate;
        try {
            parseDate = dateFormat.parse(info.getRegularInspectionDate());
        } catch (ParseException e) {
            log.error("Invalid date format: {}", info.getRegularInspectionDate(), e);
            return ResponseEntity.badRequest().body("Invalid date format.");
        }

        // Builder 패턴을 사용하여 기본 엔티티 생성
        InspectionEntity.InspectionEntityBuilder inspectionEntityBuilder = InspectionEntity.builder()
                .manager(info.getManager())
                .location(info.getLocation())
                .regularInspectionDate(parseDate)
                .type(info.getType())
                .status(info.getStatus())
                .phone(info.getPhone())
                .text(info.getText());

        // Type에 따른 처리
        switch (info.getType()) {
            case "gate":
                Gate gate = gateRepository.findBygateCode(info.getAirplaneCode());
                if (gate == null) {
                    log.error("Gate with code {} not found.", info.getAirplaneCode());
                    return ResponseEntity.badRequest().body("Gate not found.");
                }
                gateRepository.save(gate);
                inspectionEntityBuilder.gate(gate);
                break;

            case "checkinCounter":
                CheckinCounter checkinCounter = checkinCounterRepository.findBycheckinCounterCode(info.getAirplaneCode());
                if (checkinCounter == null) {
                    log.error("Check-in counter with code {} not found.", info.getAirplaneCode());
                    return ResponseEntity.badRequest().body("CheckinCounter not found.");
                }
                checkinCounterRepository.save(checkinCounter);
                inspectionEntityBuilder.checkinCounter(checkinCounter);
                break;

            case "baggageClaim":
                BaggageClaim baggageClaim = baggageClaimRepository.findBybaggageClaimCode(info.getAirplaneCode());
                if (baggageClaim == null) {
                    log.error("Baggage claim with code {} not found.", info.getAirplaneCode());
                    return ResponseEntity.badRequest().body("BaggageClaim not found.");
                }
                baggageClaimRepository.save(baggageClaim);
                inspectionEntityBuilder.baggageClaim(baggageClaim);
                break;

            case "store":
                StoreEntity store = storeRepository.findById(info.getAirplaneCode()).orElse(null);
                if (store == null) {
                    log.error("Store with code {} not found.", info.getAirplaneCode());
                    return ResponseEntity.badRequest().body("Store not found.");
                }
                storeRepository.save(store);
                inspectionEntityBuilder.store(store);
                break;

            case "storage":
                StorageEntity storageEntity = storageRepository.findById(info.getAirplaneCode()).orElse(null);
                if (storageEntity == null) {
                    log.error("Storage entity with code {} not found.", info.getAirplaneCode());
                    return ResponseEntity.badRequest().body("StorageEntity not found.");
                }
                storageRepository.save(storageEntity);
                inspectionEntityBuilder.storage(storageEntity);
                break;

            case "facilities":
                FacilitiesEntity facilities = facilitiesRepository.findById(info.getAirplaneCode()).orElse(null);
                if (facilities == null) {
                    log.error("Facilities entity with code {} not found.", info.getAirplaneCode());
                    return ResponseEntity.badRequest().body("FacilitiesEntity not found.");
                }
                facilitiesRepository.save(facilities);
                inspectionEntityBuilder.facilities(facilities);
                break;

            default:
                log.error("Invalid type: {}", info.getType());
                return ResponseEntity.badRequest().body("Invalid type.");
        }

        InspectionEntity inspectionEntity = inspectionEntityBuilder.build();

        String inspectionContent = inspectionEntity.toString();
        log.info("Generated inspection content: {}", inspectionContent);

        GPTRequestDTO.Message message = new GPTRequestDTO.Message();
        message.setRole("user");
        message.setContent(inspectionContent);

        GPTRequestDTO gptRequestDTO = new GPTRequestDTO();
        gptRequestDTO.setMessages(Collections.singletonList(message));

        String prompt = "\"GPT 모델에게 이 프롬프트를 사용하여 보고서를 작성해달라고 요청하세요. 보고서 제목과 각 섹션의 내용은 아래와 같은 형식을 따릅니다:\n" +
                "\n" +
                "1. 보고서 제목은 문서의 상단에 큰 글씨로 작성됩니다.\n" +
                "2. 서론과 결론 섹션은 간단한 html 형식으로 작성됩니다.\n" +
                "3. 본문은 html 사용하여 왼쪽과 오른쪽으로 나누어 정보를 제공합니다.\n" +
                "4. 제목, 본문, 리스트, 테이블 등을 포함할 수 있습니다.\n" +
                "5. htm l을  적절히 조합하여 문서의 스타일을 조정하세요.\n" +
                "6. 2000 자 정도를 채워주세요." +
                "7. 한국어로 작성하세요!" +
                "8. html 만 사용해야 합니다. css 는 사용 할 수 없습니다.";

        GPTResponseDTO createText = gptService.getChatCompletion(gptRequestDTO, prompt);
        log.info("Received GPT response.");

        inspectionEntity = inspectionEntity.toBuilder()
                .text(createText.getChoices().get(0).getMessage().getContent())
                .build();

        inspectionRepository.save(inspectionEntity);
        log.info("Inspection entity saved with generated text.");

        return ResponseEntity.ok().build();
    }
}
