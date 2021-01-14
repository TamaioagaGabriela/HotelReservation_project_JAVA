package com.example.demo.repository;

import com.example.demo.entity.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<HotelEntity, Integer> {

    Iterable<HotelEntity> findAllByCity(String city);
    Optional<HotelEntity> findByAddress(String address);
    HotelEntity findByIdHotel(int id);
    HotelEntity findAllByCityAndAddress(String city, String address);
}
