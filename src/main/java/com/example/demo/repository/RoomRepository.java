package com.example.demo.repository;

import com.example.demo.entity.HotelEntity;
import com.example.demo.entity.RoomEntity;
import com.example.demo.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Integer> {
    RoomEntity findByIdRoom(int id);
    Optional<RoomEntity> findByNumberRoom(Integer numberRoom);
    List<RoomEntity> findAllByRoomType(RoomType roomType);

    List<RoomEntity> findAllByRoomTypeAndHotelRoom(RoomType roomType, HotelEntity hotelEntity);


}
