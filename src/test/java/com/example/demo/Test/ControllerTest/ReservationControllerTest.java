package com.example.demo.Test.ControllerTest;

import com.example.demo.controller.ReservationController;
import com.example.demo.controller.ReviewController;
import com.example.demo.dto.ClientDto;
import com.example.demo.dto.PaymentDto;
import com.example.demo.dto.ReservationDto;
import com.example.demo.dto.RoomDto;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.ReservationService;
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

@WebMvcTest(controllers = ReservationController.class)
@EnableTransactionManagement
@ComponentScan
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;

    private static ReservationDto reservationDto;
    private static ReservationDto reservationDtoAdd;
    private static List<ReservationDto> reservationDtos;
    private static ReservationEntity reservationEntity;


    @BeforeAll
    public static void setup(){

        reservationDto = new ReservationDto(13, 21, "Ionel",
                "Pop", 5, RoomType.SUITE, 19, 500, "Iasi",
                "Strada Victoriei 54", LocalDateTime.of(2021,2, 11,15,0,0),
                LocalDateTime.of(2021,2, 13,11,30,0), 2, 1000 );

        reservationDtoAdd = new ReservationDto(13, 21, "Ionel",
                "Pop", 5, RoomType.SUITE, 19, 500, "Iasi",
                "Strada Victoriei 54", null, null, 2, 1000 );

        reservationDtos = new ArrayList<>();
        reservationDtos.add(reservationDto);

    }


    @Test
    public void testAddReservation() throws Exception{
        ObjectMapper mapper = new ObjectMapper();

        reservationEntity = new ReservationEntity();
        reservationEntity.setIdReservation(reservationDtoAdd.getIdReservation());
        reservationEntity.setPeriod(reservationDtoAdd.getPeriod());
        reservationEntity.setEndDate(reservationDtoAdd.getEndDate());
        reservationEntity.setStartDate(reservationDtoAdd.getStartDate());

        given(reservationService.addReservation(any())).willReturn(reservationEntity);

        mockMvc.perform(
                post("/api/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reservationDtoAdd))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

    }

    @Test
    public void testUpdateReservation() throws Exception{
        ObjectMapper mapper = new ObjectMapper();

        reservationEntity = new ReservationEntity();
        reservationEntity.setIdReservation(reservationDtoAdd.getIdReservation());
        reservationEntity.setPeriod(reservationDtoAdd.getPeriod());
        reservationEntity.setEndDate(reservationDtoAdd.getEndDate());
        reservationEntity.setStartDate(reservationDtoAdd.getStartDate());

        given(reservationService.updateReservation( anyInt(), any()) ).willReturn(reservationDtoAdd);

        mockMvc.perform(
                put("/api/reservation/{id}" , 13)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reservationDtoAdd))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    public void testGetReservationById() throws Exception{
        ObjectMapper mapper = new ObjectMapper();

        given(reservationService.getReservationById(reservationDto.getIdReservation()))
                .willReturn(reservationDto);

        mockMvc.perform(get("/api/reservation/{id}", reservationDto.getIdReservation())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testGetReservationByIdClient() throws Exception{
        given(reservationService.findAllByIdClientReservation(reservationDto.getIdClientReservation()))
                .willReturn(reservationDtos);

        String endpoint ="/api/reservation?idClient=%s";
        mockMvc.perform(get(String.format(endpoint, reservationDto.getIdClientReservation()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(reservationDtos)));
    }



    @Test
    public void testGetReservationByIdRoom() throws Exception{
        given(reservationService.findAllByIdRoomReservation(reservationDto.getIdRoomReservation()))
                .willReturn(reservationDtos);

        String endpoint ="/api/reservation?idRoom=%s";
        mockMvc.perform(get(String.format(endpoint, reservationDto.getIdRoomReservation()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(reservationDtos)));
    }

    @Test
    public void testGetReservations() throws Exception{

        given(reservationService.getReservations(null, null, reservationDto.getPeriod()))
                .willReturn(reservationDtos);

        String endpoint ="/api/reservation?period=%s";
        mockMvc.perform(get(String.format(endpoint, reservationDto.getPeriod()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(reservationDtos)));

    }


    @Test
    public void testDeleteReservationById() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        mockMvc.perform(delete("/api/reservation/{id}", reservationDto.getIdReservation())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
