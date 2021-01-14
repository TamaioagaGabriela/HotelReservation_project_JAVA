package com.example.demo.dto;

import com.example.demo.entity.RoomType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
public class ReservationDto {

    @NotNull
    private int idReservation;
    @NotNull
    private int idClientReservation;
    private String clientLastName;
    private String clientFirstName;
    @NotNull
    private int idRoomReservation;
    private RoomType roomType;
    private Integer roomNumber;
    private Integer cost;
    private String hotelCity;
    private String hotelAddress;
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
    private Integer period;
    private Integer costTotal;


    public ReservationDto(){}

    public ReservationDto(@NotNull int idReservation, @NotNull int idClientReservation, String clientLastName, String clientFirstName, @NotNull int idRoomReservation, RoomType roomType, Integer roomNumber, Integer cost, String hotelCity, String hotelAddress, LocalDateTime startDate, LocalDateTime endDate, Integer period, Integer costTotal) {
        this.idReservation = idReservation;
        this.idClientReservation = idClientReservation;
        this.clientLastName = clientLastName;
        this.clientFirstName = clientFirstName;
        this.idRoomReservation = idRoomReservation;
        this.roomType = roomType;
        this.roomNumber = roomNumber;
        this.cost = cost;
        this.hotelCity = hotelCity;
        this.hotelAddress = hotelAddress;
        this.startDate = startDate;
        this.endDate = endDate;
        this.period = period;
        this.costTotal = costTotal;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public String getHotelCity() {
        return hotelCity;
    }

    public void setHotelCity(String hotelCity) {
        this.hotelCity = hotelCity;
    }

    public String getHotelAddress() {
        return hotelAddress;
    }

    public void setHotelAddress(String hotelAddress) {
        this.hotelAddress = hotelAddress;
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

    public int getIdRoomReservation() {
        return idRoomReservation;
    }

    public void setIdRoomReservation(int idRoomReservation) {
        this.idRoomReservation = idRoomReservation;
    }

    public int getIdClientReservation() {
        return idClientReservation;
    }

    public void setIdClientReservation(int idClientReservation) {
        this.idClientReservation = idClientReservation;
    }

    public String getClientLastName() {
        return clientLastName;
    }

    public void setClientLastName(String clientLastName) {
        this.clientLastName = clientLastName;
    }

    public String getClientFirstName() {
        return clientFirstName;
    }

    public void setClientFirstName(String clientFirstName) {
        this.clientFirstName = clientFirstName;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getCostTotal() {
        return costTotal;
    }

    public void setCostTotal(Integer costTotal) {
        this.costTotal = costTotal;
    }


}
