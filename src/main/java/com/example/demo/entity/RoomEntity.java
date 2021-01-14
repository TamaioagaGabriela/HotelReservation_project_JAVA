package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="room")
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_room", insertable = false, updatable = false)
    private int idRoom;

    @Column(name = "number_room")
    private Integer numberRoom;
    @Column(name = "capacity")
    private Integer capacity;
    @Column(name = "floor_number")
    private Integer floorNumber;
    @Column(name = "cost")
    private Integer cost;
    @Column(name = "room_type")
    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    // chei externe
    //@JsonBackReference
    @JsonIgnoreProperties("rooms")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_hotel")
    private HotelEntity hotelRoom;

    @JsonIgnoreProperties("roomReservation")
    @OneToMany(orphanRemoval = true, mappedBy = "roomReservation")
    private List<ReservationEntity> reservationsRoom;



    public RoomEntity(){}

    public RoomEntity(int idRoom, Integer numberRoom, Integer capacity, Integer floorNumber, Integer cost, RoomType roomType, HotelEntity idHotelRoom) {
        this.numberRoom = numberRoom;
        this.idRoom = idRoom;
        this.capacity = capacity;
        this.floorNumber = floorNumber;
        this.cost = cost;
        this.roomType = roomType;
    }

    public RoomEntity(int idRoom, Integer numberRoom, Integer capacity, Integer floorNumber, Integer cost, RoomType roomType, HotelEntity idHotelRoom, List<ReservationEntity> reservationsRoom) {
        this.idRoom = idRoom;
        this.numberRoom = numberRoom;
        this.capacity = capacity;
        this.floorNumber = floorNumber;
        this.cost = cost;
        this.roomType = roomType;
        this.hotelRoom = idHotelRoom;
        this.reservationsRoom = reservationsRoom;
    }

    public Integer getNumberRoom() {
        return numberRoom;
    }

    public void setNumberRoom(Integer numberRoom) {
        this.numberRoom = numberRoom;
    }

    public int getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(int idRoom) {
        this.idRoom = idRoom;
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

    public HotelEntity getHotelRoom() {
        return hotelRoom;
    }

    public void setHotelRoom(HotelEntity hotelRoom) {
        this.hotelRoom = hotelRoom;
    }

    public List<ReservationEntity> getReservationsRoom() {
        return reservationsRoom;
    }

    public void setReservationsRoom(List<ReservationEntity> reservationsRoom) {
        this.reservationsRoom = reservationsRoom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoomEntity)) return false;
        RoomEntity that = (RoomEntity) o;
        return getIdRoom() == that.getIdRoom() &&
                getNumberRoom().equals(that.getNumberRoom()) &&
                getCapacity().equals(that.getCapacity()) &&
                getFloorNumber().equals(that.getFloorNumber()) &&
                getCost().equals(that.getCost()) &&
                getRoomType() == that.getRoomType() &&
                getHotelRoom().equals(that.getHotelRoom()) &&
                getReservationsRoom().equals(that.getReservationsRoom());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdRoom(), getNumberRoom(), getCapacity(), getFloorNumber(), getCost(), getRoomType(), getHotelRoom(), getReservationsRoom());
    }

    @Override
    public String toString() {
        return "RoomEntity{" +
                "idRoom=" + idRoom +
                ", numberRoom=" + numberRoom +
                ", capacity=" + capacity +
                ", floorNumber=" + floorNumber +
                ", cost=" + cost +
                ", roomType=" + roomType +
                ", idHotelRoom=" + hotelRoom +
                ", reservationsRoom=" + reservationsRoom +
                '}';
    }
}
