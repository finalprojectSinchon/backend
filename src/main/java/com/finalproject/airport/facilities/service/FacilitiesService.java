package com.finalproject.airport.facilities.service;

import com.finalproject.airport.approval.entity.ApprovalEntity;
import com.finalproject.airport.approval.entity.ApprovalTypeEntity;
import com.finalproject.airport.approval.repository.ApprovalRepository;
import com.finalproject.airport.facilities.dto.FacilitiesDTO;
import com.finalproject.airport.facilities.entity.FacilitiesEntity;
import com.finalproject.airport.facilities.repository.FacilitiesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FacilitiesService {

    private final FacilitiesRepository facilitiesRepository;
    private final ModelMapper modelMapper;
    private final ApprovalRepository approvalRepository;

    public List<FacilitiesDTO> selectAllFacilities() {
        log.info("Fetching all active facilities");

        List<FacilitiesEntity> facilities = facilitiesRepository.findAllByIsActive("Y");
        return facilities.stream()
                .map(facility -> modelMapper.map(facility, FacilitiesDTO.class))
                .collect(Collectors.toList());
    }

    public FacilitiesDTO findFacilities(int facilitiesCode) {
        log.info("Fetching facility with code: {}", facilitiesCode);

        FacilitiesEntity facilitiesEntity = facilitiesRepository.findById(facilitiesCode)
                .orElseThrow(() -> new IllegalArgumentException("No facility found with code: " + facilitiesCode));

        return modelMapper.map(facilitiesEntity, FacilitiesDTO.class);
    }

    @Transactional
    public String insertFacilities(FacilitiesDTO facilitiesDTO) {
        log.info("Inserting new facility: {}", facilitiesDTO);

        try {
            FacilitiesEntity newFacility = FacilitiesEntity.builder()
                    .status(facilitiesDTO.getStatus())
                    .location(facilitiesDTO.getLocation())
                    .facilitiesName(facilitiesDTO.getFacilitiesName())
                    .facilitiesType(facilitiesDTO.getType())
                    .manager(facilitiesDTO.getManager())
                    .facilitiesClass(facilitiesDTO.getFacilitiesClass())
                    .note(facilitiesDTO.getNote())
                    .isActive("N")
                    .build();

            FacilitiesEntity savedFacility = facilitiesRepository.save(newFacility);
            log.info("Saved new facility: {}", savedFacility);

            ApprovalEntity approval = new ApprovalEntity(
                    ApprovalTypeEntity.등록,
                    "N",
                    null,
                    null,
                    null,
                    null,
                    savedFacility
            );

            approvalRepository.save(approval);
            return "Facility approval request succeeded";
        } catch (Exception e) {
            log.error("Error inserting facility", e);
            throw new RuntimeException("Error inserting facility", e);
        }
    }

    @Transactional
    public String modifyFacilities(FacilitiesDTO facilitiesDTO) {
        log.info("Modifying facility: {}", facilitiesDTO);

        try {
            FacilitiesEntity existingFacility = facilitiesRepository.findById(facilitiesDTO.getFacilitiesCode())
                    .orElseThrow(() -> new IllegalArgumentException("No facility found with code: " + facilitiesDTO.getFacilitiesCode()));

            FacilitiesEntity modifiedFacility = existingFacility.toBuilder()
                    .status(facilitiesDTO.getStatus())
                    .location(facilitiesDTO.getLocation())
                    .facilitiesName(facilitiesDTO.getFacilitiesName())
                    .facilitiesType(facilitiesDTO.getType())
                    .manager(facilitiesDTO.getManager())
                    .facilitiesClass(facilitiesDTO.getFacilitiesClass())
                    .note(facilitiesDTO.getNote())
                    .build();

            FacilitiesEntity savedFacility = facilitiesRepository.save(modifiedFacility);
            log.info("Saved modified facility: {}", savedFacility);

            ApprovalEntity approval = new ApprovalEntity(
                    ApprovalTypeEntity.수정,
                    "N",
                    null,
                    null,
                    null,
                    null,
                    savedFacility,
                    null,
                    facilitiesDTO.getFacilitiesCode()
            );

            approvalRepository.save(approval);
            return "Facility approval modification succeeded";
        } catch (Exception e) {
            log.error("Error modifying facility", e);
            throw new RuntimeException("Error modifying facility", e);
        }
    }

    @Transactional
    public void deleteFacilities(int facilitiesCode) {
        log.info("Deleting facility with code: {}", facilitiesCode);

        FacilitiesEntity facility = facilitiesRepository.findById(facilitiesCode)
                .orElseThrow(() -> new IllegalArgumentException("No facility found with code: " + facilitiesCode));

        FacilitiesEntity updatedFacility = facility.toBuilder().isActive("N").build();
        facilitiesRepository.save(updatedFacility);
    }
}
