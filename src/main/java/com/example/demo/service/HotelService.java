package com.example.demo.service;


import com.example.demo.dto.HotelDto;
import com.example.demo.dto.ReservationDto;
import com.example.demo.dto.ReviewDto;
import com.example.demo.dto.RoomDto;
import com.example.demo.entity.*;
import com.example.demo.exception.ClientException;
import com.example.demo.exception.HotelException;
import com.example.demo.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HotelService {


    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomService roomService;

    @Autowired
    private ReviewService reviewService;


    // constructor
    public HotelService(HotelRepository hotelRepository, RoomService roomService, ReviewService reviewService) {
        this.hotelRepository = hotelRepository;
        this.roomService = roomService;
        this.reviewService = reviewService;
    }

    public HotelDto entityToDto(HotelEntity hotelEntity){
        HotelDto hotelDto = new HotelDto();

        hotelDto.setIdHotel(hotelEntity.getIdHotel());
        hotelDto.setAddress(hotelEntity.getAddress());
        hotelDto.setCity(hotelEntity.getCity());
        hotelDto.setPhoneNumber(hotelEntity.getPhoneNumber());
        hotelDto.setMaxNumberRooms(hotelEntity.getMaxNumberRooms());

        Set<RoomEntity> roomEntities =  hotelEntity.getRooms();
        Set<RoomDto> roomDtos = roomEntities.stream()
                .map(roomEntity -> roomService.entityToDto(roomEntity))
                .collect(Collectors.toSet());
        hotelDto.setRooms(roomDtos);

        List<ReviewEntity> reviewEntities = hotelEntity.getReviews();
        List<ReviewDto> reviewDtos = reviewEntities.stream()
                .map(reviewEntity -> reviewService.entityToDto(reviewEntity))
                .collect(Collectors.toList());
        hotelDto.setReviews(reviewDtos);

        return hotelDto;
    }

    public HotelEntity dtoToEntity(HotelDto hotel)
    {
        HotelEntity hotelEntity = new HotelEntity();

        hotelEntity.setAddress(hotel.getAddress());
        hotelEntity.setCity(hotel.getCity());
        hotelEntity.setPhoneNumber(hotel.getPhoneNumber());
        hotelEntity.setMaxNumberRooms(hotel.getMaxNumberRooms());

        Set<RoomDto> roomDtos = hotel.getRooms();
        Set<RoomEntity> roomEntities = roomDtos.stream()
                .map(roomDto -> roomService.dtoToEntity(roomDto))
                .collect(Collectors.toSet());
        hotelEntity.setRooms(roomEntities);

        List<ReviewDto> reviewDtos = hotel.getReviews();
        List<ReviewEntity> reviewEntities = reviewDtos.stream()
                .map(reviewDto -> reviewService.dtoToEntity(reviewDto))
                .collect(Collectors.toList());
        hotelEntity.setReviews(reviewEntities);

        return hotelEntity;
    }



    //  CREATE A NEW HOTEL  =>  POST
    public HotelEntity addHotel(HotelDto hotel){
        Optional<HotelEntity> existingHotel = hotelRepository.findByAddress(hotel.getAddress());
        if (existingHotel.isPresent() && existingHotel.get().getCity().equals(hotel.getCity())) {
            throw HotelException.hotelWithSameAddressAlreadyExists();
        }
        HotelEntity hotelEntity = dtoToEntity(hotel);
        return hotelRepository.save(hotelEntity);
    }

    public List<HotelDto> getHotels(String city, String address, String phoneNumber, Integer totalRoomNumbers){
        List<HotelEntity> hotelEntities = hotelRepository.findAll().stream()
                .filter( hotel -> isMatch(hotel, city, address, phoneNumber, totalRoomNumbers))
                .collect(Collectors.toList());

        if(hotelEntities.isEmpty()){
            throw HotelException.hotelDoesNotExist();
        }

        return hotelEntities.stream()
                .map(hotelEntity -> entityToDto(hotelEntity))
                .collect(Collectors.toList());
    }

    private boolean isMatch(HotelEntity hotel, String city, String address, String phoneNumber, Integer totalRoomNumbers) {
        return (city == null || hotel.getCity().equals(city)
                ) &&
                (address == null || hotel.getAddress().equals(address)
                ) &&
                (phoneNumber == null || hotel.getPhoneNumber().equals(phoneNumber)
                ) &&
                (totalRoomNumbers == null || hotel.getMaxNumberRooms().equals(totalRoomNumbers)
                );
    }

    //  GET a hotel using id
    public HotelDto findById(int id){
        HotelEntity hotelEntity = hotelRepository.findByIdHotel(id);
        if(hotelEntity == null) {
            throw HotelException.hotelDoesNotExist();
        }
        return entityToDto(hotelEntity);
    }


    //  UPDATE HOTEL'S INFO => PUT
    public HotelDto updateHotel(int id, HotelDto hotel){
        HotelEntity hotelEntity = hotelRepository.findById(id).orElse(null);
        if(hotelEntity == null){
            throw HotelException.hotelDoesNotExist();
        }
        Optional<HotelEntity> existingHotel = hotelRepository.findByAddress(hotel.getAddress());
        if (existingHotel.isPresent() && existingHotel.get().getCity().equals(hotel.getCity())) {
            throw HotelException.hotelWithSameAddressAlreadyExists();
        }
        if(hotelEntity.getRooms().size() > hotel.getMaxNumberRooms())
        {
            throw  HotelException.hotelNotUpdated();
        }
        hotelEntity.setCity(hotel.getCity());
        hotelEntity.setAddress(hotel.getAddress());
        hotelEntity.setPhoneNumber(hotel.getPhoneNumber());
        hotelEntity.setMaxNumberRooms(hotel.getMaxNumberRooms());

        return entityToDto(hotelRepository.save(hotelEntity));
    }

    //  DELETE A HOTEL
    public void deleteHotel(int id){
        HotelEntity hotelEntity = hotelRepository.findById(id).orElse(null);
        if(hotelEntity == null){
            throw HotelException.hotelDoesNotExist();
        }
        hotelRepository.deleteById(id);
    }
}
