package com.example.demo.Test.ControllerTest;

import com.example.demo.controller.ReviewController;
import com.example.demo.dto.ClientDto;
import com.example.demo.dto.HotelDto;
import com.example.demo.dto.ReviewDto;
import com.example.demo.dto.RoomDto;
import com.example.demo.entity.ReviewEntity;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(controllers = ReviewController.class)
@EnableTransactionManagement
@ComponentScan
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;


    private static ReviewDto reviewDto1;
    private static ReviewDto reviewWithoutId;
    private static ReviewDto reviewDto2;
    private static ReviewDto reviewDtoNull;
    private static ClientDto clientDto;
    private static HotelDto hotelDto;

    private static ReviewEntity reviewEntity;
    private static List<ReviewDto> reviewDtos;
    private static Set<RoomDto> roomDtos;

    @BeforeAll
    public static void setup(){

        reviewDto1 = new ReviewDto(13, "review hotel", "hotel excelent",
                3, 21, "Ionel", "Pop", 8,
                "Iasi", "Strada Victoriei 54");

        // reviewDto2 are rate setat 4
        reviewDto2 = new ReviewDto(13, "review hotel", "hotel excelent",
                4, 21, "Ionel", "Pop", 8,
                "Iasi", "Strada Victoriei 54");

        reviewDtos = new ArrayList<>();
        reviewDtos.add(reviewDto1);


        roomDtos = new HashSet<>();

        clientDto = new ClientDto(21, "Ionel", "Pop", "IOPOP", "3902485324", reviewDtos, null, null);

        hotelDto = new HotelDto(8, "Strada Victoriei 54", "Iasi", "54654645", 34, roomDtos, reviewDtos);

        reviewDtoNull = new ReviewDto(13, "review hotel", "hotel excelent",
                3, 0, null, null, 0,
                null, null);

        reviewWithoutId = new ReviewDto("review hotel", "hotel excelent",
                3, 21, "Ionel", "Pop", 8,
                "Iasi", "Strada Victoriei 54");
    }

    @Test
    public void testGetReviewById() throws Exception{
        ObjectMapper mapper = new ObjectMapper();

        given(reviewService.getReviewById(reviewDto1.getIdReview()))
                .willReturn(reviewDto1);


        mockMvc.perform(get("/api/review/{id}", reviewDto1.getIdReview())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(reviewDto1)));
    }

    @Test
    public void testGetReviews() throws Exception{
        ObjectMapper mapper = new ObjectMapper();

        given(reviewService.getReviews(reviewDto1.getSubject(), reviewDto1.getDescription(), reviewDto1.getRate()))
                .willReturn(reviewDtos);

        String endpoint ="/api/review?subject=%s&description=%s&rate=%s";
        mockMvc.perform(get(String.format(endpoint, reviewDto1.getSubject(), reviewDto1.getDescription(), reviewDto1.getRate())).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(reviewDtos)));
    }

    @Test
    public void testGetReviewByIdClient() throws Exception{
        given(reviewService.findAllByIdClientReview(reviewDto1.getIdClientReview()))
                .willReturn(reviewDtos);

        String endpoint ="/api/review?idClient=%s";
        mockMvc.perform(get(String.format(endpoint, reviewDto1.getIdClientReview()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(reviewDtos)));
    }

    @Test
    public void testGetReviewByIdHotel() throws Exception{
        given(reviewService.findAllByIdHotelReview(reviewDto1.getIdHotelReview()))
                .willReturn(reviewDtos);

        String endpoint ="/api/review?idHotel=%s";
        mockMvc.perform(get(String.format(endpoint, reviewDto1.getIdHotelReview()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(reviewDtos)));
    }

    @Test
    public void testUpdateReview() throws Exception{
        ObjectMapper mapper = new ObjectMapper();

        reviewEntity = new ReviewEntity();
        reviewEntity.setIdReview(reviewDto1.getIdReview());
        reviewEntity.setRate(reviewDto1.getRate());
        reviewEntity.setDescription(reviewDto1.getDescription());
        reviewEntity.setSubject(reviewDto1.getSubject());

        when(reviewService.updateReview(anyInt(), any())).thenReturn(reviewDto1);
        mockMvc.perform(
                put("/api/review/{id}" , 13)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDto1))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(reviewDto1)));
    }

    @Test
    public void testAddReview() throws Exception{
        ObjectMapper mapper = new ObjectMapper();

        reviewEntity = new ReviewEntity();
        reviewEntity.setIdReview(reviewDto1.getIdReview());
        reviewEntity.setRate(reviewDto1.getRate());
        reviewEntity.setDescription(reviewDto1.getDescription());
        reviewEntity.setSubject(reviewDto1.getSubject());

        given(reviewService.addReview(any())).willReturn(reviewEntity);

        mockMvc.perform(
                post("/api/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(reviewEntity))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

    }

    @Test
    public void testDeleteReviewById() throws Exception{
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(delete("/api/review/{id}", reviewDto1.getIdReview())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}
