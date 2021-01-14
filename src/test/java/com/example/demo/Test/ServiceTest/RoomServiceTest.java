package com.example.demo.Test.ServiceTest;

import com.example.demo.dto.HotelDto;
import com.example.demo.dto.ReservationDto;
import com.example.demo.dto.RoomDto;
import com.example.demo.entity.RoomEntity;
import com.example.demo.entity.RoomType;
import com.example.demo.exception.ReviewException;
import com.example.demo.exception.RoomException;
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
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @Mock
    private ReservationService reservationServiceMock;

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
    private RoomService roomService;



    private static RoomDto roomDto;
    private static RoomDto roomDtoSameNumber;
    private static HotelDto hotelDto;
    private static ReservationDto reservationDto;

    private static List<ReservationDto> reservationDtos;
    private static List<RoomDto> roomDtos;
    private static Set<RoomDto> roomSetDtos;

    private static RoomEntity roomEntity;
    private static List<RoomEntity> roomEntities;

    @BeforeAll
    public static void setup() {

        reservationDto = new ReservationDto(13, 21, "Ionel",
                "Pop", 5, RoomType.SUITE, 19, 500, "Iasi",
                "Strada Victoriei 54", LocalDateTime.of(2021,2, 11,15,0,0),
                LocalDateTime.of(2021,2, 13,11,30,0), 2, 1000 );
        reservationDtos = new ArrayList<>();
        reservationDtos.add(reservationDto);

        roomDto = new RoomDto(5, 19, 4, 2, 500,
                RoomType.SUITE, 8, "Strada Victoriei 54" , reservationDtos);
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
    @DisplayName("Test room number already exists")
    public void testRoomNumberAlreadyExist(){
        roomEntity = roomService.dtoToEntity(roomDto);
        roomEntity.setIdRoom(roomDto.getIdRoom());
        Mockito.lenient().when(roomRepository.findByNumberRoom(roomDto.getNumberRoom())).thenReturn(Optional.ofNullable(roomEntity));
        RoomException exception = assertThrows(RoomException.class, () ->
                roomService.addRoom(roomDtoSameNumber));
        assertEquals(RoomException.RoomErrors.ROOM_WITH_SAME_NUMBER_ROOM_ALREADY_EXISTS, exception.getError());
    }

    @Test
    @DisplayName("Test rooms not found")
    public void testGetRooms(){
        roomEntities = roomDtos.stream()
                .map(roomDto -> roomService.dtoToEntity(roomDto))
                .collect(Collectors.toList());
        Mockito.lenient().when(roomRepository.findAll()).thenReturn((List<RoomEntity>) roomEntities);
        RoomException exception = assertThrows(RoomException.class, () ->
                roomService.getRooms(roomDto.getNumberRoom(), 6, roomDto.getFloorNumber(),
                        roomDto.getCost(), roomDto.getRoomType()));
        assertEquals(RoomException.RoomErrors.ROOM_DOES_NOT_EXIST, exception.getError());
    }

    @Test
    @DisplayName("Test room with the given id not found")
    public void testGetRoomById(){
        roomEntity = roomService.dtoToEntity(roomDto);
        roomEntity.setIdRoom(roomDto.getIdRoom());
        Mockito.lenient().when(roomRepository.findByIdRoom(roomDto.getIdRoom())).thenReturn(roomEntity);
        RoomException exception = assertThrows(RoomException.class, () ->
                roomService.getRoomById(10));
        assertEquals(RoomException.RoomErrors.ROOM_DOES_NOT_EXIST, exception.getError());
    }

    @Test
    @DisplayName("Test room update id not found")
    public void testUpdateRoomIdNotFound(){

        roomEntity = roomService.dtoToEntity(roomDto);
        roomEntity.setIdRoom(roomDto.getIdRoom());
        Mockito.lenient().when(roomRepository.findById(roomDto.getIdRoom())).thenReturn(java.util.Optional.ofNullable(roomEntity));
        RoomException exception = assertThrows(RoomException.class, () ->
                roomService.updateRoom(10, roomDto));
        assertEquals(RoomException.RoomErrors.ROOM_DOES_NOT_EXIST, exception.getError());
    }

    @Test
    @DisplayName("Test room not deleted")
    public void testDeleteRoom(){
        roomEntity = roomService.dtoToEntity(roomDto);
        roomEntity.setIdRoom(roomDto.getIdRoom());
        Mockito.lenient().when(roomRepository.findById(roomDto.getIdRoom())).thenReturn(java.util.Optional.ofNullable(roomEntity));
        RoomException exception = assertThrows(RoomException.class, () ->
                roomService.deleteRoom(10));
        assertEquals(RoomException.RoomErrors.ROOM_DOES_NOT_EXIST, exception.getError());
    }

}
