package com.example.demo.Test.ServiceTest;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.exception.PaymentException;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private PaymentService paymentService;

    @InjectMocks
    private ClientService clientService;

    @InjectMocks
    private HotelService hotelService;

    @InjectMocks
    private RoomService roomService;

    @Mock
    private ReservationService reservationServiceMock;

    @InjectMocks
    private ReservationService reservationService;

    @InjectMocks
    private ReviewService reviewService;


    private static PaymentDto paymentDto;
    private static PaymentDto paymentDto1;
    private static ClientDto clientDto;
    private static ReservationDto reservationDto1;
    private static ReservationDto reservationDto2;
    private static RoomDto roomDto;
    private static List<PaymentDto> paymentDtos;
    private static List<ReservationDto> reservationDtos;


    private static PaymentEntity paymentEntity;
    private static PaymentEntity paymentEntity1;
    private static ReservationEntity reservationEntity;
    private static RoomEntity roomEntity;
    private static ClientEntity clientEntity;
    private static List<PaymentEntity> paymentEntities;


    @BeforeAll
    public static void setup(){

        reservationDtos = new ArrayList<>();
        paymentEntities = new ArrayList<>();

        paymentDto = new PaymentDto(11, PaymentCurrency.RON, PaymentMethod.CARD,
                PaymentStatus.EFFECTUATED, (double) 1000, LocalDateTime.of(2021,2, 11,15,30,0),
                21, "Pop Ionel", "IOPOP", 37, 17, 8);

        paymentDto1 = new PaymentDto(11, PaymentCurrency.RON, PaymentMethod.CARD,
                PaymentStatus.EFFECTUATED, (double) 0, LocalDateTime.of(2021,2, 11,15,30,0),
                21, "Pop Ionel", "IOPOP", 37, 17, 8);
        paymentDtos = new ArrayList<>();
        paymentDtos.add(new PaymentDto(paymentDto.getIdPayment(), paymentDto.getCurrency(), paymentDto.getPaymentMethod(),
                paymentDto.getPaymentStatus(), paymentDto.getAmount(), paymentDto.getPaymentDate(),
                paymentDto.getIdSender(), paymentDto.getClientName(), paymentDto.getClientEmail(),
                paymentDto.getIdReservation(), paymentDto.getRoomNumber(), paymentDto.getIdHotel()));

        reservationDto1 = new ReservationDto(13, 21, "Ionel", "Pop",
                5, RoomType.SUITE, 19, 500,
                "Iasi", "Strada Victoriei 54",
                LocalDateTime.of(2021,2, 11,15,0,0),
                LocalDateTime.of(2021,2, 13,11,30,0), 2, 1000 );

        reservationDtos.add(new ReservationDto(reservationDto1.getIdReservation(), reservationDto1.getIdClientReservation(),
                reservationDto1.getClientLastName(), reservationDto1.getClientFirstName(), reservationDto1.getIdRoomReservation(),
                reservationDto1.getRoomType(), reservationDto1.getRoomNumber(), reservationDto1.getCost(),
                reservationDto1.getHotelCity(), reservationDto1.getHotelAddress(),
                reservationDto1.getStartDate(), reservationDto1.getEndDate(), reservationDto1.getPeriod(),
                reservationDto1.getCostTotal()));

        roomDto = new RoomDto(5, 19, 4, 2, 500,
                RoomType.SUITE, 8, "Strada Victoriei 54" , reservationDtos);

        clientDto = new ClientDto(21, "Ionel", "Pop", "IOPOP",
                "3902485324", null, paymentDtos, null);

    }

    @Test
    @DisplayName("Test invalid amount")
    public void testInvalidAmount(){
        reservationEntity = reservationService.dtoToEntity(reservationDto1);
        Mockito.lenient().when(reservationRepository.findByIdReservation(paymentDto1.getIdReservation())).thenReturn(reservationEntity);

        roomEntity = roomService.dtoToEntity(roomDto);
        Mockito.lenient().when(roomRepository.findByIdRoom(reservationDto1.getIdReservation())).thenReturn(roomEntity);

        paymentEntity1 = paymentService.dtoToEntity(paymentDto1);
        paymentEntity1.setIdPayment(paymentDto1.getIdPayment());
        Mockito.lenient().when(paymentRepository.save(paymentEntity1)).thenReturn(paymentEntity1);

        PaymentException exception = assertThrows(PaymentException.class, () ->
                paymentService.addPayment(paymentDto1));
        assertEquals(PaymentException.PaymentErrors.PAYMENT_AMOUNT_CANNOT_BE_ZERO, exception.getError());
    }


    @Test
    @DisplayName("Test payments not found")
    public void testGetPayments(){
        paymentEntities = paymentDtos.stream()
                .map(paymentDto -> paymentService.dtoToEntity(paymentDto))
                .collect(Collectors.toList());
        when(paymentRepository.findAll()).thenReturn(paymentEntities);
        PaymentException exception = assertThrows(PaymentException.class, () ->
                paymentService.getPayments(PaymentCurrency.USD, PaymentMethod.CARD,
                        PaymentStatus.EFFECTUATED, 1000.,
                        LocalDateTime.of(2021,2, 11,15,30,0)));
        assertEquals(PaymentException.PaymentErrors.PAYMENT_DOES_NOT_EXIST, exception.getError());
    }


    @Test
    @DisplayName("Test payment with the given id not found")
    public void testGetPaymentById(){
        paymentEntity = paymentService.dtoToEntity(paymentDto);
        paymentEntity.setIdPayment(paymentDto.getIdPayment());
        Mockito.lenient().when(paymentRepository.findByIdPayment(11)).thenReturn(paymentEntity);
        PaymentException exception = assertThrows(PaymentException.class, () ->
                paymentService.getPaymentById(10));
        assertEquals(PaymentException.PaymentErrors.PAYMENT_DOES_NOT_EXIST, exception.getError());
    }

    @Test
    @DisplayName("Test payment with client not found")
    public void testFindAllByIdClientPayment(){
        Mockito.lenient().doReturn(Optional.empty()).when(clientRepository).findById(paymentDto.getIdSender());
        PaymentException exception = assertThrows(PaymentException.class, () ->
                paymentService.findAllByIdClientPayment(21));
        assertEquals(PaymentException.PaymentErrors.CLIENT_DOES_NOT_EXIST, exception.getError());
    }

    @Test
    @DisplayName("Test payment update id not found")
    public void testUpdatePaymentIdNotFound(){

        paymentEntity = paymentService.dtoToEntity(paymentDto);
        paymentEntity.setIdPayment(paymentDto.getIdPayment());
        Mockito.lenient().when(paymentRepository.findById(paymentDto.getIdPayment())).thenReturn(Optional.ofNullable(paymentEntity));
        PaymentException exception = assertThrows(PaymentException.class, () ->
                paymentService.updatePayment(10, paymentDto));
        assertEquals(PaymentException.PaymentErrors.PAYMENT_DOES_NOT_EXIST, exception.getError());
    }


    @Test
    @DisplayName("Test payment not deleted")
    public void testDeletePayment(){
        paymentEntity = paymentService.dtoToEntity(paymentDto);
        paymentEntity.setIdPayment(paymentDto.getIdPayment());
        Mockito.lenient().when(paymentRepository.findByIdPayment(paymentDto.getIdPayment())).thenReturn(paymentEntity);
        PaymentException exception = assertThrows(PaymentException.class, () ->
                paymentService.deletePayment(10));
        assertEquals(PaymentException.PaymentErrors.PAYMENT_DOES_NOT_EXIST, exception.getError());
    }

}
