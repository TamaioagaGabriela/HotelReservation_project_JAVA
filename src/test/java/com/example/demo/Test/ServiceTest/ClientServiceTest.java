package com.example.demo.Test.ServiceTest;

import com.example.demo.dto.ClientDto;
import com.example.demo.dto.PaymentDto;
import com.example.demo.dto.ReservationDto;
import com.example.demo.dto.ReviewDto;
import com.example.demo.entity.*;
import com.example.demo.exception.ClientException;
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

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

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
    private PaymentService paymentService;

    @InjectMocks
    private ClientService clientService;


    private static ClientDto clientDto;
    private static ClientDto clientDtoSameEmail;
    private static ClientDto clientDtoNullName;
    private static ReservationDto reservationDto;
    private static PaymentDto paymentDto;
    private static ReviewDto reviewDto;

    private static List<ReservationDto> reservationDtos;
    private static List<PaymentDto> paymentDtos;
    private static List<ReviewDto> reviewDtos;
    private static List<ClientDto> clientDtos;

    private static ClientEntity clientEntity;
    private static List<ClientEntity> clientEntities;


    @BeforeAll
    public static void setup() {

        reviewDto = new ReviewDto(13, "review hotel", "hotel excelent",
                3, 21, "Ionel", "Pop", 8,
                "Iasi", "Strada Victoriei 54");
        reviewDtos = new ArrayList<>();
        reviewDtos.add(reviewDto);

        paymentDto = new PaymentDto(11, PaymentCurrency.RON, PaymentMethod.CARD,
                PaymentStatus.EFFECTUATED, (double) 1000, LocalDateTime.of(2021,2, 11,15,30,0),
                21, "Pop Ionel", "IOPOP", 37, 17, 8);
        paymentDtos = new ArrayList<>();
        paymentDtos.add(paymentDto);

        reservationDto = new ReservationDto(13, 21, "Ionel",
                "Pop", 5, RoomType.SUITE, 19, 500, "Iasi",
                "Strada Victoriei 54", LocalDateTime.of(2021,2, 11,15,0,0),
                LocalDateTime.of(2021,2, 13,11,30,0), 2, 1000 );
        reservationDtos = new ArrayList<>();
        reservationDtos.add(reservationDto);


        clientDto = new ClientDto(21, "Ionel", "Pop", "IOPOP",
                "3902485324", reviewDtos, paymentDtos, reservationDtos);
        clientDtos = new ArrayList<>();
        clientDtos.add(clientDto);

        clientDtoSameEmail = new ClientDto(22, "Barbu", "Gigel", "IOPOP",
                "390245554", reviewDtos, paymentDtos, reservationDtos);
        clientDtoNullName = new ClientDto(22, null, null, "IOPOP",
                "390245554", reviewDtos, paymentDtos, reservationDtos);

    }

    @Test
    @DisplayName("Test email already exists")
    public void testEmailAlreadyExists(){
        clientEntity = clientService.dtoToEntity(clientDto);
        clientEntity.setIdClient(clientDto.getIdClient());
        Mockito.lenient().when(clientRepository.findByEmail(clientDto.getEmail())).thenReturn(Optional.ofNullable(clientEntity));
        ClientException exception = assertThrows(ClientException.class, () ->
                clientService.addClient(clientDtoSameEmail));
        assertEquals(ClientException.ClientErrors.CLIENT_WITH_SAME_EMAIL_ALREADY_EXISTS, exception.getError());
    }


    @Test
    @DisplayName("Test clients not found")
    public void testGetClients(){
        clientEntities = clientDtos.stream()
                .map(clientDto -> clientService.dtoToEntity(clientDto))
                .collect(Collectors.toList());
        Mockito.lenient().when(clientRepository.findAll()).thenReturn(clientEntities);
        ClientException exception = assertThrows(ClientException.class, () ->
                clientService.getClients("bebe", clientDto.getFirstName(), clientDto.getEmail(),
                        clientDto.getPhoneNumberClient()));
        assertEquals(ClientException.ClientErrors.CLIENT_DOES_NOT_EXIST, exception.getError());
    }

    @Test
    @DisplayName("Test client with the given id not found")
    public void testGetClientById(){
        clientEntity = clientService.dtoToEntity(clientDto);
        clientEntity.setIdClient(clientDto.getIdClient());
        Mockito.lenient().when(clientRepository.findByIdClient(clientDto.getIdClient())).thenReturn(clientEntity);
        ClientException exception = assertThrows(ClientException.class, () ->
                clientService.getClientById(10));
        assertEquals(ClientException.ClientErrors.CLIENT_DOES_NOT_EXIST, exception.getError());
    }

    @Test
    @DisplayName("Test client update id not found")
    public void testUpdateClientIdNotFound(){

        clientEntity = clientService.dtoToEntity(clientDto);
        clientEntity.setIdClient(clientDto.getIdClient());
        Mockito.lenient().when(clientRepository.findById(clientDto.getIdClient())).thenReturn(java.util.Optional.ofNullable(clientEntity));
        ClientException exception = assertThrows(ClientException.class, () ->
                clientService.updateClient(10, clientDto));
        assertEquals(ClientException.ClientErrors.CLIENT_DOES_NOT_EXIST, exception.getError());
    }

    @Test
    @DisplayName("Test client not deleted")
    public void testDeleteClient(){
        clientEntity = clientService.dtoToEntity(clientDto);
        clientEntity.setIdClient(clientDto.getIdClient());
        Mockito.lenient().when(clientRepository.findById(clientDto.getIdClient())).thenReturn(java.util.Optional.ofNullable(clientEntity));
        ClientException exception = assertThrows(ClientException.class, () ->
                clientService.deleteClient(10));
        assertEquals(ClientException.ClientErrors.CLIENT_DOES_NOT_EXIST, exception.getError());
    }
}
