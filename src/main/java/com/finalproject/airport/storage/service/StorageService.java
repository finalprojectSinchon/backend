package com.finalproject.airport.storage.service;

import com.finalproject.airport.approval.entity.ApprovalEntity;
import com.finalproject.airport.approval.entity.ApprovalTypeEntity;
import com.finalproject.airport.approval.repository.ApprovalRepository;
import com.finalproject.airport.approval.service.ApprovalService;
import com.finalproject.airport.storage.dto.StorageDTO;
import com.finalproject.airport.storage.entity.StorageEntity;
import com.finalproject.airport.storage.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageService {

    @Autowired
    private final StorageRepository storageRepository;

    private final ModelMapper modelMapper;

    @Autowired
    private ApprovalService approvalService;

    @Autowired
    private ApprovalRepository approvalRepository;

    public List<StorageDTO> selectAllStorage() {
        List<StorageEntity> storage = storageRepository.findByisActive("Y");

        return storage.stream()
                .map(fact -> modelMapper.map(fact, StorageDTO.class))
                .collect(Collectors.toList());
    }

    public StorageDTO getStorage(String storageCode) {
        StorageEntity storage = storageRepository.findById(Integer.valueOf(storageCode)).orElse(null);

        return modelMapper.map(storage, StorageDTO.class);
    }

    public String addStorage(StorageDTO storageDTO) {
        int result = 0;

        try {
            StorageEntity insertStorage = StorageEntity.builder()
                    .type(storageDTO.getType())
                    .status(storageDTO.getStatus())
                    .location(storageDTO.getLocation())
                    .category(storageDTO.getCategory())
                    .department(storageDTO.getDepartment())
                    .manager(storageDTO.getManager())
                    .period(storageDTO.getPeriod())
                    .date(storageDTO.getDate())
                    .isActive("N")
                    .build();

            StorageEntity storageEntity = storageRepository.save(insertStorage);
            log.info("Saved new storage entity: {}", storageEntity);

            // 승인을 위한 정보 저장
            ApprovalEntity approval = new ApprovalEntity(
                    ApprovalTypeEntity.등록,
                    "N",
                    null,
                    null,
                    null,
                    storageEntity,
                    null
            );
            approvalRepository.save(approval);

            result = 1;
        } catch (Exception e) {
            log.error("Error adding storage: ", e);
            throw new RuntimeException(e);
        }
        return (result > 0) ? "창고 승인 요청 성공" : "창고 승인 요청 실패";
    }

    @Transactional
    public String updateStorage(StorageDTO modifyStorage) {
        int result = 0;

        try {
            StorageEntity storage = StorageEntity.builder()
                    .manager(modifyStorage.getManager())
                    .type(modifyStorage.getType())
                    .location(modifyStorage.getLocation())
                    .status(modifyStorage.getStatus())
                    .department(modifyStorage.getDepartment())
                    .category(modifyStorage.getCategory())
                    .isActive("N")
                    .build();

            StorageEntity storage1 = storageRepository.save(storage);

            ApprovalEntity approval = new ApprovalEntity(
                    ApprovalTypeEntity.수정,
                    "N",
                    null,
                    null,
                    null,
                    storage1,
                    null,
                    null,
                    modifyStorage.getStorageCode()
            );
            approvalRepository.save(approval);
            result = 1;

            log.info("Updated storage entity with code {}: {}", modifyStorage.getStorageCode(), storage1);
        } catch (Exception e) {
            log.error("Error updating storage: ", e);
            throw new RuntimeException(e);
        }
        return (result > 0) ? "창고 승인 수정 성공" : "창고 승인 수정 실패";
    }

    public void softDeleteStorage(int storageCode) {
        StorageEntity storageEntity = storageRepository.findById(storageCode)
                .orElseThrow(() -> new IllegalArgumentException("Storage not found with code: " + storageCode));

        storageEntity = storageEntity.toBuilder().isActive("N").build();
        storageRepository.save(storageEntity);

        log.info("Soft deleted storage entity with code {}", storageCode);
    }
}
