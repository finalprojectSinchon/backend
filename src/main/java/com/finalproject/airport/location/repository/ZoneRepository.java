package com.finalproject.airport.location.repository;

import com.finalproject.airport.location.entity.ZoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZoneRepository extends JpaRepository<ZoneEntity, Integer> {

}
