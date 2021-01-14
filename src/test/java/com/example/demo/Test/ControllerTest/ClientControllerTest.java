package com.example.demo.Test.ControllerTest;

import com.example.demo.controller.ClientController;
import com.example.demo.controller.ReviewController;
import com.example.demo.dto.ClientDto;
import com.example.demo.dto.PaymentDto;
import com.example.demo.dto.ReservationDto;
import com.example.demo.dto.ReviewDto;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.ClientService;
import com.example.demo.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ClientController.class)
@EnableTransactionManagement
@ComponentScan
public class ClientControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;


    private static ClientDto clientDto;
    private static ClientDto clientDto2;
    private static ReservationDto reservationDto;
    private static PaymentDto paymentDto;
    private static ReviewDto reviewDto;

    private static List<ReservationDto> reservationDtos;
    private static List<PaymentDto> paymentDtos;
    private static List<ReviewDto> reviewDtos;
    private static List<ClientDto> clientDtos;

    private static ClientEntity clientEntity;



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


        clientDto2 = new ClientDto(22, "Barbu", "Gigel", "IOPOP",
                "390245554", null, null, null);
        clientDtos = new ArrayList<>();
        clientDtos.add(clientDto2);

    }



    @Test
    public void testGetClientById() throws Exception{

        given(clientService.getClientById(clientDto2.getIdClient()))
                .willReturn(clientDto2);

        mockMvc.perform(get("/api/client/{id}", clientDto2.getIdClient())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(clientDto2)));
    }


    @Test
    public void testGetClients() throws Exception{

        given(clientService.getClients(clientDto2.getLastName(), clientDto2.getFirstName(),
                clientDto2.getEmail(), clientDto2.getPhoneNumberClient())).willReturn(clientDtos);

        String endpoint ="/api/client?lastName=%s&firstName=%s&email=%s&phoneNumberClient=%s";
        mockMvc.perform(get(String.format(endpoint, clientDto2.getLastName(), clientDto2.getFirstName(),
                clientDto2.getEmail(), clientDto2.getPhoneNumberClient()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(clientDtos)));
    }

    @Test
    public void testAddClient() throws Exception{
        ObjectMapper mapper = new ObjectMapper();

        clientEntity = new ClientEntity();
        clientEntity.setIdClient(clientDto.getIdClient());
        clientEntity.setEmail(clientDto.getEmail());
        clientEntity.setPhoneNumberClient(clientDto.getPhoneNumberClient());
        clientEntity.setFirstName(clientDto.getFirstName());
        clientEntity.setLastName(clientDto.getLastName());

        given(clientService.addClient(any())).willReturn(clientEntity);

        mockMvc.perform(
                post("/api/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clientEntity))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

    }

    @Test
    public void testUpdateClient() throws Exception{

        given(clientService.updateClient( anyInt(), any()) ).willReturn(clientDto);

        mockMvc.perform(
                put("/api/client/{id}" , 21)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    public void testDeleteClientById() throws Exception{
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(delete("/api/client/{id}", clientDto.getIdClient())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
