package com.finalproject.airport.facilities.service;

import com.finalproject.airport.approval.dto.ApprovalDTO;
import com.finalproject.airport.approval.entity.ApprovalEntity;
import com.finalproject.airport.approval.entity.ApprovalStatusEntity;
import com.finalproject.airport.approval.entity.ApprovalTypeEntity;
import com.finalproject.airport.approval.repository.ApprovalRepository;
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
    @Autowired
    private ApprovalRepository approvalRepository;

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
                findfacilities.getCreatedDate(),
                findfacilities.getNote()
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
                    .note(facilitiesDTO.getNote())
                    .isActive("N")
                    .build();

            FacilitiesEntity facilitiesEntity = facilitiesRepository.save(insertFacilities);
            System.out.println("insertFacilities = " + insertFacilities);

            ApprovalEntity approval = new ApprovalEntity(
                    ApprovalTypeEntity.등록,
                    "N",
                    null,
                    null,
                    null,
                    null,
                    facilitiesEntity
            );

            approvalRepository.save(approval);

            result = 1;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return (result > 0) ? "편의시설 승인 요청 성공" : "편의시설 승인 요청 실패";

    }



@Transactional
    public String modifyFacilities(FacilitiesDTO facilitiesDTO) {

       int result = 0;

       try {

           FacilitiesEntity modifyFacility = new FacilitiesEntity(
                   0,
                   facilitiesDTO.getStatus(),
                   facilitiesDTO.getLocation(),
                   facilitiesDTO.getFacilitiesName(),
                   facilitiesDTO.getType(),
                   facilitiesDTO.getManager(),
                   facilitiesDTO.getFacilitiesClass(),
                   "N",  // 여기서 isActive를 명시적으로 설정
                   facilitiesDTO.getNote()
           );

           System.out.println("modifyFacility = " + modifyFacility);
           FacilitiesEntity facilities1 = facilitiesRepository.save(modifyFacility);
           System.out.println("facilities1 = " + facilities1);

           ApprovalEntity approval = new ApprovalEntity(
                   ApprovalTypeEntity.수정,
                   "N",
                   null,
                   null,
                   null,
                   null,
                   facilities1,
                   null,
                   facilitiesDTO.getFacilitiesCode()
           );
           approvalRepository.save(approval);
           result = 1;
       }catch (Exception e){
           throw new RuntimeException(e);
       }
      return (result>0) ? "편의시설 승인 수정 성공" : "편의시건 승인 수정 실패";
    }

    public void deleteFacilities(int facilitiesCode) {

        FacilitiesEntity deleteFacilities = facilitiesRepository.findById(facilitiesCode).orElseThrow(IllegalArgumentException::new);

        FacilitiesEntity delete = deleteFacilities.toBuilder()
                .isActive("N").build();

        facilitiesRepository.save(delete);
    }
}






