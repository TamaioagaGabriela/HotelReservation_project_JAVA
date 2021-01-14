package com.example.demo.dto;

import com.example.demo.entity.PaymentCurrency;
import com.example.demo.entity.PaymentMethod;
import com.example.demo.entity.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
public class PaymentDto {
    @NotNull
    private int idPayment;
    private PaymentCurrency currency;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private Double amount;
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentDate;
    // chei externe
    @NotNull
    private int idSender;
    private String clientName;
    private String clientEmail;
    @NotNull
    private int idReservation;
    private Integer roomNumber;
    private int idHotel;

    public PaymentDto(){}

    public PaymentDto(@NotNull int idPayment, PaymentCurrency currency, PaymentMethod paymentMethod, PaymentStatus paymentStatus, Double amount, LocalDateTime paymentDate, @NotNull int idSender, String clientName, String clientEmail, @NotNull int idReservation, Integer roomNumber, int idHotel) {
        this.idPayment = idPayment;
        this.currency = currency;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.idSender = idSender;
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.idReservation = idReservation;
        this.roomNumber = roomNumber;
        this.idHotel = idHotel;
    }

    public int getIdPayment() {
        return idPayment;
    }

    public void setIdPayment(int idPayment) {
        this.idPayment = idPayment;
    }

    public PaymentCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(PaymentCurrency currency) {
        this.currency = currency;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public int getIdSender() {
        return idSender;
    }

    public void setIdSender(int idSender) {
        this.idSender = idSender;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(int idHotel) {
        this.idHotel = idHotel;
    }
}
