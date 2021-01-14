package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Builder
public class HotelDto {

    @NotNull
    private int idHotel;
    private String address;
    private String city;
    private String phoneNumber;
    private Integer maxNumberRooms;
    private Set<RoomDto> rooms;
    private List<ReviewDto> reviews;

    public HotelDto(){}

    public HotelDto(@NotNull int idHotel, String address, String city, String phoneNumber, Integer maxNumberRooms, Set<RoomDto> rooms, List<ReviewDto> reviews) {
        this.idHotel = idHotel;
        this.address = address;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.maxNumberRooms = maxNumberRooms;
        this.rooms = rooms;
        this.reviews = reviews;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getMaxNumberRooms() {
        return maxNumberRooms;
    }

    public void setMaxNumberRooms(Integer maxNumberRooms) {
        this.maxNumberRooms = maxNumberRooms;
    }

    public Set<RoomDto> getRooms() {
        return rooms;
    }

    public void setRooms(Set<RoomDto> rooms) {
        this.rooms = rooms;
    }

    public List<ReviewDto> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewDto> reviews) {
        this.reviews = reviews;
    }
}
