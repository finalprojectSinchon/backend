package com.finalproject.airport.facilities.service;

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

@Service
@RequiredArgsConstructor
public class FacilitiesService {

    @Autowired
    private final FacilitiesRepository facilitiesRepository;

    private final ModelMapper modelMapper;
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
                findfacilities.getFacilitiesStatus(),
                findfacilities.getFacilitiesLocation(),
                findfacilities.getFacilitiesName(),
                findfacilities.getFacilitiesType(),
                findfacilities.getFacilitiesManager(),
                findfacilities.getFacilitiesClass(),
                findfacilities.getIsActive()
        );
      return facilitiesDTO2;
    }

    public void insertFacilities(FacilitiesDTO facilitiesDTO) {

        FacilitiesEntity insertFacilities = new FacilitiesEntity(
                facilitiesDTO.getFacilitiesCode(),
                facilitiesDTO.getFacilitiesStatus(),
                facilitiesDTO.getFacilitiesLocation(),
                facilitiesDTO.getFacilitiesName(),
                facilitiesDTO.getFacilitiesType(),
                facilitiesDTO.getFacilitiesManager(),
                facilitiesDTO.getFacilitiesClass(),
                null

                );

                facilitiesRepository.save(insertFacilities);


    }

@Transactional
    public void updateFacilities(int facilitiesCode , FacilitiesDTO facilitiesDTO) {

        FacilitiesEntity findUpdateFacilities = facilitiesRepository.findById(facilitiesCode).orElseThrow(IllegalArgumentException::new);


       FacilitiesEntity update = findUpdateFacilities.toBuilder()
//                .facilitiesCode(facilitiesCode)
                .facilitiesClass(facilitiesDTO.getFacilitiesClass())
                .facilitiesLocation(facilitiesDTO.getFacilitiesLocation())
                .facilitiesName(facilitiesDTO.getFacilitiesName())
                .facilitiesManager(facilitiesDTO.getFacilitiesManager())
                .facilitiesType(facilitiesDTO.getFacilitiesType())
                .facilitiesStatus(facilitiesDTO.getFacilitiesStatus()).build();
    System.out.println( findUpdateFacilities + "11111111111111111111111111 ");
//    System.out.println( update + " ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        facilitiesRepository.save(update);

    }

    public void deleteFacilities(int facilitiesCode) {

        FacilitiesEntity deleteFacilities = facilitiesRepository.findById(facilitiesCode).orElseThrow(IllegalArgumentException::new);

        FacilitiesEntity delete = deleteFacilities.toBuilder()

                .isActive("N").build();

        facilitiesRepository.save(delete);
    }
}






