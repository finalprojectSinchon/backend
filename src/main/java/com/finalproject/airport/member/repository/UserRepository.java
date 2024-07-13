package com.finalproject.airport.member.repository;

import com.finalproject.airport.member.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Boolean existsByUserId(String userId);

    Boolean existsByUserEmail(String userEmail);

    Boolean existsByUserPhone(String userPhone);

    UserEntity findByUserId(String userId);
}
