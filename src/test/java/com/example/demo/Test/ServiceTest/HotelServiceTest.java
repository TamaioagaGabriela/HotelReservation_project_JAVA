package com.example.demo.Test.ServiceTest;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.exception.HotelException;
import com.example.demo.repository.*;
import com.example.demo.service.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class HotelServiceTest {


    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomService roomServiceMock;

    @Mock
    private ReviewService reviewServiceMock;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private HotelService hotelService;


    private static HotelDto hotelDto;
    private static HotelDto hotelDtoSameAddress;
    private static RoomDto roomDto;
    private static ReviewDto reviewDto;

    private static Set<RoomDto> roomDtos;
    private static List<ReviewDto> reviewDtos;
    private static List<HotelDto> hotelDtos;

    private static HotelEntity hotelEntity;
    private static List<HotelEntity> hotelEntities;

    @BeforeAll
    public static void setup(){

        reviewDto = new ReviewDto(13, "review hotel", "hotel excelent",
                3, 21, "Ionel", "Pop", 8,
                "Iasi", "Strada Victoriei 54");
        reviewDtos = new ArrayList<>();
        reviewDtos.add(reviewDto);

        roomDto = new RoomDto(5, 19, 4, 2, 500,
                RoomType.SUITE, 8, "Strada Victoriei 54" , null);
        roomDtos = new HashSet<>();
        roomDtos.add(roomDto);

        hotelDto = new HotelDto(8, "Strada Victoriei 54", "Iasi", "54654645",
                34, roomDtos, reviewDtos);
        hotelDtos = new ArrayList<>();
        hotelDtos.add(hotelDto);

        hotelDtoSameAddress = new HotelDto(9, "Strada Victoriei 54", "Iasi", "54654645",
                34, roomDtos, reviewDtos);

    }

    @Test
    @DisplayName("Test address already exists")
    public void testAddressAlreadyExists(){
        hotelEntity = hotelService.dtoToEntity(hotelDto);
        hotelEntity.setIdHotel(hotelDto.getIdHotel());
        Mockito.lenient().when(hotelRepository.findByAddress(hotelDto.getAddress())).thenReturn(Optional.ofNullable(hotelEntity));
        HotelException exception = assertThrows(HotelException.class, () ->
                hotelService.addHotel(hotelDtoSameAddress));
        assertEquals(HotelException.HotelErrors.HOTEL_WITH_SAME_ADDRESS_ALREADY_EXISTS, exception.getError());
    }

    @Test
    @DisplayName("Test hotels not found")
    public void testGetHotels(){
        hotelEntities = hotelDtos.stream()
                .map(hotelDto -> hotelService.dtoToEntity(hotelDto))
                .collect(Collectors.toList());
        Mockito.lenient().when(hotelRepository.findAll()).thenReturn(hotelEntities);
        HotelException exception = assertThrows(HotelException.class, () ->
                hotelService.getHotels("Bucuresti", hotelDto.getAddress(), hotelDto.getPhoneNumber(), hotelDto.getMaxNumberRooms()));
        assertEquals(HotelException.HotelErrors.HOTEL_DOES_NOT_EXIST, exception.getError());
    }

    @Test
    @DisplayName("Test hotel with the given id not found")
    public void testGetHotelById(){
        hotelEntity = hotelService.dtoToEntity(hotelDto);
        hotelEntity.setIdHotel(hotelDto.getIdHotel());
        Mockito.lenient().when(hotelRepository.findByIdHotel(hotelDto.getIdHotel())).thenReturn(hotelEntity);
        HotelException exception = assertThrows(HotelException.class, () ->
                hotelService.findById(10));
        assertEquals(HotelException.HotelErrors.HOTEL_DOES_NOT_EXIST, exception.getError());
    }

    @Test
    @DisplayName("Test hotel update id not found")
    public void testUpdateHotelIdNotFound(){
        hotelEntity = hotelService.dtoToEntity(hotelDto);
        hotelEntity.setIdHotel(hotelDto.getIdHotel());
        Mockito.lenient().when(hotelRepository.findById(hotelDto.getIdHotel())).thenReturn(java.util.Optional.ofNullable(hotelEntity));
        HotelException exception = assertThrows(HotelException.class, () ->
                hotelService.updateHotel(10, hotelDto));
        assertEquals(HotelException.HotelErrors.HOTEL_DOES_NOT_EXIST, exception.getError());
    }

    @Test
    @DisplayName("Test hotel not deleted")
    public void testDeleteHotel(){
        hotelEntity = hotelService.dtoToEntity(hotelDto);
        hotelEntity.setIdHotel(hotelDto.getIdHotel());
        Mockito.lenient().when(hotelRepository.findById(hotelDto.getIdHotel())).thenReturn(java.util.Optional.ofNullable(hotelEntity));
        HotelException exception = assertThrows(HotelException.class, () ->
                hotelService.deleteHotel(10));
        assertEquals(HotelException.HotelErrors.HOTEL_DOES_NOT_EXIST, exception.getError());
    }

}
