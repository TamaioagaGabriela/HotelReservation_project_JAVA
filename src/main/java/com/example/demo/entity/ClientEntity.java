package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="client")
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_client", insertable = false, updatable = false)
    private int idClient;

    //@NotBlank(message = "Lastname of the client is mandatory")
    @Column(name = "last_name")
    private String lastName;
    //@NotBlank(message = "Firstname of the client is mandatory")
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "email")
    private String email;
    @Column(name = "phone_number")
    private String phoneNumberClient;

    @JsonIgnoreProperties("clientReview")
    @OneToMany(orphanRemoval = true, mappedBy = "clientReview")
    private List<ReviewEntity> reviews;

    @JsonIgnoreProperties("sender")
    @OneToMany(orphanRemoval = true, mappedBy = "sender")
    private List<PaymentEntity> payments;


    @JsonIgnoreProperties("clientReservation")
    @OneToMany(orphanRemoval = true, mappedBy = "clientReservation")
    private List<ReservationEntity> reservationsClient;



    public ClientEntity(){}

    public ClientEntity(int idClient, String lastName, String firstName, String email, String phoneNumberClient, List<ReviewEntity> reviews) {
        this.idClient = idClient;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.phoneNumberClient = phoneNumberClient;
        this.reviews = reviews;
    }

    public ClientEntity(int idClient, String lastName, String firstName, String email, String phoneNumberClient, List<ReviewEntity> reviews, List<PaymentEntity> payments, List<ReservationEntity> reservationsClient) {
        this.idClient = idClient;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.phoneNumberClient = phoneNumberClient;
        this.reviews = reviews;
        this.payments = payments;
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

    public List<ReviewEntity> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewEntity> reviews) {
        this.reviews = reviews;
    }

    public List<PaymentEntity> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentEntity> payments) {
        this.payments = payments;
    }

    public List<ReservationEntity> getReservationsClient() {
        return reservationsClient;
    }

    public void setReservationsClient(List<ReservationEntity> reservationsClient) {
        this.reservationsClient = reservationsClient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientEntity)) return false;
        ClientEntity that = (ClientEntity) o;
        return getIdClient() == that.getIdClient() &&
                getLastName().equals(that.getLastName()) &&
                getFirstName().equals(that.getFirstName()) &&
                getEmail().equals(that.getEmail()) &&
                getPhoneNumberClient().equals(that.getPhoneNumberClient());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdClient(), getLastName(), getFirstName(), getEmail(), getPhoneNumberClient());
    }

    @Override
    public String toString() {
        return "ClientEntity{" +
                "idClient=" + idClient +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumberClient='" + phoneNumberClient + '\'' +
                '}';
    }
}
