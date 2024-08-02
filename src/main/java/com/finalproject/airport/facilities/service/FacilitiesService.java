package com.finalproject.airport.facilities.service;

import com.finalproject.airport.approval.dto.ApprovalDTO;
import com.finalproject.airport.approval.entity.ApprovalStatusEntity;
import com.finalproject.airport.approval.entity.ApprovalTypeEntity;
import com.finalproject.airport.approval.service.ApprovalService;
import com.finalproject.airport.facilities.dto.FacilitiesDTO;
import com.finalproject.airport.facilities.entity.FacilitesType;
import com.finalproject.airport.facilities.entity.FacilitiesEntity;
import com.finalproject.airport.facilities.repository.FacilitiesRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static ch.qos.logback.classic.spi.ThrowableProxyVO.build;

@Service
@RequiredArgsConstructor
public class FacilitiesService {

    @Autowired
    private final FacilitiesRepository facilitiesRepository;

    private final ModelMapper modelMapper;
    @Autowired
    private ApprovalService approvalService;

    public List<FacilitiesDTO> selectAllFacilities() {


        List<FacilitiesEntity> facilities = facilitiesRepository.findAllByIsActive("Y");


        return facilities.stream()
                .map(fact -> modelMapper.map(fact, FacilitiesDTO.class)
                ).collect(Collectors.toList());
    }


    public FacilitiesDTO findFacilities(int facilitiesCode) {

        FacilitiesEntity findfacilities =facilitiesRepository.findById(facilitiesCode).orElseThrow();

        findfacilities.getFacilitiesCode();

        FacilitiesDTO facilitiesDTO =new FacilitiesDTO();
//        facilitiesDTO.setFacilitiesCode(facilitiesCode);
//        facilitiesDTO.setFacilitiesName(findfacilities.getFacilitiesName());
//        facilitiesDTO.setFacilitiesLocation(findfacilities.getFacilitiesLocation());
//        facilitiesDTO.setFacilitiesManager(findfacilities.getFacilitiesManager());
//        facilitiesDTO.setFacilitiesType(findfacilities.getFacilitiesType());

        FacilitiesDTO facilitiesDTO2 = new FacilitiesDTO(
                facilitiesCode,
                findfacilities.getStatus(),
                findfacilities.getLocation(),
                findfacilities.getFacilitiesName(),
                findfacilities.getFacilitiesType(),
                findfacilities.getManager(),
                findfacilities.getFacilitiesClass(),
                findfacilities.getIsActive(),
                findfacilities.getCreatedDate()
        );
      return facilitiesDTO2;
    }

    // 등록
    @Transactional
    public String insertFacilities(FacilitiesDTO facilitiesDTO) {

        int result = 0;

        try {

            FacilitiesEntity insertFacilities = FacilitiesEntity.builder()
                    .status(facilitiesDTO.getStatus())
                    .location(facilitiesDTO.getLocation())
                    .facilitiesName(facilitiesDTO.getFacilitiesName())
                    .facilitiesType(facilitiesDTO.getType())
                    .manager(facilitiesDTO.getManager())
                    .facilitiesClass(facilitiesDTO.getFacilitiesClass())
                    .isActive("N")
                    .build();

            FacilitiesEntity facilitiesEntity = facilitiesRepository.save(insertFacilities);
            System.out.println("insertFacilities = " + insertFacilities);

            ApprovalDTO approvalDTO = new ApprovalDTO(
                    ApprovalTypeEntity.등록,
                    ApprovalStatusEntity.N,
                    null,
                    null,
                    null,
                    null,
                    facilitiesEntity.getFacilitiesCode()
            );
            System.out.println("approvalDTO = " + approvalDTO);
            approvalService.saveFacilities(approvalDTO);

            result = 1;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return (result > 0) ? "편의시설 승인 요청 성공" : "편의시설 승인 요청 실패";

    }



@Transactional
    public void updateFacilities(int facilitiesCode , FacilitiesDTO facilitiesDTO) {

        FacilitiesEntity findUpdateFacilities = facilitiesRepository.findById(facilitiesCode).orElseThrow(IllegalArgumentException::new);


       FacilitiesEntity update = findUpdateFacilities.toBuilder()
//                .facilitiesCode(facilitiesCode)
                .facilitiesClass(facilitiesDTO.getFacilitiesClass())
                .location(facilitiesDTO.getLocation())
                .facilitiesName(facilitiesDTO.getFacilitiesName())
                .manager(facilitiesDTO.getManager())
                .facilitiesType(facilitiesDTO.getType())
                .status(facilitiesDTO.getStatus()).build();

        facilitiesRepository.save(update);
    }

    public void deleteFacilities(int facilitiesCode) {

        FacilitiesEntity deleteFacilities = facilitiesRepository.findById(facilitiesCode).orElseThrow(IllegalArgumentException::new);

        FacilitiesEntity delete = deleteFacilities.toBuilder()

                .isActive("N").build();

        facilitiesRepository.save(delete);
    }
}






