package com.example.demo.repository;

import com.example.demo.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;
@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Integer> {
    ReservationEntity findByIdReservation(int id);
}
