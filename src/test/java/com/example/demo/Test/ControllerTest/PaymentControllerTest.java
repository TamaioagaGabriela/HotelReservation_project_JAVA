package com.example.demo.Test.ControllerTest;

import com.example.demo.controller.PaymentController;
import com.example.demo.dto.ClientDto;
import com.example.demo.dto.PaymentDto;
import com.example.demo.dto.ReservationDto;
import com.example.demo.dto.RoomDto;
import com.example.demo.entity.*;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.service.PaymentService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PaymentController.class)
@EnableTransactionManagement
@ComponentScan
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaymentService paymentService;


    private static PaymentDto paymentDto;
    private static PaymentDto paymentDto1;
    private static PaymentDto payment;
    private static PaymentEntity paymentEntity;
    private static List<PaymentDto> paymentDtos;


    @BeforeAll
    public static void setup() {

        paymentDto = new PaymentDto(11, PaymentCurrency.RON, PaymentMethod.CARD,
                PaymentStatus.EFFECTUATED, (double) 1000, LocalDateTime.of(2021,2, 11,15,30,0),
                21, "Pop Ionel", "IOPOP", 37, 17, 8);
        paymentDtos = new ArrayList<>();
        paymentDtos.add(paymentDto);

        payment = new PaymentDto(11, PaymentCurrency.RON, PaymentMethod.CARD,
                PaymentStatus.EFFECTUATED, (double) 1000, null,
                21, "Pop Ionel", "IOPOP", 37, 17, 8);

        paymentDto1 = new PaymentDto(11, PaymentCurrency.RON, PaymentMethod.CARD,
                PaymentStatus.EFFECTUATED, (double) 0, LocalDateTime.of(2021,2, 11,15,30,0),
                21, "Pop Ionel", "IOPOP", 37, 17, 8);
    }

    @Test
    public void testGetPaymentById() throws Exception{
        ObjectMapper mapper = new ObjectMapper();

        given(paymentService.getPaymentById(paymentDto.getIdPayment()))
                .willReturn(paymentDto);

        mockMvc.perform(get("/api/payment/{id}", paymentDto.getIdPayment())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testGetPayments() throws Exception{

        given(paymentService.getPayments(paymentDto.getCurrency(), paymentDto.getPaymentMethod(),
                paymentDto.getPaymentStatus(), paymentDto.getAmount(), null))
                .willReturn(paymentDtos);

        String endpoint ="/api/payment?currency=%s&paymentMethod=%s&paymentStatus=%s&amount=%s";
        mockMvc.perform(get(String.format(endpoint, paymentDto.getCurrency(), paymentDto.getPaymentMethod(),
                paymentDto.getPaymentStatus(), paymentDto.getAmount()))
               .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(paymentDtos)));

    }

    @Test
    public void testGetPaymentByIdClient() throws Exception{
        given(paymentService.findAllByIdClientPayment(paymentDto.getIdSender()))
                .willReturn(paymentDtos);

        String endpoint ="/api/payment?idClient=%s";
        mockMvc.perform(get(String.format(endpoint, paymentDto.getIdSender()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(paymentDtos)));
    }

    @Test
    public void testGetPaymentByIdReservation() throws Exception{
        given(paymentService.findAllByIdReservationPayment(paymentDto.getIdReservation()))
                .willReturn(paymentDto);

        String endpoint ="/api/payment?idReservation=%s";
        mockMvc.perform(get(String.format(endpoint, paymentDto.getIdReservation()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(paymentDto)));
    }

    @Test
    public void testAddPayment() throws Exception{
        ObjectMapper mapper = new ObjectMapper();

        given(paymentService.addPayment(any())).willReturn(payment);

        mockMvc.perform(
                post("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payment))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

    }

    @Test
    public void testUpdatePayment() throws Exception{

        paymentEntity = new PaymentEntity();
        paymentEntity.setPaymentStatus(paymentDto.getPaymentStatus());
        paymentEntity.setPaymentMethod(paymentDto.getPaymentMethod());
        paymentEntity.setAmount(paymentDto.getAmount());
        paymentEntity.setCurrency(paymentDto.getCurrency());
        paymentEntity.setPaymentDate(paymentDto.getPaymentDate());
        paymentEntity.setIdPayment(paymentDto.getIdPayment());

        given(paymentService.updatePayment( anyInt(), any()) ).willReturn(paymentDto);

        mockMvc.perform(
                put("/api/payment/{id}" , 11)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    public void testDeletePaymentById() throws Exception{
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(delete("/api/payment/{id}", payment.getIdPayment())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
