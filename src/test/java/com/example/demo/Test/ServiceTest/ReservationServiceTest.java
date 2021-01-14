package com.example.demo.Test.ServiceTest;

import com.example.demo.dto.ClientDto;
import com.example.demo.dto.PaymentDto;
import com.example.demo.dto.ReservationDto;
import com.example.demo.dto.RoomDto;
import com.example.demo.entity.*;
import com.example.demo.exception.ReservationException;
import com.example.demo.exception.ReviewException;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

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
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReservationService reservationService;

    @InjectMocks
    private ReviewService reviewService;



    private static ReservationDto reservationDto;
    private static ReservationDto reservationDtoWrong;
    private static PaymentDto paymentDto;
    private static ClientDto clientDto;
    private static RoomDto roomDto;

    private static List<ReservationDto> reservationDtos;
    private static List<ReservationEntity> reservationEntities;

    private static ReservationEntity reservationEntity;
    private static ReservationEntity reservationEntityWrong;


    @BeforeAll
    public static void setup(){

        reservationDto = new ReservationDto(13, 21, "Ionel",
                "Pop", 5, RoomType.SUITE, 19, 500, "Iasi",
                "Strada Victoriei 54", LocalDateTime.of(2021,2, 11,15,0,0),
                LocalDateTime.of(2021,2, 13,11,30,0), 2, 1000 );

        reservationDtoWrong = new ReservationDto(13, 21, "Ionel",
                "Pop", 5, RoomType.SUITE, 19, 500, "Iasi",
                "Strada Victoriei 54", LocalDateTime.of(2021,2, 11,10,0,0),
                LocalDateTime.of(2021,2, 13,11,30,0), 2, 1000 );

        paymentDto = new PaymentDto(11, PaymentCurrency.RON, PaymentMethod.CARD,
                PaymentStatus.EFFECTUATED, (double) 1000, LocalDateTime.of(2021,2, 11,15,30,0),
                21, "Pop Ionel", "IOPOP", 13, 17, 8);

        reservationDtos = new ArrayList<>();
        reservationDtos.add(reservationDto);

        clientDto = new ClientDto(21, "Ionel", "Pop", "IOPOP",
                "3902485324", null, null, reservationDtos);

        roomDto = new RoomDto(5, 19, 4, 2, 500,
                RoomType.SUITE, 8, "Strada Victoriei 54" , reservationDtos);
    }

    @Test
    @DisplayName("Test reservation room unavailable")
    public void testRoomTypeUnavailable(){

        reservationEntityWrong = reservationService.dtoToEntity(reservationDtoWrong);
        reservationEntityWrong.setIdReservation(reservationDtoWrong.getIdReservation());
        Mockito.lenient().when(reservationRepository.save(reservationEntityWrong)).thenReturn(reservationEntityWrong);

        ReservationException exception = assertThrows(ReservationException.class, () ->
                reservationService.addReservation(reservationDtoWrong));
        assertEquals(ReservationException.ReservationErrors.RESERVATION_ROOMTYPE_NOT_AVAILABLE, exception.getError());
    }

    @Test
    @DisplayName("Test reservations not found")
    public void testGetReservations(){
        reservationEntities = reservationDtos.stream()
                .map(reservationDto -> reservationService.dtoToEntity(reservationDto))
                .collect(Collectors.toList());
        when(reservationRepository.findAll()).thenReturn(reservationEntities);
        ReservationException exception = assertThrows(ReservationException.class, () ->
                reservationService.getReservations(reservationDtoWrong.getStartDate(),
                        reservationDto.getEndDate(), reservationDto.getPeriod()));
        assertEquals(ReservationException.ReservationErrors.RESERVATION_DOES_NOT_EXIST, exception.getError());
    }

    @Test
    @DisplayName("Test reservation with the given id not found")
    public void testGetReservationById(){
        reservationEntity = reservationService.dtoToEntity(reservationDto);
        reservationEntity.setIdReservation(reservationDto.getIdReservation());
        Mockito.lenient().when(reservationRepository.findByIdReservation(reservationDto.getIdReservation())).thenReturn(reservationEntity);
        ReservationException exception = assertThrows(ReservationException.class, () ->
                reservationService.getReservationById(10));
        assertEquals(ReservationException.ReservationErrors.RESERVATION_DOES_NOT_EXIST, exception.getError());
    }

    @Test
    @DisplayName("Test reservation with client not found")
    public void testFindAllByIdClientReservation(){
        Mockito.lenient().doReturn(Optional.empty()).when(clientRepository).findById(reservationDto.getIdClientReservation());
        ReservationException exception = assertThrows(ReservationException.class, () ->
                reservationService.findAllByIdClientReservation(21));
        assertEquals(ReservationException.ReservationErrors.CLIENT_DOES_NOT_EXIST, exception.getError());
    }

    @Test
    @DisplayName("Test reservation with room id not found")
    public void testFindAllByIdRoomReservation(){
        Mockito.lenient().doReturn(Optional.empty()).when(roomRepository).findById(reservationDto.getIdRoomReservation());
        ReservationException exception = assertThrows(ReservationException.class, () ->
                reservationService.findAllByIdRoomReservation(21));
        assertEquals(ReservationException.ReservationErrors.ROOM_DOES_NOT_EXIST, exception.getError());
    }

    @Test
    @DisplayName("Test reservation update id not found")
    public void testUpdateReservationIdNotFound(){

        reservationEntity = reservationService.dtoToEntity(reservationDto);
        reservationEntity.setIdReservation(reservationDto.getIdReservation());
        Mockito.lenient().when(reservationRepository.findById(reservationDto.getIdReservation())).thenReturn(Optional.ofNullable(reservationEntity));
        ReservationException exception = assertThrows(ReservationException.class, () ->
                reservationService.updateReservation(10, reservationDto));
        assertEquals(ReservationException.ReservationErrors.RESERVATION_DOES_NOT_EXIST, exception.getError());
    }

    @Test
    @DisplayName("Test reservation not deleted")
    public void testDeleteReservation(){
        reservationEntity = reservationService.dtoToEntity(reservationDto);
        reservationEntity.setIdReservation(reservationDto.getIdReservation());
        Mockito.lenient().when(reservationRepository.findById(reservationDto.getIdReservation())).thenReturn(java.util.Optional.ofNullable(reservationEntity));
        ReservationException exception = assertThrows(ReservationException.class, () ->
                reservationService.deleteReservation(10));
        assertEquals(ReservationException.ReservationErrors.RESERVATION_DOES_NOT_EXIST, exception.getError());
    }
}
