package com.finalproject.airport.manager.repository;

import com.finalproject.airport.manager.entity.ManagersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagersRepository extends JpaRepository<ManagersEntity, Integer> {



    List<ManagersEntity> findAllByStoreIdAndIsActive(int pk, String y);
}