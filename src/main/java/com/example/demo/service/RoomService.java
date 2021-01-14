package com.example.demo.service;


import com.example.demo.dto.HotelDto;
import com.example.demo.dto.ReservationDto;
import com.example.demo.dto.RoomDto;
import com.example.demo.entity.*;
import com.example.demo.exception.HotelException;
import com.example.demo.exception.RoomException;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoomService {

    private static final Logger log = LoggerFactory.getLogger(RoomService.class);

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ReservationService reservationService;

    //constructor
    public RoomService(RoomRepository roomRepository, HotelRepository hotelRepository, ReservationService reservationService) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
        this.reservationService = reservationService;
    }

    public RoomDto entityToDto(RoomEntity roomEntity){
        RoomDto roomDto = new RoomDto();

        roomDto.setIdRoom(roomEntity.getIdRoom());
        roomDto.setNumberRoom(roomEntity.getNumberRoom());
        roomDto.setCapacity(roomEntity.getCapacity());
        roomDto.setFloorNumber(roomEntity.getFloorNumber());
        roomDto.setCost(roomEntity.getCost());
        roomDto.setRoomType(roomEntity.getRoomType());
        roomDto.setIdHotel(roomEntity.getHotelRoom().getIdHotel());
        roomDto.setAddress(roomEntity.getHotelRoom().getCity() + ", " + roomEntity.getHotelRoom().getAddress());

        List<ReservationEntity> reservationEntities = roomEntity.getReservationsRoom();
        log.info(String.valueOf(reservationEntities));
        List<ReservationDto> reservationDtos = reservationEntities.stream()
                                                    .map(reservationEntity -> reservationService.entityToDto(reservationEntity))
                                                    .collect(Collectors.toList());
        log.info(String.valueOf(reservationDtos));
        roomDto.setReservations(reservationDtos);

        return roomDto;
    }

    public RoomEntity dtoToEntity(RoomDto room){
        RoomEntity roomEntity = new RoomEntity();

        roomEntity.setNumberRoom(room.getNumberRoom());
        roomEntity.setFloorNumber(room.getFloorNumber());
        roomEntity.setRoomType(room.getRoomType());
        roomEntity.setCapacity(room.getCapacity());
        roomEntity.setCost(room.getCost());
        roomEntity.setHotelRoom(hotelRepository.findById(room.getIdHotel()).orElse(null));

        List<ReservationDto> reservationDtos = room.getReservations();
        List<ReservationEntity> reservationEntities = reservationDtos.stream()
                .map(reservationDto -> reservationService.dtoToEntity(reservationDto))
                .collect(Collectors.toList());
        roomEntity.setReservationsRoom(reservationEntities);

        return roomEntity;
    }


    // ADD ROOM => POST
    public RoomEntity addRoom(RoomDto room){
        Optional<RoomEntity> existingRoom = roomRepository.findByNumberRoom(room.getNumberRoom());
        if (existingRoom.isPresent()) {
            throw RoomException.roomWithSameNumberRoomAlreadyExists();
        }
        RoomEntity roomEntity = dtoToEntity(room);
        if (roomEntity.getHotelRoom().getMaxNumberRooms() <= roomEntity.getHotelRoom().getRooms().size())
        {
            throw RoomException.roomNotSaved();
        }
        return roomRepository.save(roomEntity);
    }


    //  GET ALL  ROOMS USING DIFFERENT PARAMS
    public List<RoomDto> getRooms(Integer numberRoom, Integer capacity, Integer floorNumber, Integer cost, RoomType roomType){
        List<RoomEntity> roomEntities = roomRepository.findAll().stream()
                .filter( room -> isMatch(room, numberRoom, capacity, floorNumber, cost, roomType))
                .collect(Collectors.toList());

        if(roomEntities.isEmpty())
        {
            throw RoomException.roomDoesNotExist();
        }

        return roomEntities.stream()
                .map(roomEntity -> entityToDto(roomEntity))
                .collect(Collectors.toList());
    }

    private boolean isMatch(RoomEntity room, Integer numberRoom, Integer capacity, Integer floorNumber, Integer cost, RoomType roomType) {
        return (numberRoom == null || room.getNumberRoom().equals(numberRoom)
        ) && (capacity == null || room.getCapacity().equals(capacity)
        ) && (floorNumber == null || room.getFloorNumber().equals(floorNumber)
        ) && (cost == null || room.getCost().equals(cost)
        ) && (roomType == null || room.getRoomType().equals(roomType)
        ) ;
    }

    // GET A ROOM BY ID HOTEL
    public Set<RoomDto> findAllByIdHotelRoom(int id){

         Optional<HotelEntity> hotel = hotelRepository.findById(id);

        if (hotel == null ) {
            throw RoomException.roomHotelDoesNotExist();
        }
        Set<RoomEntity> roomEntities =  hotel.get().getRooms();
        if (roomEntities.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Hotel does not have rooms");
        }
        return roomEntities.stream()
                .map(roomEntity -> entityToDto(roomEntity))
                .collect(Collectors.toSet());
    }

    //  GET A ROOM BY ID
    public RoomDto getRoomById(int id) {
        RoomEntity roomEntity = roomRepository.findByIdRoom(id);
        if (roomEntity == null) {
            throw RoomException.roomDoesNotExist();
        }
        log.info(String.valueOf(roomEntity.getReservationsRoom().size()));
        return entityToDto(roomEntity);
    }



    //  UPDATE ROOM'S INFO => PUT
    public RoomDto updateRoom(int id, RoomDto room){
        RoomEntity roomEntity = roomRepository.findById(id).orElse(null);
        if(roomEntity == null){
            throw RoomException.roomDoesNotExist();
        }
        Optional<RoomEntity> existingRoom = roomRepository.findByNumberRoom(room.getNumberRoom());
        if (existingRoom.isPresent()) {
            throw RoomException.roomWithSameNumberRoomAlreadyExists();
        }

        roomEntity.setCapacity(room.getCapacity());
        roomEntity.setCost(room.getCost());
        roomEntity.setFloorNumber(room.getFloorNumber());
        roomEntity.setNumberRoom(room.getNumberRoom());
        roomEntity.setRoomType(room.getRoomType());

        return entityToDto(roomRepository.save(roomEntity));
    }

    //  DELETE A ROOM
    public void deleteRoom(int id){
        RoomEntity roomEntity = roomRepository.findById(id).orElse(null);
        if(roomEntity == null){
            throw RoomException.roomDoesNotExist();
        }
        roomRepository.deleteById(id);
    }
}
