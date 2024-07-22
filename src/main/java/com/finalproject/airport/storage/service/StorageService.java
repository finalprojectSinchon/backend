package com.finalproject.airport.storage.service;

import com.finalproject.airport.storage.dto.StorageDTO;
import com.finalproject.airport.storage.dto.StorageRegistDTO;
import com.finalproject.airport.storage.entity.StorageEntity;
import com.finalproject.airport.storage.repository.StorageRepository;
import com.finalproject.airport.store.dto.StoreRegistDTO;
import com.finalproject.airport.store.entity.StoreEntity;
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
    public List<StorageDTO> selectAllStorage() {

        List<StorageEntity> storage = storageRepository.findAll();

        return storage.stream()
                .map(fact -> modelMapper.map(fact, StorageDTO.class)
                ).collect(Collectors.toList());
    }

    public StorageDTO getStorage(String storageCode) {

        StorageEntity storage = storageRepository.findById(Integer.valueOf(storageCode)).orElse(null);

        return modelMapper.map(storage, StorageDTO.class);

    }

    public void addStorage(StorageRegistDTO storageRegistDTO) {
        System.out.println("storageRegistDTO = " + storageRegistDTO);
        StorageEntity storageEntity = modelMapper.map(storageRegistDTO, StorageEntity.class);
        System.out.println("storageEntity = " + storageEntity);
        storageRepository.save(storageEntity);
    }

    public void updateStorage(int storageCode, StorageDTO storageDTO) {
        storageDTO.setStorageCode(storageCode);
        storageDTO.setStorageStatus("Y");
        StorageEntity storageEntity = modelMapper.map(storageDTO, StorageEntity.class);
        storageRepository.save(storageEntity);
    }

    public void softDeleteStorage(int storageCode) {
        StorageEntity storageEntity = storageRepository.findById(storageCode).orElseThrow(IllegalArgumentException::new);

        storageEntity = storageEntity.toBuilder().isActive("N").build();
        storageRepository.save(storageEntity);
    }

}
