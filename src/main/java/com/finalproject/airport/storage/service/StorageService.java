package com.finalproject.airport.storage.service;

import com.finalproject.airport.approval.dto.ApprovalDTO;
import com.finalproject.airport.approval.entity.ApprovalStatusEntity;
import com.finalproject.airport.approval.entity.ApprovalTypeEntity;
import com.finalproject.airport.approval.service.ApprovalService;
import com.finalproject.airport.storage.dto.StorageDTO;
import com.finalproject.airport.storage.dto.StorageRegistDTO;
import com.finalproject.airport.storage.entity.StorageEntity;
import com.finalproject.airport.storage.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StorageService {

    @Autowired
    private final StorageRepository storageRepository;

    private final ModelMapper modelMapper;
    @Autowired
    private ApprovalService approvalService;

    public List<StorageDTO> selectAllStorage() {

        List<StorageEntity> storage = storageRepository.findByisActive("Y");

        return storage.stream()
                .map(fact -> modelMapper.map(fact, StorageDTO.class)
                ).collect(Collectors.toList());
    }

    public StorageDTO getStorage(String storageCode) {

        StorageEntity storage = storageRepository.findById(Integer.valueOf(storageCode)).orElse(null);

        return modelMapper.map(storage, StorageDTO.class);

    }

    public String addStorage(StorageRegistDTO storageRegistDTO) {

        int result = 0;

        try {

            StorageEntity insertStorage = StorageEntity.builder()
                    .type(storageRegistDTO.getType())
                    .status(storageRegistDTO.getStatus())
                    .location(
                            storageRegistDTO.getLocation())
                    .category(storageRegistDTO.getCategory())
                    .department(storageRegistDTO.getDepartment())
                    .manager(storageRegistDTO.getManager())
                    .period(storageRegistDTO.getPeriod())
                    .date(storageRegistDTO.getDate())
                    .isActive("N")
                    .build();

            //StorageEntity storageEntity = modelMapper.map(storageRegistDTO, StorageEntity.class);
            StorageEntity storageEntity = storageRepository.save(insertStorage);
            System.out.println("storageEntity = " + storageEntity);

            //승인 정보 저장
            ApprovalDTO approvalDTO = new ApprovalDTO(
                    ApprovalTypeEntity.등록,
                    "N",
                    null,
                    null,
                    null,
                    storageEntity.getStorageCode(),
                    null
            );
            System.out.println("approvalDTO = " + approvalDTO);
            approvalService.saveStorageApproval(approvalDTO);

            result = 1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return(result > 0) ? "창고 승인 요청 성공" : "창소 승인 요청 실패";
    }
    /*@Transactional
    public String insertBaggageClaim(BaggageClaimDTO baggageClaim) {

        int result = 0;

        try {

        Airplane airplane = airplaneRepository.findByAirplaneCode(baggageClaim.getAirplaneCode());

        BaggageClaim insertBaggageClaim = BaggageClaim.builder()
                .airplane(airplane) // DTO에서 가져온 비행기 정보
                .lastInspectionDate(baggageClaim.getLastInspectionDate()) // 최근 점검 날짜
                .location(baggageClaim.getLocation()) // 위치
                .manager(baggageClaim.getManager()) // 담당자
                .note(baggageClaim.getNote()) // 비고
                .status(baggageClaim.getStatus()) // 상태
                .type(baggageClaim.getType()) // 타입
                .isActive("N") // 활성화/비활성화 필드 추가
                .build();

            BaggageClaim baggageClaim1 = repository.save(insertBaggageClaim);

            // 승인 정보 저장
            ApprovalDTO approvalDTO = new ApprovalDTO(
                    ApprovalTypeEntity.등록,
                    ApprovalStatusEntity.N,
                    null,
                    null,
                    baggageClaim1.getBaggageClaimCode(),
                    null
            );

            approvalService.saveBaggageClaimApproval(approvalDTO);

            result = 1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return (result > 0) ? "수화물 수취대 승인 요청 성공" : "수화물 수취대 승인 요청 실패";




    }*/


    public void updateStorage(int storageCode, StorageDTO storageDTO) {
        storageDTO.setStorageCode(storageCode);
        storageDTO.setStatus("Y");
        StorageEntity storageEntity = modelMapper.map(storageDTO, StorageEntity.class);
        storageRepository.save(storageEntity);
    }

    public void softDeleteStorage(int storageCode) {
        StorageEntity storageEntity = storageRepository.findById(storageCode).orElseThrow(IllegalArgumentException::new);

        storageEntity = storageEntity.toBuilder().isActive("N").build();
        storageRepository.save(storageEntity);
    }

}
