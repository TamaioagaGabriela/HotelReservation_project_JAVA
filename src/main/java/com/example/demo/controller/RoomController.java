package com.example.demo.controller;

import com.example.demo.dto.RoomDto;
import com.example.demo.entity.RoomEntity;
import com.example.demo.entity.RoomType;
import com.example.demo.service.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/room")
public class RoomController {

    private static final Logger log = LoggerFactory.getLogger(RoomController.class);

    @Autowired
    private RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // ADD ROOM => POST
    @PostMapping
    public ResponseEntity<RoomEntity> addRoom (@Valid @RequestBody RoomDto room)
    {
        log.info("Received");
        RoomEntity roomEntity = roomService.addRoom(room);
        return new ResponseEntity<>(roomEntity, HttpStatus.CREATED);
    }


    //  GET ALL ROOMS USING DIFFERENT PARAMS
    @GetMapping
    public ResponseEntity<Iterable<RoomDto>> getRooms(
            @RequestParam(required = false) Integer numberRoom,
            @RequestParam(required = false) Integer capacity,
            @RequestParam(required = false) Integer floorNumber,
            @RequestParam(required = false) Integer cost,
            @RequestParam(required = false) RoomType roomType) {

        List<RoomDto> roomDtos = roomService.getRooms(numberRoom, capacity, floorNumber, cost, roomType);
        log.info(String.valueOf(roomDtos.size()));
        return roomDtos.isEmpty() ?
                ResponseEntity.noContent().build()
                : ResponseEntity.ok(roomDtos);
    }

    @GetMapping(params = "idHotel")
    public ResponseEntity<Iterable<RoomDto>> getRoomsByIdHotel(@RequestParam(required = false) int idHotel) {
        Set<RoomDto> roomDtos;

        if(idHotel == 0){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Hotel does not exists in the database");
        }
        else
        {log.info("Received request to get all the rooms by idHotel");
            roomDtos = roomService.findAllByIdHotelRoom(idHotel);
        }
        return new ResponseEntity<>(roomDtos, HttpStatus.OK);
    }


    //  GET A ROOM BY ID
    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> getRoom(@PathVariable int id) {
        log.info("Received request to get employee with id: {}", id);
        RoomDto roomDto = roomService.getRoomById(id);
        log.info("Created returnedEmployeeEntity with id: {}", id);
        if (roomDto == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Employee with campaign id: " + id + " not found in the database");
        }
        log.debug("before return");
        return new ResponseEntity<>(roomDto, HttpStatus.OK);
    }

    //  UPDATE ROOM'S INFO  => PUT
    @PutMapping("/{id}")
    public ResponseEntity<RoomDto> updateRoom(@PathVariable int id, @RequestBody RoomDto room){
        RoomDto roomDto = roomService.updateRoom(id, room);
        if(roomDto == null){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Employee with campaign id: " + id + " not found in the database");
        }
        return new ResponseEntity<>(roomDto, HttpStatus.OK);
    }

    //  DELETE A ROOM
    @DeleteMapping("/{id}")
    public void deleteRoom(@PathVariable int id) {
        log.info("Received request to delete employee with id: {}", id);
        roomService.deleteRoom(id);
    }
}
