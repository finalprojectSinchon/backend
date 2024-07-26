package com.finalproject.airport.manager.service;

import com.finalproject.airport.manager.dto.ManagerTypeCodeDTO;
import com.finalproject.airport.manager.dto.ManagerUpdateDTO;
import com.finalproject.airport.manager.dto.UserFindManagerDTO;
import com.finalproject.airport.manager.entity.ManagersEntity;
import com.finalproject.airport.manager.repository.ManagersRepository;
import com.finalproject.airport.member.dto.UserDTO;
import com.finalproject.airport.member.entity.UserEntity;
import com.finalproject.airport.member.repository.UserRepository;
import com.finalproject.airport.store.entity.StoreEntity;
import com.finalproject.airport.store.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ManagersService {

   private final ManagersRepository managersRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public Map<String,List<?>> findManagers(ManagerTypeCodeDTO managerTypeCodeDTO) {

        Map<String, List<?>> managerInfo = new HashMap<>();

        String airportType = managerTypeCodeDTO.getAirportType();
        int pk = managerTypeCodeDTO.getAirportCode();



        List<UserFindManagerDTO> findUserList = new ArrayList<>();
        // 담당 직원 DTO
        switch (airportType) {
            case "checkinCounter" :
            case "facilities" :
            case "storage" :
            case "store" :
                List<ManagersEntity> findUserCode = managersRepository.findAllByStoreIdAndIsActive(pk,"Y");
                for (ManagersEntity managerEntity : findUserCode) {
                    UserDTO userDTO = modelMapper.map(managerEntity.getUser(), UserDTO.class);
                    UserFindManagerDTO userFindManagerDTO = new UserFindManagerDTO(userDTO.getUserCode(),userDTO.getUserName(),userDTO.getUserImg());
                    findUserList.add(userFindManagerDTO);
                };
                break;
            case "inspection" :
            case "baggageClaim" :
            case "gate" :
        }

        managerInfo.put("Manager",findUserList);

        // 전체 직원 DTO
        List<UserEntity> userList = userRepository.findAllByUserRole("ROLE_USER");
        List<UserFindManagerDTO> userFindManagerList = new ArrayList<>();
        for (UserEntity userEntity : userList) {
            UserFindManagerDTO userFindManagerDTO = new UserFindManagerDTO();
            userFindManagerDTO.setUserCode(userEntity.getUserCode());
            userFindManagerDTO.setUsername(userEntity.getUserName());
            userFindManagerDTO.setUserImg(userEntity.getUserImg());
            userFindManagerList.add(userFindManagerDTO);
        }


        Set<Integer> findUserCodes = new HashSet<>();
        for (UserFindManagerDTO userFindDTO : findUserList) {
            findUserCodes.add(userFindDTO.getUserCode());
        }

        List<UserFindManagerDTO> removeAllUserList = new ArrayList<>();
        for (UserFindManagerDTO userFindManagerDTO : userFindManagerList) {
            if (!findUserCodes.contains(userFindManagerDTO.getUserCode())) {
                removeAllUserList.add(userFindManagerDTO);
            }
        }

        managerInfo.put("AllUser", removeAllUserList);

        return managerInfo;
    }

    @Transactional
    public void updateManagers(List<ManagerUpdateDTO> managerUpdateDTO) {

        String airportType = managerUpdateDTO.get(0).getAirportType();
        int airportCode = managerUpdateDTO.get(0).getAirportCode();

        switch (airportType) {
            case "checkinCounter" :
            case "facilities" :
            case "storage" :
            case "store" :
                List<ManagersEntity> managerList = managersRepository.findAllByStoreIdAndIsActive(airportCode,"Y");
                for (ManagersEntity managerEntity : managerList) {
                    managerEntity.setIsActive("N");
                }
                for (ManagerUpdateDTO managerUpdate : managerUpdateDTO) {
                    UserEntity user = userRepository.findById(managerUpdate.getUserCode()).orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));
                    ManagersEntity managerEntity = ManagersEntity.builder()
                            .storeId(managerUpdate.getAirportCode())
                            .user(user)
                            .build();
                    managersRepository.save(managerEntity);
                }
            case "inspection" :
            case "baggageClaim" :
            case "gate" :
        }

    }
}