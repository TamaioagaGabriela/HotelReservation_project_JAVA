package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
public class ClientDto {

    private int idClient;
    private String lastName;
    private String firstName;
    private String email;
    private String phoneNumberClient;

    private List<ReviewDto> reviewsClient;
    private List<PaymentDto> paymentsClient;
    private List<ReservationDto> reservationsClient;

    public ClientDto(){}

    public ClientDto(int idClient, String lastName, String firstName, String email, String phoneNumberClient, List<ReviewDto> reviewsClient, List<PaymentDto> paymentsClient, List<ReservationDto> reservationsClient) {
        this.idClient = idClient;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.phoneNumberClient = phoneNumberClient;
        this.reviewsClient = reviewsClient;
        this.paymentsClient = paymentsClient;
        this.reservationsClient = reservationsClient;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumberClient() {
        return phoneNumberClient;
    }

    public void setPhoneNumberClient(String phoneNumberClient) {
        this.phoneNumberClient = phoneNumberClient;
    }

    public List<ReviewDto> getReviewsClient() {
        return reviewsClient;
    }

    public void setReviewsClient(List<ReviewDto> reviewsClient) {
        this.reviewsClient = reviewsClient;
    }

    public List<PaymentDto> getPaymentsClient() {
        return paymentsClient;
    }

    public void setPaymentsClient(List<PaymentDto> paymentsClient) {
        this.paymentsClient = paymentsClient;
    }

    public List<ReservationDto> getReservationsClient() {
        return reservationsClient;
    }

    public void setReservationsClient(List<ReservationDto> reservationsClient) {
        this.reservationsClient = reservationsClient;
    }
}
