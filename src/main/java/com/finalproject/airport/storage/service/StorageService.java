package com.finalproject.airport.storage.service;

import com.finalproject.airport.airplane.gate.dto.GateDTO;
import com.finalproject.airport.airplane.gate.entity.Gate;
import com.finalproject.airport.approval.dto.ApprovalDTO;
import com.finalproject.airport.approval.entity.ApprovalEntity;
import com.finalproject.airport.approval.entity.ApprovalStatusEntity;
import com.finalproject.airport.approval.entity.ApprovalTypeEntity;
import com.finalproject.airport.approval.repository.ApprovalRepository;
import com.finalproject.airport.approval.service.ApprovalService;
import com.finalproject.airport.storage.dto.StorageDTO;
import com.finalproject.airport.storage.dto.StorageRegistDTO;
import com.finalproject.airport.storage.entity.StorageEntity;
import com.finalproject.airport.storage.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private ApprovalRepository approvalRepository;

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
            System.out.println("storageEntity = " + storageEntity);

            //승인 정보 저장
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
            throw new RuntimeException(e);
        }
        return(result > 0) ? "창고 승인 요청 성공" : "창소 승인 요청 실패";
    }



    @Transactional
    public String updateStorage(StorageDTO modifyStorage) {


        int result = 0;

        try{
            StorageEntity storage =  StorageEntity.builder()
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
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return (result>0) ? "창고 승인 수정 성공" : "창고 승인 수정 실패";
    }

    public void softDeleteStorage(int storageCode) {
        StorageEntity storageEntity = storageRepository.findById(storageCode).orElseThrow(IllegalArgumentException::new);

        storageEntity = storageEntity.toBuilder().isActive("N").build();
        storageRepository.save(storageEntity);
    }

}
