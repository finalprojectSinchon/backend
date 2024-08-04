package com.finalproject.airport.manager.service;

import com.finalproject.airport.facilities.dto.FacilitiesDTO;
import com.finalproject.airport.facilities.entity.FacilitiesEntity;
import com.finalproject.airport.facilities.repository.FacilitiesRepository;
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

    private final FacilitiesRepository facilitiesRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public Map<String,List<?>> findManagers(ManagerTypeCodeDTO managerTypeCodeDTO) {

        Map<String, List<?>> managerInfo = new HashMap<>();

        String airportType = managerTypeCodeDTO.getAirportType();
        int pk = managerTypeCodeDTO.getAirportCode();



        List<UserFindManagerDTO> findUserList = new ArrayList<>();
        // 담당 직원 DTO
        switch (airportType) {
            case "facilities" :
                List<ManagersEntity> findUserCodeForFacilities = managersRepository.findAllByFacilitiesCodeAndIsActive(pk,"Y");
                for (ManagersEntity manager : findUserCodeForFacilities) {
                    UserDTO userDTO = modelMapper.map(manager.getUser(), UserDTO.class);
                    UserFindManagerDTO userFindManagerDTO = new UserFindManagerDTO(userDTO.getUserCode(),userDTO.getUserName(),userDTO.getUserImg(),userDTO.getUserPhone(),userDTO.getUserDepartment());
                    findUserList.add(userFindManagerDTO);
                }
                break;
            case "storage" :
                List<ManagersEntity> findUserCodeForStorage = managersRepository.findAllByStorageCodeAndIsActive(pk,"Y");
                for (ManagersEntity manager : findUserCodeForStorage) {
                    UserDTO userDTO = modelMapper.map(manager.getUser(), UserDTO.class);
                    UserFindManagerDTO userFindManagerDTO = new UserFindManagerDTO(userDTO.getUserCode(),userDTO.getUserName(),userDTO.getUserImg(),userDTO.getUserPhone(),userDTO.getUserDepartment());
                    findUserList.add(userFindManagerDTO);
                }
                break;
            case "store" :
                List<ManagersEntity> findUserCode = managersRepository.findAllByStoreIdAndIsActive(pk,"Y");
                for (ManagersEntity managerEntity : findUserCode) {
                    UserDTO userDTO = modelMapper.map(managerEntity.getUser(), UserDTO.class);
                    UserFindManagerDTO userFindManagerDTO = new UserFindManagerDTO(userDTO.getUserCode(),userDTO.getUserName(),userDTO.getUserImg(),userDTO.getUserPhone(),userDTO.getUserDepartment());
                    findUserList.add(userFindManagerDTO);
                };
                break;
            case "equipment" :
                List<ManagersEntity> findUserCodeForEquipment = managersRepository.findAllByEquipmentCodeAndIsActive(pk,"Y");
                for (ManagersEntity manager : findUserCodeForEquipment) {
                    UserDTO userDTO = modelMapper.map(manager.getUser(), UserDTO.class);
                    UserFindManagerDTO userFindManagerDTO = new UserFindManagerDTO(userDTO.getUserCode(),userDTO.getUserName(),userDTO.getUserImg(),userDTO.getUserPhone(),userDTO.getUserDepartment());
                    findUserList.add(userFindManagerDTO);
                };
                break;
            case "baggageClaim" :
                List<ManagersEntity> findUserCodeForBaggageClaim = managersRepository.findAllByBaggageClaimCodeAndIsActive(pk,"Y");
                for (ManagersEntity manager : findUserCodeForBaggageClaim) {
                    UserDTO userDTO = modelMapper.map(manager.getUser(), UserDTO.class);
                    UserFindManagerDTO userFindManagerDTO = new UserFindManagerDTO(userDTO.getUserCode(),userDTO.getUserName(),userDTO.getUserImg(),userDTO.getUserPhone(),userDTO.getUserDepartment());
                    findUserList.add(userFindManagerDTO);
                };
                break;
            case "checkinCounter" :
                List<ManagersEntity> findUserCodeForCheckinCounter = managersRepository.findAllByCheckinCounterCodeAndIsActive(pk,"Y");
                for (ManagersEntity manager : findUserCodeForCheckinCounter) {
                    UserDTO userDTO = modelMapper.map(manager.getUser(), UserDTO.class);
                    UserFindManagerDTO userFindManagerDTO = new UserFindManagerDTO(userDTO.getUserCode(),userDTO.getUserName(),userDTO.getUserImg(),userDTO.getUserPhone(),userDTO.getUserDepartment());
                    findUserList.add(userFindManagerDTO);
                }
            case "inspection" :
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
            userFindManagerDTO.setUserPhone(userEntity.getUserPhone());
            userFindManagerDTO.setUserDepartment(userEntity.getUserDepartment());
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
            case "facilities" :
                List<ManagersEntity> facilitiesManagerList = managersRepository.findAllByFacilitiesCodeAndIsActive(airportCode,"Y");
                for(ManagersEntity manager : facilitiesManagerList) {
                    manager.setIsActive("N");
                }
                for(ManagerUpdateDTO managerUpdate : managerUpdateDTO) {
                    UserEntity user = userRepository.findById(managerUpdate.getUserCode()).orElseThrow(null);
                    ManagersEntity managersEntity = ManagersEntity.builder()
                            .facilitiesCode(managerUpdate.getAirportCode())
                            .user(user)
                            .build();
                    managersRepository.save(managersEntity);
                }
                break;
            case "storage" :
                List<ManagersEntity> managersList = managersRepository.findAllByStorageCodeAndIsActive(airportCode,"Y");
                for(ManagersEntity manager : managersList) {
                    manager.setIsActive("N");
                }
                for(ManagerUpdateDTO managerUpdate : managerUpdateDTO) {
                    UserEntity user = userRepository.findById(managerUpdate.getUserCode()).orElseThrow(null);
                    ManagersEntity managersEntity = ManagersEntity.builder()
                            .storageCode(managerUpdate.getAirportCode())
                            .user(user)
                            .build();
                    managersRepository.save(managersEntity);
                }
                break;
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
                break;
            case "equipment" :
                List<ManagersEntity> managerList2 = managersRepository.findAllByEquipmentCodeAndIsActive(airportCode,"Y");
                for(ManagersEntity manager : managerList2) {
                    manager.setIsActive("N");
                }
                for(ManagerUpdateDTO managerUpdate : managerUpdateDTO) {
                    UserEntity user = userRepository.findById(managerUpdate.getUserCode()).orElseThrow(null);
                    ManagersEntity managersEntity = ManagersEntity.builder()
                            .equipmentCode(managerUpdate.getAirportCode())
                            .user(user)
                            .build();
                    managersRepository.save(managersEntity);
                }
                break;
            case "baggageClaim" :
                List<ManagersEntity> managersForBaggageClaimList = managersRepository.findAllByBaggageClaimCodeAndIsActive(airportCode,"Y");
                for(ManagersEntity manager : managersForBaggageClaimList) {
                    manager.setIsActive("N");
                }
                for(ManagerUpdateDTO managerUpdate : managerUpdateDTO) {
                    UserEntity user = userRepository.findById(managerUpdate.getUserCode()).orElseThrow(null);
                    ManagersEntity managersEntity = ManagersEntity.builder()
                            .baggageClaimCode(managerUpdate.getAirportCode())
                            .user(user)
                            .build();
                    managersRepository.save(managersEntity);
                }
            case "checkinCounter" :
                List<ManagersEntity> managerForCheckinCounterList = managersRepository.findAllByCheckinCounterCodeAndIsActive(airportCode,"Y");
                for(ManagersEntity manager : managerForCheckinCounterList) {
                    manager.setIsActive("N");
                }
                for(ManagerUpdateDTO managerUpdate : managerUpdateDTO) {
                    UserEntity user = userRepository.findById(managerUpdate.getUserCode()).orElseThrow(null);
                    ManagersEntity managersEntity = ManagersEntity.builder()
                            .checkinCounterCode(managerUpdate.getAirportCode())
                            .user(user)
                            .build();
                    managersRepository.save(managersEntity);
                }
            case "inspection" :
            case "gate" :
        }

    }
}
