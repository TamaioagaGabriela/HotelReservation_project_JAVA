package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="review")
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_review", insertable = false, updatable = false)
    private int idReview;

    @Column(name = "subject")
    private String subject;
    @Column(name = "rate")
    private Integer rate;
    @Column(name = "description")
    private String description;

    @JsonIgnoreProperties("reviews")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_client")
    private ClientEntity clientReview;

    @JsonIgnoreProperties("reviews")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_hotel")
    private HotelEntity hotelReview;


    public ReviewEntity(){}

    public ReviewEntity(int idReview, String subject, Integer rate, String description, ClientEntity clientReview, HotelEntity hotelReview) {
        this.idReview = idReview;
        this.subject = subject;
        this.rate = rate;
        this.description = description;
        this.clientReview = clientReview;
        this.hotelReview = hotelReview;
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

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ClientEntity getClientReview() {
        return clientReview;
    }

    public void setClientReview(ClientEntity clientReview) {
        this.clientReview = clientReview;
    }

    public HotelEntity getHotelReview() {
        return hotelReview;
    }

    public void setHotelReview(HotelEntity hotelReview) {
        this.hotelReview = hotelReview;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReviewEntity)) return false;
        ReviewEntity that = (ReviewEntity) o;
        return getIdReview() == that.getIdReview() &&
                getSubject().equals(that.getSubject()) &&
                getRate().equals(that.getRate()) &&
                getDescription().equals(that.getDescription()) &&
                getClientReview().equals(that.getClientReview()) &&
                getHotelReview().equals(that.getHotelReview());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdReview(), getSubject(), getRate(), getDescription(), getClientReview(), getHotelReview());
    }

    @Override
    public String toString() {
        return "ReviewEntity{" +
                "idReview=" + idReview +
                ", subject='" + subject + '\'' +
                ", rate=" + rate +
                ", description='" + description + '\'' +
                ", clientReview=" + clientReview +
                ", hotelReview=" + hotelReview +
                '}';
    }
}
