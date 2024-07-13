package com.finalproject.airport.member.service;

import com.finalproject.airport.member.entity.UserEntity;
import com.finalproject.airport.member.join.JoinDTO;
import com.finalproject.airport.member.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void joinProcess(JoinDTO joinDTO) {

        System.out.println("joinDTO = " + joinDTO);
        String userId = joinDTO.getUserId();
        String password = joinDTO.getUserPassword();
        String userEmail = joinDTO.getUserEmail();
        String userPhone = joinDTO.getUserPhone();
        String userAddress= joinDTO.getUserAddress();


        Boolean isExist = userRepository.existsByUserId(userId);
        Boolean emailExist = userRepository.existsByUserEmail(userEmail);
        Boolean phoneExist = userRepository.existsByUserPhone(userPhone);

        if (isExist || emailExist || phoneExist) {

            return;
        }

        String encodePassword = bCryptPasswordEncoder.encode(password);

        UserEntity data = new UserEntity(userId,encodePassword,userEmail,userPhone,userAddress,"ROLE_USER");

        userRepository.save(data);
    }
}
