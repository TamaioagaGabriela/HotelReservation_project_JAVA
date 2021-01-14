package com.example.demo.controller;

import com.example.demo.dto.PaymentDto;
import com.example.demo.entity.*;
import com.example.demo.exception.PaymentException;
import com.example.demo.service.PaymentService;
import org.apache.commons.lang3.EnumUtils;
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
@RequestMapping("/api/payment")
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // ADD PAYMENT => POST
    @PostMapping
    public ResponseEntity<PaymentDto> addPayment (@Valid @RequestBody PaymentDto payment)
    {

        PaymentDto paymentDto = paymentService.addPayment(payment);
        return new ResponseEntity<>(paymentDto, HttpStatus.CREATED);
    }


    //  GET ALL PAYMENTS USING DIFFERENT PARAMS
    @GetMapping
    public ResponseEntity<Iterable<PaymentDto>> getPayments(
            @RequestParam(required = false) PaymentCurrency currency,
            @RequestParam(required = false) PaymentMethod paymentMethod,
            @RequestParam(required = false) PaymentStatus paymentStatus,
            @RequestParam(required = false) Double amount,
            @RequestParam(required = false) LocalDateTime paymentDate) {

        List<PaymentDto> paymentDtos = paymentService.getPayments(currency, paymentMethod, paymentStatus, amount, paymentDate);
        return paymentDtos.isEmpty() ?
                ResponseEntity.noContent().build()
                : ResponseEntity.ok(paymentDtos);
    }

    //  GET PAYMENT BY ID RESERVATION
    @GetMapping(params = "idReservation")
    public ResponseEntity<PaymentDto> getPaymentByIdReservation(@RequestParam(required = false) int idReservation) {
        PaymentDto paymentDto;

        if(idReservation == 0){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Reservation does not exists in the database");
        }
        else
        {log.info("Received request to get all the payments by idReservation", idReservation);
            paymentDto = paymentService.findAllByIdReservationPayment(idReservation);
        }
        return new ResponseEntity<>(paymentDto, HttpStatus.OK);
    }

    // GET BY ID CLIENT
    @GetMapping(params = "idClient") // idSender
    public ResponseEntity<Iterable<PaymentDto>> getPaymentsByIdClient(@RequestParam(required = false) int idClient) {
        List<PaymentDto> paymentDtos;
        if(idClient == 0){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Client does not exists in the database");
        }
        else
        {log.info("Received request to get all the payments by idClient", idClient);
            paymentDtos = paymentService.findAllByIdClientPayment(idClient);
        }
        return new ResponseEntity<>(paymentDtos, HttpStatus.OK);
    }

    //  GET A PAYMENT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getPayment(@PathVariable int id) {
        log.info("Received request to get employee with id: {}", id);
        PaymentDto paymentDto = paymentService.getPaymentById(id);
        log.info("Created returnedEmployeeEntity with id: {}", id);
        if (paymentDto == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Employee with campaign id: " + id + " not found in the database");
        }
        log.debug("before return");
        return new ResponseEntity<>(paymentDto, HttpStatus.OK);
    }

    //  UPDATE PAYMENT'S INFO => PUT
    @PutMapping("/{id}")
    public ResponseEntity<PaymentDto> updatePayment(@PathVariable int id, @RequestBody PaymentDto payment){
        PaymentDto paymentDto = paymentService.updatePayment(id, payment);
        if(paymentDto == null){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Payment with id: " + id + " not found in the database");
        }

        return new ResponseEntity<>(paymentDto, HttpStatus.OK);
    }

    //  DELETE A PAYMENT
    @DeleteMapping("/{id}")
    public void deletePayment(@PathVariable int id) {
        log.info("Received request to delete employee with id: {}", id);
        paymentService.deletePayment(id);
    }


}
