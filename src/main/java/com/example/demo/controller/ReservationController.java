package com.example.demo.controller;

import com.example.demo.dto.ReservationDto;
import com.example.demo.entity.ReservationEntity;
import com.example.demo.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    private static final Logger log = LoggerFactory.getLogger(ReservationController.class);

    @Autowired
    private ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }


    // ADD RESERVATION => POST
    @PostMapping
    public ResponseEntity<ReservationEntity> addReservation (@Valid @RequestBody ReservationDto reservation)
    {
        log.info("Received");
        ReservationEntity reservationEntity = reservationService.addReservation(reservation);
        return new ResponseEntity<>(reservationEntity, HttpStatus.CREATED);
    }


    //  GET ALL RESERVATIONS USING DIFFERENT PARAMS
    @GetMapping
    public ResponseEntity<Iterable<ReservationDto>> getReservations(
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) Integer period) {
        log.info("Received request to get all the reservations");
        log.info(String.valueOf(reservationService.getReservations(startDate, endDate, period)));
        List<ReservationDto> reservationDtos = reservationService.getReservations(startDate, endDate, period);
        log.info(String.valueOf(reservationDtos.size()));
        return reservationDtos.isEmpty() ?
                ResponseEntity.noContent().build()
                : ResponseEntity.ok(reservationDtos);
    }


    //  GET RESERVATION BY ID ROOM
    @GetMapping(params = "idRoom")
    public ResponseEntity<Iterable<ReservationDto>> getReservationsByIdHotel(@RequestParam(required = false) int idRoom) {
        List<ReservationDto> reservationDtos;

        if(idRoom == 0){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Hotel does not exists in the database");
        }
        else
        {log.info("Received request to get all the reservation by idRoom", idRoom);
            reservationDtos = reservationService.findAllByIdRoomReservation(idRoom);
        }
        return new ResponseEntity<>(reservationDtos, HttpStatus.OK);
    }

    //  GET RESERVATION BY ID CLIENT
    @GetMapping(params = "idClient")
    public ResponseEntity<Iterable<ReservationDto>> getReservationsByIdClient(@RequestParam(required = false) int idClient) {
        List<ReservationDto> reservationDtos;
        if(idClient == 0){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Client does not exists in the database");
        }
        else
        {log.info("Received request to get all the reservations by idClient", idClient);
            reservationDtos = reservationService.findAllByIdClientReservation(idClient);
            if (reservationDtos.isEmpty()){
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Client does not have reservations");
            }
        }
        return new ResponseEntity<>(reservationDtos, HttpStatus.OK);
    }

    //  GET A RESERVATION BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> getReservation(@PathVariable int id) {
        log.info("Received request to get reservation with id: {}", id);
        ReservationDto reservationDto = reservationService.getReservationById(id);
        log.info("Created returnedEmployeeEntity with id: {}", id);
        if (reservationDto == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Reservation does not exist");
        }
        log.debug("before return");
        return new ResponseEntity<>(reservationDto, HttpStatus.OK);
    }

    //  UPDATE RESERVATION'S INFO => PUT
    @PutMapping("/{id}")
    public ResponseEntity<ReservationDto> updateReservation(@PathVariable int id, @RequestBody ReservationDto reservation){
        ReservationDto reservationDto = reservationService.updateReservation(id, reservation);
        if(reservationDto == null){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Reservation not found in the database");
        }
        return new ResponseEntity<>(reservationDto, HttpStatus.OK);
    }

    //  DELETE A RESERVATION
    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable int id) {
        log.info("Received request to delete reservation with id: {}", id);
        reservationService.deleteReservation(id);
    }
}
