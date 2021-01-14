package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="hotel")
public class HotelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_hotel", insertable = false, updatable = false)
    private int idHotel;

    @Column(name = "address")
    private String address;
    @Column(name = "city")
    private String city;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "total_number_rooms")
    private Integer maxNumberRooms;

    //@JsonManagedReference
    @JsonIgnoreProperties("hotelRoom")
    @OneToMany(orphanRemoval = true, mappedBy = "hotelRoom")
    private Set<RoomEntity> rooms;

    @JsonIgnoreProperties("hotelReview")
    @OneToMany(orphanRemoval = true, mappedBy = "hotelReview")
    private List<ReviewEntity> reviews;



    public HotelEntity() { }

    public HotelEntity(int idHotel, String address, String city, String phoneNumber, Integer totalRoomNumbers){
        this.idHotel = idHotel;
        this.address = address;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.maxNumberRooms = totalRoomNumbers;
    }

    public HotelEntity(int idHotel, String address, String city, String phoneNumber, Integer totalRoomNumbers, Set<RoomEntity> rooms, List<ReviewEntity> reviews) {
        this.idHotel = idHotel;
        this.address = address;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.maxNumberRooms = totalRoomNumbers;
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

    public Set<RoomEntity> getRooms() {
        return rooms;
    }

    public void setRooms(Set<RoomEntity> rooms) {
        this.rooms = rooms;
    }

    public List<ReviewEntity> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewEntity> reviews) {
        this.reviews = reviews;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HotelEntity)) return false;
        HotelEntity that = (HotelEntity) o;
        return getIdHotel() == that.getIdHotel() &&
                getAddress().equals(that.getAddress()) &&
                getCity().equals(that.getCity()) &&
                getPhoneNumber().equals(that.getPhoneNumber()) &&
                getMaxNumberRooms().equals(that.getMaxNumberRooms());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdHotel(), getAddress(), getCity(), getPhoneNumber(), getMaxNumberRooms());
    }

    @Override
    public String toString() {
        return "HotelEntity{" +
                "idHotel=" + idHotel +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", maxNumberRooms=" + maxNumberRooms +
                '}';
    }
}
