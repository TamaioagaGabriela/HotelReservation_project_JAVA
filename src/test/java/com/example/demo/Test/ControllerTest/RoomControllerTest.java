package com.example.demo.Test.ControllerTest;

import com.example.demo.controller.ReviewController;
import com.example.demo.controller.RoomController;
import com.example.demo.dto.HotelDto;
import com.example.demo.dto.ReservationDto;
import com.example.demo.dto.RoomDto;
import com.example.demo.entity.ClientEntity;
import com.example.demo.entity.RoomEntity;
import com.example.demo.entity.RoomType;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.service.ReviewService;
import com.example.demo.service.RoomService;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RoomController.class)
@EnableTransactionManagement
@ComponentScan
public class RoomControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomService roomService;

    @Autowired
    private ObjectMapper objectMapper;



    private static RoomDto roomDto;
    private static RoomDto roomDtoSameNumber;
    private static HotelDto hotelDto;

    private static List<ReservationDto> reservationDtos;
    private static List<RoomDto> roomDtos;
    private static Set<RoomDto> roomSetDtos;

    private static RoomEntity roomEntity;
    private static List<RoomEntity> roomEntities;

    @BeforeAll
    public static void setup() {


        roomDto = new RoomDto(5, 19, 4, 2, 500,
                RoomType.SUITE, 8, "Strada Victoriei 54" , null);
        roomDtos = new ArrayList<>();
        roomDtos.add(roomDto);

        roomSetDtos = new HashSet<>();
        roomSetDtos.add(roomDto);

        hotelDto = new HotelDto(8, "Strada Victoriei 54", "Iasi", "54654645",
                34, roomSetDtos, null);

        roomDtoSameNumber = new RoomDto(7, 19, 4, 2, 500,
                RoomType.SUITE, 8, "Strada Victoriei 54" , reservationDtos);

    }


    @Test
    public void testGetRoomById() throws Exception{

        given(roomService.getRoomById(roomDto.getIdRoom()))
                .willReturn(roomDto);

        mockMvc.perform(get("/api/room/{id}", roomDto.getIdRoom())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(roomDto)));
    }

    @Test
    public void testGetRooms() throws Exception{

        given(roomService.getRooms(roomDto.getNumberRoom(), roomDto.getCapacity(), roomDto.getFloorNumber(),
                roomDto.getCost(), roomDto.getRoomType()))
                .willReturn(roomDtos);

        String endpoint ="/api/room?numberRoom=%s&capacity=%s&floorNumber=%s&cost=%s&roomType=%s";
        mockMvc.perform(get(String.format(endpoint, roomDto.getNumberRoom(), roomDto.getCapacity(), roomDto.getFloorNumber(),
                roomDto.getCost(), roomDto.getRoomType()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(roomDtos)));
    }

    @Test
    public void testGetRoomByIdHotel() throws Exception{
        given(roomService.findAllByIdHotelRoom(roomDto.getIdHotel()))
                .willReturn(roomSetDtos);

        String endpoint ="/api/room?idHotel=%s";
        mockMvc.perform(get(String.format(endpoint, roomDto.getIdHotel()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(roomSetDtos)));
    }

    @Test
    public void testAddRoom() throws Exception{
        ObjectMapper mapper = new ObjectMapper();

        roomEntity = new RoomEntity();
        roomEntity.setIdRoom(roomDto.getIdRoom());
        roomEntity.setCost(roomDto.getCost());
        roomEntity.setCapacity(roomDto.getCapacity());
        roomEntity.setRoomType(roomDto.getRoomType());
        roomEntity.setFloorNumber(roomDto.getFloorNumber());
        roomEntity.setNumberRoom(roomDto.getNumberRoom());

        given(roomService.addRoom(any())).willReturn(roomEntity);

        mockMvc.perform(
                post("/api/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(roomEntity))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

    }

    @Test
    public void testUpdateRoom() throws Exception{

        given(roomService.updateRoom( anyInt(), any()) ).willReturn(roomDto);

        mockMvc.perform(
                put("/api/room/{id}" , 5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roomDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    public void testDeleteRoomById() throws Exception{
        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(delete("/api/room/{id}", roomDto.getIdRoom())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
