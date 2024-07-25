package com.finalproject.airport.member.service;

import com.finalproject.airport.auth.util.SMSUtil;
import com.finalproject.airport.common.ResponseDTO;
import com.finalproject.airport.member.dto.*;
import com.finalproject.airport.member.entity.UserEntity;
import com.finalproject.airport.member.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final MailService mailService;

    private final ModelMapper modelMapper;

    private final SMSUtil smsUtil;

    public ResponseEntity<?> joinProcess(JoinDTO joinDTO) {

        System.out.println("joinDTO = " + joinDTO);
        String userId = joinDTO.getUserId();
        String password = joinDTO.getUserPassword();
        String userEmail = joinDTO.getUserEmail();
        String userPhone = joinDTO.getUserPhone();
        String userAddress= joinDTO.getUserAddress();
        String userName = joinDTO.getUserName();
        String userAbout = joinDTO.getUserAbout();
        int authCode = joinDTO.getAuthCode();

        UserEntity joinUserEntity = userRepository.findByAuthCode(authCode);

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

        UserEntity newUser = joinUserEntity.toBuilder().userId(userId).userPassword(encodePassword)
                .userEmail(userEmail).userPhone(userPhone).userAddress(userAddress).userRole("ROLE_USER")
                .userName(userName).userAbout(userAbout).isActive("Y").build();

        userRepository.save(newUser);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.CREATED,"회원가입이 완료되었습니다.",null));
    }

    public ResponseEntity<?> modifyUser(UserModifyDTO userModifyDTO) {

        UserEntity user = userRepository.findById(userModifyDTO.getUserCode()).orElseThrow(IllegalArgumentException::new);
        System.out.println("user = " + user);
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

    public void saveprofileImg(Map<String, Object> info) {

        UserEntity user = userRepository.findById((Integer) info.get("userCode")).orElseThrow();
        UserEntity newUser = user.toBuilder().userImg((String) info.get("profileImageUrl")).build();
        userRepository.save(newUser);

    }

    @Transactional
    public ResponseEntity<?> getAdminCode() {

        int randomNumber = (int) (Math.random() * 900000) + 100000;
        Boolean isAuthCode = userRepository.existsByAuthCode(randomNumber);
        if (isAuthCode) {
            return  ResponseEntity.badRequest().body(new ResponseDTO(HttpStatus.BAD_REQUEST,"이미 있는 랜덤번호입니다. 다시시도해주세요",null));
        } else {
            UserEntity user = new UserEntity();
            user.setAuthCode(randomNumber);
            System.out.println("user = " + user);
            userRepository.save(user);

            Integer userCode = userRepository.findforAuthCode(randomNumber);
            Map<String, Object> info = new HashMap<>();
            info.put("userCode", userCode);
            info.put("authCode", randomNumber);
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "인증번호 발급 성공", info));
        }
    }

    public ResponseEntity<?> isCheckAuth(Map<String, Integer> authCode) {
        Boolean isAuthCode = userRepository.existsByAuthCode(authCode.get("password"));
        System.out.println("isAuthCode = " + isAuthCode);
        if (isAuthCode) {

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<?> userAboutChange(ChangeAboutDTO changeAboutDTO) {
        int userCode = changeAboutDTO.getUserCode();
        UserEntity user = userRepository.findById(userCode).orElseThrow(IllegalArgumentException::new);
        user = user.toBuilder().userAbout(changeAboutDTO.getUserAbout()).build();


        userRepository.save(user);

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.ACCEPTED,"내 정보 등록 성공",null));
    }

    public ResponseEntity<?> findUserId(UserFindIdDTO userIdDTO) {

        String userEmail = userIdDTO.getEmail();
        String userName = userIdDTO.getUname();

        UserEntity user = userRepository.findByUserEmailAndUserName(userEmail , userName);

        if (user != null) {

            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "아이디 찾기완료", user.getUserId() ));
        } else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("이름,이메일이 틀립니다.");
        }


    }

    public ResponseEntity<?> findPwd(UserFindPasswordDTO userFindPasswordDTO) throws MessagingException {
        String userId = userFindPasswordDTO.getUserId();
        String userPhone = userFindPasswordDTO.getUserPhone();
        String userEmail = userFindPasswordDTO.getUserEmail();

        System.out.println(userFindPasswordDTO);

        UserEntity user = userRepository.findByUserId(userId);

        if (user.userId != null){


          if (userEmail != null && !userEmail.isEmpty()){

              if (user.userEmail .equals(userEmail)){

                  String randomCode = String.valueOf((int) (Math.random() * 90000000) + 10000000);
                  mailService.sendForNewPassword(user.userEmail,randomCode);
                  String code = bCryptPasswordEncoder.encode(randomCode);
                  UserEntity newPwd = user.toBuilder().userPassword(code).build();
                  userRepository.save(newPwd);

                  return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "정상적으로 이메일을 보냈습니다.", null));

              } else {
                  return ResponseEntity.status(HttpStatus.NOT_FOUND).body("이메일이 일치하지 않습니다.");
              }
          } else {
            if (userPhone != null || !userPhone.isEmpty()){
                if (user.userPhone .equals(userPhone)) {
                    String randomCode = String.valueOf((int) (Math.random() * 90000000) + 10000000);
                    smsUtil.newpassword(userPhone,randomCode);
                    String code = bCryptPasswordEncoder.encode(randomCode);
                    UserEntity newPwd = user.toBuilder().userPassword(code).build();
                    userRepository.save(newPwd);

                    return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "정상적으로 SMS를 보냈습니다.", null));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("전화번호가 일치하지 않습니다.");
            }
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body("아이디를 찾을수 없습니다");
          }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("아이디를 찾을수 없습니다");
        }


    }
}
