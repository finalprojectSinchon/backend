package com.finalproject.airport.member.repository;

import com.finalproject.airport.member.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Boolean existsByUserId(String userId);

    Boolean existsByUserEmail(String userEmail);

    Boolean existsByUserPhone(String userPhone);

    UserEntity findByUserId(String userId);

    @Query(value = "SELECT user_code FROM tbl_user WHERE auth_code = :randomNumber", nativeQuery = true)
    Integer findforAuthCode(@Param("randomNumber") int randomNumber);

    Boolean existsByAuthCode(int randomNumber);

    UserEntity findByAuthCode(int authCode);


    List<UserEntity> findAllByIsActive(String y);

    UserEntity findByUserEmailAndUserName(String userEmail, String userName);

}
