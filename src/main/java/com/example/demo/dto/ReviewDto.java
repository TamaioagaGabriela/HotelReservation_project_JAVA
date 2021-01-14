package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Builder
@Component
public class ReviewDto {

    private int idReview;
    private String subject;
    private String description;
    @NotNull
    private Integer rate;
    @NotNull
    private int idClientReview;
    private String clientLastName;
    private String clientFirstName;
    @NotNull
    private int idHotelReview;
    private String hotelCity;
    private String hotelAddress;

    public ReviewDto(){};

    public ReviewDto(int idReview, String subject, String description, @NotNull Integer rate, @NotNull int idClientReview, String clientLastName, String clientFirstName, @NotNull int idHotelReview, String hotelCity, String hotelAddress) {
        this.idReview = idReview;
        this.subject = subject;
        this.description = description;
        this.rate = rate;
        this.idClientReview = idClientReview;
        this.clientLastName = clientLastName;
        this.clientFirstName = clientFirstName;
        this.idHotelReview = idHotelReview;
        this.hotelCity = hotelCity;
        this.hotelAddress = hotelAddress;
    }

    public ReviewDto(String subject, String description, @NotNull Integer rate, @NotNull int idClientReview, String clientLastName, String clientFirstName, @NotNull int idHotelReview, String hotelCity, String hotelAddress) {
        this.subject = subject;
        this.description = description;
        this.rate = rate;
        this.idClientReview = idClientReview;
        this.clientLastName = clientLastName;
        this.clientFirstName = clientFirstName;
        this.idHotelReview = idHotelReview;
        this.hotelCity = hotelCity;
        this.hotelAddress = hotelAddress;
    }

    public int getIdReview() {
        return idReview;
    }

    public void setIdReview(int idReview) {
        this.idReview = idReview;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public int getIdClientReview() {
        return idClientReview;
    }

    public void setIdClientReview(int idClientReview) {
        this.idClientReview = idClientReview;
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

    public int getIdHotelReview() {
        return idHotelReview;
    }

    public void setIdHotelReview(int idHotelReview) {
        this.idHotelReview = idHotelReview;
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
}
