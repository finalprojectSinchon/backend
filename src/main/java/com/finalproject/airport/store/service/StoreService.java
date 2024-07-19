package com.finalproject.airport.store.service;

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

    public void updateStore(int storeCode, StoreDTO storeDTO) {
        System.out.println("storeDTO = " + storeDTO);
        storeDTO.setStoreId(storeCode);
        StoreEntity storeEntity = storeRepository.findById(storeCode).orElseThrow(IllegalArgumentException::new);
        storeEntity = modelMapper.map(storeDTO, StoreEntity.class);
        storeRepository.save(storeEntity);
    }

    public void updateApi(List<StoreAPIDTO> storeDTO) {
        List<StoreEntity> storeEntityList = new ArrayList<>();
        for(StoreAPIDTO storeAPIDTO : storeDTO) {
            StoreEntity storeEntity = new StoreEntity(storeAPIDTO.getEntrpskoreannm(),null,storeAPIDTO.getTel(),
                    storeAPIDTO.getServicetime(),storeAPIDTO.getTrtmntprdlstkoreannm(),"운영중", StoreType.점포,"담당자");
            storeEntityList.add(storeEntity);
        }

        storeRepository.saveAll(storeEntityList);
    }
}
