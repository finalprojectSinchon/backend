package com.finalproject.airport.inspection.service;

import com.finalproject.airport.airplane.baggageclaim.entity.BaggageClaim;
import com.finalproject.airport.inspection.dto.InspectionDTO;
import com.finalproject.airport.inspection.entity.InspectionEntity;
import com.finalproject.airport.inspection.respository.InspectionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectionService {

    private final InspectionRepository inspectionRepository;

    @Autowired
    private final ModelMapper modelMapper;



    //점검 전체 조회
    public List<InspectionDTO> getInspectionList() {

        List<InspectionDTO> inspectionDTOList = new ArrayList<>();
        List<InspectionEntity> inspectionList1 = inspectionRepository.findByIsActive("Y");
        inspectionList1.forEach(inspectionEntity -> {inspectionDTOList.add(modelMapper.map(inspectionEntity, InspectionDTO.class));});


        return inspectionDTOList;
    }

    //점검 상세 조회
    public InspectionDTO getInspection(int inspectionCode) {
        InspectionEntity inspection = inspectionRepository.findByinspectionCode(inspectionCode);

        return modelMapper.map(inspection, InspectionDTO.class);
    }


    // 점검 등록
    public void addInspection(InspectionDTO inspectionDTO) {
        InspectionEntity inspectionEntity = modelMapper.map(inspectionDTO, InspectionEntity.class);
        inspectionRepository.save(inspectionEntity);
    }

    // 점검 수정
    public void updateInspection(int inspectionCode, InspectionDTO inspectionDTO) {

        InspectionEntity inspectionEntity = inspectionRepository.findByinspectionCode(inspectionCode);


        System.out.println("inspectionEntity = " + inspectionEntity);
        inspectionEntity = modelMapper.map(inspectionDTO, InspectionEntity.class);
        inspectionRepository.save(inspectionEntity);

    }

    // 점검 삭제
    public void softDeleteInspection(int inspectionCode) {
        InspectionEntity inspectionEntity = inspectionRepository.findById(Integer.valueOf(inspectionCode)).orElseThrow(IllegalArgumentException::new);
        inspectionEntity = inspectionEntity.toBuilder().isActive("N").build();


        inspectionRepository.save(inspectionEntity);

    }

    @Transactional
    public String insertInspection(InspectionDTO inspectionDTO) {

        int result = 0;
        try {
            InspectionEntity insertInspection =modelMapper.map(inspectionDTO, InspectionEntity.class);
            inspectionRepository.save(insertInspection);

            result = 1;

        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return(result > 0)? "정비 등록 성공": "정비 등록 실패";
    }
}
