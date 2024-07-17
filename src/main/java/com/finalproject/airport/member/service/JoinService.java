package com.finalproject.airport.member.service;

import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.member.entity.UserEntity;
import com.finalproject.airport.member.dto.JoinDTO;
import com.finalproject.airport.member.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<?> joinProcess(JoinDTO joinDTO) {

        System.out.println("joinDTO = " + joinDTO);
        String userId = joinDTO.getUserId();
        String password = joinDTO.getUserPassword();
        String userEmail = joinDTO.getUserEmail();
        String userPhone = joinDTO.getUserPhone();
        String userAddress= joinDTO.getUserAddress();
        String userName = joinDTO.getUserName();
        String userAbout = joinDTO.getUserAbout();


        Boolean isExist = userRepository.existsByUserId(userId);
        Boolean emailExist = userRepository.existsByUserEmail(userEmail);
        Boolean phoneExist = userRepository.existsByUserPhone(userPhone);

        if (userRepository.existsByUserId(userId)) {
            return ResponseEntity.badRequest().body(new ResponseDTO(HttpStatus.BAD_REQUEST, "아이디가 중복입니다.", null));
        }

        if (userRepository.existsByUserEmail(userEmail)) {
            return ResponseEntity.badRequest().body(new ResponseDTO(HttpStatus.BAD_REQUEST, "이메일이 중복입니다.", null));
        }

        if (userRepository.existsByUserPhone(userPhone)) {
            return ResponseEntity.badRequest().body(new ResponseDTO(HttpStatus.BAD_REQUEST, "휴대폰 번호가 중복입니다.", null));
        }

        String encodePassword = bCryptPasswordEncoder.encode(password);

        UserEntity data = new UserEntity(userId,encodePassword,userEmail,userPhone,userAddress,"ROLE_USER",userName,userAbout);
        userRepository.save(data);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.CREATED,"회원가입이 완료되었습니다.",null));
    }
}
