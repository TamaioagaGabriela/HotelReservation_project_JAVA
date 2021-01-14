package com.example.demo.controller;

import com.example.demo.dto.HotelDto;
import com.example.demo.entity.HotelEntity;
import com.example.demo.service.HotelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/hotel")
public class HotelController {


    private static final Logger log = LoggerFactory.getLogger(HotelController.class);

    @Autowired
    private HotelService hotelService;

    // constructor
    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    // POST -- merge
    @PostMapping()
    public ResponseEntity<HotelEntity> addHotel(@Valid @RequestBody HotelDto hotel) {
        log.info("Received request to create a hotel: {}", hotel);
        HotelEntity savedHotelEntity = hotelService.addHotel(hotel);
        return new ResponseEntity<>(savedHotelEntity, HttpStatus.CREATED);
    }

    //  GET ALL RESERVATIONS USING DIFFERENT PARAMS
    @GetMapping
    public ResponseEntity<Iterable<HotelDto>> getHotels(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) Integer totalRoomNumbers) {
        log.info("Received request to get all the hotels");
        List<HotelDto> hotelDtos = hotelService.getHotels(city, address, phoneNumber, totalRoomNumbers);
        return hotelDtos.isEmpty() ?
                ResponseEntity.noContent().build()
                : ResponseEntity.ok(hotelDtos);
    }

    // GET
    @GetMapping("/{id}")
    public ResponseEntity<HotelDto> getHotel(@PathVariable int id) {
        log.info("Received request to get hotel with id: {}", id);
        HotelDto hotelDto = hotelService.findById(id);
        log.info("Created returnedHotelEntity with id: {}", id);
        if (hotelDto == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Hotel with the id: " + id + " not found in the database");
        }
        log.debug("before return");
        return new ResponseEntity<>(hotelDto, HttpStatus.OK);
    }

    //  UPDATE HOTEL'S INFO => PUT
    @PutMapping("/{id}")
    public ResponseEntity<HotelDto> updateHotel(@PathVariable int id, @RequestBody HotelDto hotel){
        HotelDto hotelDto = hotelService.updateHotel(id, hotel);
        if(hotelDto == null){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Hotel with id : " + id + " not found in the database");
        }
        return new ResponseEntity<>(hotelDto, HttpStatus.OK);
    }

    //  DELETE A HOTEL
    @DeleteMapping("/{id}")
    public void hotelRoom(@PathVariable int id) {
        log.info("Received request to delete employee with id: {}", id);
        hotelService.deleteHotel(id);
    }
}
