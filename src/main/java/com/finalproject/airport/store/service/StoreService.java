package com.finalproject.airport.store.service;

import com.finalproject.airport.approval.entity.ApprovalEntity;
import com.finalproject.airport.approval.entity.ApprovalTypeEntity;
import com.finalproject.airport.approval.repository.ApprovalRepository;
import com.finalproject.airport.member.entity.UserEntity;
import com.finalproject.airport.store.dto.StoreAPIDTO;
import com.finalproject.airport.store.dto.StoreDTO;
import com.finalproject.airport.store.dto.StoreRegistDTO;
import com.finalproject.airport.store.entity.StoreEntity;
import com.finalproject.airport.store.entity.StoreType;
import com.finalproject.airport.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreService {

    private final StoreRepository storeRepository;
    private final ApprovalRepository approvalRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<StoreDTO> getStoreList() {
        List<StoreEntity> storeList = storeRepository.findByIsActive("Y");
        List<StoreDTO> storeDTOList = new ArrayList<>();
        storeList.forEach(storeEntity -> storeDTOList.add(modelMapper.map(storeEntity, StoreDTO.class)));

        log.info("Retrieved {} active stores", storeDTOList.size());
        return storeDTOList;
    }

    public StoreDTO getStore(String storeCode) {
        StoreEntity store = storeRepository.findById(Integer.valueOf(storeCode)).orElse(null);
        StoreDTO storeDTO = modelMapper.map(store, StoreDTO.class);

        log.info("Retrieved store with code {}: {}", storeCode, storeDTO);
        return storeDTO;
    }

    public void addStore(StoreRegistDTO storeRegistDTO) {
        StoreEntity storeEntity = modelMapper.map(storeRegistDTO, StoreEntity.class);
        storeRepository.save(storeEntity);

        log.info("Added new store: {}", storeEntity);
    }

    public void softDeleteStore(int storeCode) {
        StoreEntity storeEntity = storeRepository.findById(storeCode)
                .orElseThrow(() -> new IllegalArgumentException("Store not found with code: " + storeCode));

        storeEntity = storeEntity.toBuilder().isActive("N").build();
        storeRepository.save(storeEntity);

        log.info("Soft deleted store with code {}", storeCode);
    }

    @Transactional
    public String updateStore(StoreDTO modifyStore) {
        int result = 0;

        UserEntity user = modelMapper.map(modifyStore.getApprovalRequester() , UserEntity.class);

        try {
            StoreEntity storeEntity = StoreEntity.builder()
                    .storeName(modifyStore.getStoreName())
                    .manager(modifyStore.getManager())
                    .type(modifyStore.getType())
                    .storeLocation(modifyStore.getStoreLocation())
                    .status(modifyStore.getStatus())
                    .storeContact(modifyStore.getStoreContact())
                    .storeOperatingTime(modifyStore.getStoreOperatingTime())
                    .storeExtra(modifyStore.getStoreExtra())
                    .isActive("N")
                    .approvalRequester(user)
                    .build();

            StoreEntity updatedStore = storeRepository.save(storeEntity);

            ApprovalEntity approval = new ApprovalEntity(
                    ApprovalTypeEntity.수정,
                    "N",
                    null,
                    null,
                    null,
                    null,
                    null,
                    updatedStore,
                    modifyStore.getStoreId()
            );
            approvalRepository.save(approval);
            result = 1;

            log.info("Updated store with ID {}: {}", modifyStore.getStoreId(), updatedStore);
        } catch (Exception e) {
            log.error("Error updating store with ID {}: ", modifyStore.getStoreId(), e);
            throw new RuntimeException(e);
        }
        return (result > 0) ? "점포 승인 수정 성공" : "점포 승인 수정 실패";
    }

    public void updateApi(List<StoreAPIDTO> storeDTO) {
        List<StoreEntity> storeEntityList = new ArrayList<>();
        for (StoreAPIDTO storeAPIDTO : storeDTO) {
            StoreEntity storeEntity = new StoreEntity(
                    storeAPIDTO.getEntrpskoreannm(),
                    null,
                    storeAPIDTO.getTel(),
                    storeAPIDTO.getServicetime(),
                    storeAPIDTO.getTrtmntprdlstkoreannm(),
                    "운영중",
                    StoreType.점포,
                    "담당자",
                    storeAPIDTO.getLckoreannm()
            );
            storeEntityList.add(storeEntity);
        }

        storeRepository.saveAll(storeEntityList);
        log.info("Updated stores from API with {} entries", storeEntityList.size());
    }
}
