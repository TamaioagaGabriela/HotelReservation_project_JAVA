package com.example.demo.Test.ControllerTest;

import com.example.demo.controller.HotelController;
import com.example.demo.controller.ReviewController;
import com.example.demo.dto.HotelDto;
import com.example.demo.dto.ReviewDto;
import com.example.demo.dto.RoomDto;
import com.example.demo.entity.HotelEntity;
import com.example.demo.entity.PaymentEntity;
import com.example.demo.entity.ReviewEntity;
import com.example.demo.entity.RoomType;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.service.HotelService;
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

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = HotelController.class)
@EnableTransactionManagement
@ComponentScan
public class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HotelService hotelService;

    @Autowired
    private ObjectMapper objectMapper;

    private static HotelDto hotelDto;
    private static HotelDto hotelDtoSameAddress;
    private static RoomDto roomDto;
    private static ReviewDto reviewDto;

    private static Set<RoomDto> roomDtos;
    private static List<ReviewDto> reviewDtos;
    private static List<HotelDto> hotelDtos;

    private static HotelEntity hotelEntity;

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
                34, null, null);
        hotelDtos = new ArrayList<>();
        hotelDtos.add(hotelDto);

        hotelDtoSameAddress = new HotelDto(9, "Strada Victoriei 54", "Iasi", "54654645",
                34, roomDtos, reviewDtos);

    }


    @Test
    public void testGetHotelById() throws Exception{
        ObjectMapper mapper = new ObjectMapper();

        given(hotelService.findById(hotelDto.getIdHotel()))
                .willReturn(hotelDto);


        mockMvc.perform(get("/api/hotel/{id}", hotelDto.getIdHotel())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(hotelDto)));
    }

    @Test
    public void testGetHotels() throws Exception{
        ObjectMapper mapper = new ObjectMapper();

        given(hotelService.getHotels(hotelDto.getCity(), hotelDto.getAddress(), hotelDto.getPhoneNumber(),
                hotelDto.getMaxNumberRooms()))
                .willReturn(hotelDtos);

        String endpoint ="/api/hotel?city=%s&address=%s&phoneNumber=%s&totalRoomNumbers=%s";
        mockMvc.perform(get(String.format(endpoint, hotelDto.getCity(), hotelDto.getAddress(), hotelDto.getPhoneNumber(),
                hotelDto.getMaxNumberRooms()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(hotelDtos)));
    }


    @Test
    public void testAddHotel() throws Exception{
        ObjectMapper mapper = new ObjectMapper();

        hotelEntity = new HotelEntity();
        hotelEntity.setIdHotel(hotelDto.getIdHotel());
        hotelEntity.setCity(hotelDto.getCity());
        hotelEntity.setAddress(hotelDto.getAddress());
        hotelEntity.setMaxNumberRooms(hotelDto.getMaxNumberRooms());
        hotelEntity.setPhoneNumber(hotelDto.getPhoneNumber());

        given(hotelService.addHotel(any())).willReturn(hotelEntity);

        mockMvc.perform(
                post("/api/hotel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(hotelEntity))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

    }

    @Test
    public void testUpdateHotel() throws Exception{

        given(hotelService.updateHotel( anyInt(), any()) ).willReturn(hotelDto);

        mockMvc.perform(
                put("/api/hotel/{id}" , 8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotelDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    public void testDeleteHotelById() throws Exception{
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(delete("/api/hotel/{id}", hotelDto.getIdHotel())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
