package com.example.demo.service;

import com.example.demo.dto.PaymentDto;
import com.example.demo.entity.*;
import com.example.demo.exception.ClientException;
import com.example.demo.exception.PaymentException;
import com.example.demo.repository.*;
import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private RoomRepository roomRepository;

    public PaymentService(PaymentRepository paymentRepository, ReservationRepository reservationRepository, ClientRepository clientRepository, RoomRepository roomRepository) {
        this.paymentRepository = paymentRepository;
        this.reservationRepository = reservationRepository;
        this.clientRepository = clientRepository;
        this.roomRepository = roomRepository;
    }

    public PaymentDto entityToDto(PaymentEntity paymentEntity){
        PaymentDto paymentDto = new PaymentDto();

        paymentDto.setIdPayment(paymentEntity.getIdPayment());
        paymentDto.setCurrency(paymentEntity.getCurrency());
        paymentDto.setPaymentMethod(paymentEntity.getPaymentMethod());
        paymentDto.setPaymentStatus(paymentEntity.getPaymentStatus());
        paymentDto.setAmount(paymentEntity.getAmount());
        paymentDto.setPaymentDate(paymentEntity.getPaymentDate());
        paymentDto.setIdSender(paymentEntity.getSender().getIdClient());
        paymentDto.setClientName(paymentEntity.getSender().getLastName() + ' ' + paymentEntity.getSender().getFirstName());
        paymentDto.setClientEmail(paymentEntity.getSender().getEmail());
        paymentDto.setIdReservation(paymentEntity.getReservationPayment().getIdReservation());
        paymentDto.setRoomNumber(paymentEntity.getReservationPayment().getRoomReservation().getNumberRoom());
        paymentDto.setIdHotel(paymentEntity.getReservationPayment().getRoomReservation().getHotelRoom().getIdHotel());

        return paymentDto;
    }


    public PaymentEntity dtoToEntity(PaymentDto payment){
        PaymentEntity paymentEntity = new PaymentEntity();

        paymentEntity.setPaymentStatus(payment.getPaymentStatus());
        paymentEntity.setPaymentMethod(payment.getPaymentMethod());
        paymentEntity.setPaymentDate(payment.getPaymentDate());
        paymentEntity.setCurrency(payment.getCurrency());
        paymentEntity.setAmount(payment.getAmount());
        paymentEntity.setSender(clientRepository.findByIdClient(payment.getIdSender()));
        paymentEntity.setReservationPayment(reservationRepository.findById(payment.getIdReservation()).orElse(null));

        return paymentEntity;

    }

    // ADD PAYMENT => POST
    public PaymentDto addPayment(PaymentDto payment){
        if( ! EnumUtils.isValidEnum(PaymentCurrency.class, String.valueOf(payment.getCurrency()))
                ||
                ! EnumUtils.isValidEnum(PaymentMethod.class, String.valueOf(payment.getPaymentMethod()))
                ||
                ! EnumUtils.isValidEnum(PaymentStatus.class, String.valueOf(payment.getPaymentStatus()))
            ) {
            throw PaymentException.paymentNotSaved();
        }
        if(payment.getAmount() == 0){
            throw PaymentException.paymentAmountNotValid();
        }
        ReservationEntity reservation = reservationRepository.findByIdReservation(payment.getIdReservation());
        RoomEntity room = roomRepository.findByIdRoom(reservation.getRoomReservation().getIdRoom());
        if(payment.getPaymentDate().compareTo(reservation.getEndDate()) > 0) {
            throw PaymentException.paymentDateNotValid();
        }
        if( payment.getAmount() < reservation.getPeriod()*room.getCost() && String.valueOf(payment.getPaymentStatus()).equals("EFFECTUATED")) {
            throw PaymentException.paymentStatusNotValid();
        }
        PaymentEntity paymentEntity = dtoToEntity(payment);
        return entityToDto(paymentRepository.save(paymentEntity));
    }


    //  GET ALL PAYMENTS USING DIFFERENT PARAMS
    public List<PaymentDto> getPayments(PaymentCurrency currency, PaymentMethod paymentMethod, PaymentStatus paymentStatus, Double amount, LocalDateTime paymentDate){
        List<PaymentEntity> paymentEntities =  paymentRepository.findAll().stream()
                .filter( payment -> isMatch(payment, currency, paymentMethod, paymentStatus, amount, paymentDate))
                .collect(Collectors.toList());

        if(paymentEntities.isEmpty())
        {
            throw PaymentException.paymentDoesNotExist();
        }
        return paymentEntities.stream()
                .map(paymentEntity -> entityToDto(paymentEntity))
                .collect(Collectors.toList());
    }

    private boolean isMatch(PaymentEntity payment, PaymentCurrency currency, PaymentMethod paymentMethod, PaymentStatus paymentStatus, Double amount, LocalDateTime paymentDate) {
        return (currency == null || payment.getCurrency().equals(currency)
                ) &&
                (paymentMethod == null || payment.getPaymentMethod().equals(paymentMethod)
                ) &&
                ( paymentStatus == null || payment.getPaymentStatus().equals(paymentStatus)
                ) &&
                (amount == null || payment.getAmount().equals(amount)
                ) &&
                (paymentDate == null || payment.getPaymentDate().equals(paymentDate)
                );
    }


    // GET A PAYMENT BY ID RESERVATION // ONE TO ONE
    public PaymentDto findAllByIdReservationPayment(int id){

        Optional<ReservationEntity> reservation = reservationRepository.findById(id);

        if (reservation == null ) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Hotel does not exists in the database");
        }
        PaymentEntity paymentEntity = reservation.get().getPayment();

        if(paymentEntity == null){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Reservation does not have payment");
        }

        return entityToDto(paymentEntity);
    }

    // GET A PAYMENT BY ID CLIENT
    public List<PaymentDto> findAllByIdClientPayment(int id){

        Optional<ClientEntity> client = clientRepository.findById(id);

        if (client.isEmpty()) {
            throw PaymentException.paymentClientDoesNotExist();
        }
        List<PaymentEntity> paymentEntities = client.get().getPayments();
        if (paymentEntities.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Client does not have reviews");
        }

        return paymentEntities.stream()
                .map(paymentEntity -> entityToDto(paymentEntity))
                .collect(Collectors.toList());
    }

    //  GET A PAYMENT BY ID
    public PaymentDto getPaymentById(int id) {
        PaymentEntity paymentEntity = paymentRepository.findByIdPayment(id);
        if (paymentEntity == null)
        {
            throw PaymentException.paymentDoesNotExist();
        }
        return entityToDto(paymentEntity);
    }


    //  UPDATE PAYMENT'S INFO => PUT
    public PaymentDto updatePayment(int id, PaymentDto payment){
        PaymentEntity paymentEntity = paymentRepository.findById(id).orElse(null);
        if(paymentEntity == null){
            throw PaymentException.paymentDoesNotExist();
        } else if( ! EnumUtils.isValidEnum(PaymentCurrency.class, String.valueOf(payment.getCurrency()))
                ||
                ! EnumUtils.isValidEnum(PaymentMethod.class, String.valueOf(payment.getPaymentMethod()))
                ||
                ! EnumUtils.isValidEnum(PaymentStatus.class, String.valueOf(payment.getPaymentStatus()))
                ) {
                    throw PaymentException.paymentNotSaved();
                }
        ReservationEntity reservation = reservationRepository.findByIdReservation(payment.getIdReservation());
        RoomEntity room = roomRepository.findByIdRoom(reservation.getRoomReservation().getIdRoom());

        if(payment.getPaymentDate().compareTo(reservation.getEndDate()) > 0)
        {
            throw PaymentException.paymentDateNotValid();
        }
        if( payment.getAmount() < reservation.getPeriod()*room.getCost()
                && String.valueOf(payment.getPaymentStatus()).equals("EFFECTUATED")
        )
        {
            throw PaymentException.paymentStatusNotValid();
        }
        if(payment.getAmount() == 0){
            throw PaymentException.paymentAmountNotValid();
        }

        paymentEntity.setCurrency(payment.getCurrency());
        paymentEntity.setAmount(payment.getAmount());
        paymentEntity.setPaymentDate(payment.getPaymentDate());
        paymentEntity.setPaymentMethod(payment.getPaymentMethod());
        paymentEntity.setPaymentStatus(payment.getPaymentStatus());
        return entityToDto(paymentRepository.save(paymentEntity));
    }



    //  DELETE A PAYMENT
    public void deletePayment(int id){
        PaymentEntity paymentEntity = paymentRepository.findById(id).orElse(null);
        if(paymentEntity == null){
            throw PaymentException.paymentDoesNotExist();
        }
        paymentRepository.deleteById(id);
    }

}
