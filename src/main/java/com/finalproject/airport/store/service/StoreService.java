package com.finalproject.airport.store.service;

import com.finalproject.airport.approval.entity.ApprovalEntity;
import com.finalproject.airport.approval.entity.ApprovalTypeEntity;
import com.finalproject.airport.approval.repository.ApprovalRepository;
import com.finalproject.airport.store.dto.StoreAPIDTO;
import com.finalproject.airport.store.dto.StoreDTO;
import com.finalproject.airport.store.dto.StoreRegistDTO;
import com.finalproject.airport.store.entity.StoreEntity;
import com.finalproject.airport.store.entity.StoreType;
import com.finalproject.airport.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final ApprovalRepository approvalRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<StoreDTO> getStoreList() {
        List<StoreEntity> storeList = storeRepository.findByIsActive("Y");
        List<StoreDTO> storeDTOList = new ArrayList<>();
        storeList.forEach(storeEntity -> storeDTOList.add(modelMapper.map(storeEntity, StoreDTO.class)));

        return storeDTOList;
    }

    public StoreDTO getStore(String storeCode) {

        StoreEntity store = storeRepository.findById(Integer.valueOf(storeCode)).orElse(null);

        return modelMapper.map(store, StoreDTO.class);
    }

    public void addStore(StoreRegistDTO storeRegistDTO) {
        StoreEntity storeEntity = modelMapper.map(storeRegistDTO, StoreEntity.class);
        storeRepository.save(storeEntity);
    }

    public void softDeleteStore(int storeCode) {
        StoreEntity storeEntity = storeRepository.findById(storeCode).orElseThrow(IllegalArgumentException::new);

        storeEntity = storeEntity.toBuilder().isActive("N").build();
        storeRepository.save(storeEntity);
    }


    @Transactional
    public String updateStore(StoreDTO modifyStore) {


        int result = 0;

        try{
            StoreEntity storage =  StoreEntity.builder()
                    .storeName(modifyStore.getStoreName())
                    .manager(modifyStore.getManager())
                    .type(modifyStore.getType())
                    .storeLocation(modifyStore.getStoreLocation())
                    .status(modifyStore.getStatus())
                    .storeContact(modifyStore.getStoreContact())
                    .storeOperatingTime(modifyStore.getStoreOperatingTime())
                    .storeExtra(modifyStore.getStoreExtra())
                    .isActive("N")
                    .build();

            StoreEntity store1 = storeRepository.save(storage);

            ApprovalEntity approval = new ApprovalEntity(
                    ApprovalTypeEntity.수정,
                    "N",
                    null,
                    null,
                    null,
                    null,
                    null,
                    store1,
                    modifyStore.getStoreId()

            );
            approvalRepository.save(approval);
            result = 1;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return (result>0) ? "점포 승인 수정 성공" : "점포 승인 수정 실패";
    }

    public void updateApi(List<StoreAPIDTO> storeDTO) {
        List<StoreEntity> storeEntityList = new ArrayList<>();
        for(StoreAPIDTO storeAPIDTO : storeDTO) {
            StoreEntity storeEntity = new StoreEntity(storeAPIDTO.getEntrpskoreannm(),null,storeAPIDTO.getTel(),
                    storeAPIDTO.getServicetime(),storeAPIDTO.getTrtmntprdlstkoreannm(),"운영중", StoreType.점포,"담당자",
                    storeAPIDTO.getLckoreannm());
            storeEntityList.add(storeEntity);
        }

        storeRepository.saveAll(storeEntityList);
    }
}
