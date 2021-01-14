package com.example.demo.dto;

import com.example.demo.entity.RoomType;
import lombok.Builder;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
public class RoomDto {

    @NotNull
    private int idRoom;
    private Integer numberRoom;
    private Integer capacity;
    private Integer floorNumber;
    private Integer cost;
    private RoomType roomType;
    //cheie externa
    @NotNull
    private int idHotel;
    private String address;
    private List<ReservationDto> reservations;


    public RoomDto(){}

    public RoomDto(@NotNull int idRoom, Integer numberRoom, Integer capacity, Integer floorNumber, Integer cost, RoomType roomType, @NotNull int idHotel, String address, List<ReservationDto> reservations) {
        this.idRoom = idRoom;
        this.numberRoom = numberRoom;
        this.capacity = capacity;
        this.floorNumber = floorNumber;
        this.cost = cost;
        this.roomType = roomType;
        this.idHotel = idHotel;
        this.address = address;
        this.reservations = reservations;
    }

    public int getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(int idRoom) {
        this.idRoom = idRoom;
    }

    public Integer getNumberRoom() {
        return numberRoom;
    }

    public void setNumberRoom(Integer numberRoom) {
        this.numberRoom = numberRoom;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public int getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(int idHotel) {
        this.idHotel = idHotel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<ReservationDto> getReservations() {
        return reservations;
    }

    public void setReservations(List<ReservationDto> reservations) {
        this.reservations = reservations;
    }
}
