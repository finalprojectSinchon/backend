package com.finalproject.airport.member.service;

import com.finalproject.airport.member.dto.ImgAndNameDTO;
import com.finalproject.airport.member.dto.UserContactDTO;
import com.finalproject.airport.member.entity.UserEntity;
import com.finalproject.airport.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public List<UserContactDTO> contact() {

        List<UserEntity> userList = userRepository.findAllByIsActive("Y");

        List<UserContactDTO> userContactDTOList = new ArrayList<>();

        for (UserEntity userEntity : userList) {
            if (userEntity.getUserName() != null && !userEntity.getUserName().isEmpty() ) {
                UserContactDTO userContactDTO = modelMapper.map(userEntity, UserContactDTO.class);
                String formattedPhoneNumber = formatPhoneNumber(userEntity.getUserPhone());
                userContactDTO.setUserPhone(formattedPhoneNumber);
                userContactDTO.setDeleted(false);
                userContactDTO.setCreatedDate(String.valueOf(userEntity.getCreatedDate()));
                userContactDTO.setUserDepartment(userEntity.getUserDepartment());
                userContactDTOList.add(userContactDTO);
            }
        }

        return userContactDTOList;
    }

    private String formatPhoneNumber(String phoneNumberStr) {
        if (phoneNumberStr.length() == 10) {
            return String.format("010-%s-%s", phoneNumberStr.substring(1, 4), phoneNumberStr.substring(4));
        } else if (phoneNumberStr.length() == 11) {
            return String.format("010-%s-%s", phoneNumberStr.substring(3, 7), phoneNumberStr.substring(7));
        } else {
            return phoneNumberStr;
        }
    }


    public ImgAndNameDTO getImgAndName(int userCode) {

        UserEntity user = userRepository.findById(userCode).orElseThrow();
        ImgAndNameDTO imgAndNameDTO = new ImgAndNameDTO(user.getUserName(),user.getUserImg());

        return imgAndNameDTO;
    }
}
