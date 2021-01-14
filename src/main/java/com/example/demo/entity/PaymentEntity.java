package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name="payment")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_payment", insertable = false, updatable = false)
    private int idPayment;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private PaymentCurrency currency;
    @Column(name = "payment_mode")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    @Column(name = "amount")
    private Double amount;
    @Column(name = "payment_date")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentDate;

    // chei externe
    @JsonIgnoreProperties("payments")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_sender") // ce e in paranteza e numele coloanei din tabelul de aici
    private ClientEntity sender;

    @JsonIgnoreProperties("payment")
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_reservation")
    private ReservationEntity reservationPayment;



    public PaymentEntity(){}

    public PaymentEntity(int idPayment, PaymentCurrency currency, PaymentMethod paymentMethod, PaymentStatus paymentStatus, Double amount, LocalDateTime paymentDate, ClientEntity idSender, ReservationEntity reservationPayment) {
        this.idPayment = idPayment;
        this.currency = currency;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.sender = idSender;
        this.reservationPayment = reservationPayment;
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

    public ClientEntity getSender() {
        return sender;
    }

    public void setSender(ClientEntity sender) {
        this.sender = sender;
    }

    public ReservationEntity getReservationPayment() {
        return reservationPayment;
    }

    public void setReservationPayment(ReservationEntity reservationPayment) {
        this.reservationPayment = reservationPayment;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return "PaymentEntity{" +
                "idPayment=" + idPayment +
                ", currency=" + currency +
                ", paymentMethod=" + paymentMethod +
                ", amount=" + amount +
                ", paymentDate=" + paymentDate +
                ", idSender=" + sender +
                ", reservationPayment=" + reservationPayment +
                '}';
    }
}
