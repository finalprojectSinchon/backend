package com.finalproject.airport.storage.service;

import com.finalproject.airport.storage.dto.StorageDTO;
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
    public List<StorageDTO> selectAllStorage() {

        List<StorageEntity> storage = storageRepository.findAll();

        return storage.stream()
                .map(fact -> modelMapper.map(fact, StorageDTO.class)
                ).collect(Collectors.toList());
    }

    public StorageDTO getstorage(String storageCode) {

        StorageEntity storage = storageRepository.findById(Integer.valueOf(storageCode)).orElse(null);

        return modelMapper.map(storage, StorageDTO.class);

    }
}
