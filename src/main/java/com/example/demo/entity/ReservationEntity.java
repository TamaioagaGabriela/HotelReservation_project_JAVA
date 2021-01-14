package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="reservation")
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_reservation", insertable = false, updatable = false)
    private int idReservation;

    @Column(name = "start_date")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
    @Column(name = "period")
    private Integer period;


    //@JsonIgnoreProperties("reservationsRoom")
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_room")
    private RoomEntity roomReservation;

    //@JsonIgnoreProperties("reservationsClient")
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_client")
    private ClientEntity clientReservation;

    @JsonIgnoreProperties("reservationPayment")
    @JsonIgnore
    @OneToOne(mappedBy = "reservationPayment", cascade = CascadeType.ALL, optional = true)
    private PaymentEntity payment;


    public ReservationEntity(){}

    public ReservationEntity(int idReservation, LocalDateTime startDate, LocalDateTime endDate, Integer period, RoomEntity roomReservation, ClientEntity clientReservation, PaymentEntity payment) {
        this.idReservation = idReservation;
        this.startDate = startDate;
        this.endDate = endDate;
        this.period = period;
        this.roomReservation = roomReservation;
        this.clientReservation = clientReservation;
        this.payment = payment;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public RoomEntity getRoomReservation() {
        return roomReservation;
    }

    public void setRoomReservation(RoomEntity roomReservation) {
        this.roomReservation = roomReservation;
    }

    public ClientEntity getClientReservation() {
        return clientReservation;
    }

    public void setClientReservation(ClientEntity clientReservation) {
        this.clientReservation = clientReservation;
    }

    public PaymentEntity getPayment() {
        return payment;
    }

    public void setPayment(PaymentEntity payment) {
        this.payment = payment;
    }
}
