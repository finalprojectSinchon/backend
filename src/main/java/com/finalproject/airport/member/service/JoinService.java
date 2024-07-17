package com.finalproject.airport.member.service;

import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.member.dto.*;
import com.finalproject.airport.member.entity.UserEntity;
import com.finalproject.airport.member.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;

    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
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

    public ResponseEntity<?> modifyUser(UserModifyDTO userModifyDTO) {

        UserEntity user = userRepository.findById(userModifyDTO.getUserCode()).orElseThrow(IllegalArgumentException::new);

        UserEntity modifiedUser = user.toBuilder().userName(userModifyDTO.getUserName()).userEmail(userModifyDTO.getUserEmail()).userPhone(userModifyDTO.getUserPhone())
                .userAddress(userModifyDTO.getUserAddress()).userAbout(userModifyDTO.getUserAbout()).build();

        System.out.println("modifiedUser = " + modifiedUser);
        userRepository.save(modifiedUser);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK,"수정 성공하였습니다.",null));
    }

    public Boolean passwordCheck(UserPasswordCheckDTO passwordCheckDTO) {

        UserEntity user = userRepository.findById(passwordCheckDTO.getUserCode()).orElseThrow(IllegalArgumentException::new);
        return bCryptPasswordEncoder.matches(passwordCheckDTO.getUserPassword(),user.getUserPassword());
    }

    public ResponseEntity<?> passwordChange(ChangePasswordDTO changePasswordDTO) {

        // 새로운 비밀번호 비밀번호 확인
        if(changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())){
            // 비밀번호 인코드
            String encodePassword = bCryptPasswordEncoder.encode(changePasswordDTO.getNewPassword());
            // 변경
            UserEntity user = userRepository.findById(changePasswordDTO.getUserCode()).orElseThrow(IllegalArgumentException::new);
            UserEntity newUser = user.toBuilder().userPassword(encodePassword).build();
            userRepository.save(newUser);
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.ACCEPTED,"비밀번호 변경에 성공하였습니다.",null));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseDTO(HttpStatus.UNAUTHORIZED,"비밀번호가 일치하지 않습니다.",null));
        }




    }
}
