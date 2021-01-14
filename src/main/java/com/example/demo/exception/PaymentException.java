package com.example.demo.exception;

public class PaymentException extends RuntimeException{
    private PaymentException.PaymentErrors error;

    private PaymentException(PaymentException.PaymentErrors error) {
        this.error = error;
    }

    public PaymentException.PaymentErrors getError() {
        return error;
    }


    public enum PaymentErrors {
        USER_NOT_FOUND,
        BAD_CREDENTIALS,
        BAD_REQUEST,
        CURRENCY_OR_STATUS_OR_PAYMENT_METHOD_NOT_VALID,
        PAYMENT_DATE_NOT_VALID,
        PAYMENT_AMOUNT_CANNOT_BE_ZERO,
        PAYMENT_STATUS_NOT_VALID,
        PAYMENT_DOES_NOT_EXIST,
        CLIENT_DOES_NOT_EXIST
    }

    public static PaymentException paymentNotSaved() {
        return new PaymentException(PaymentException.PaymentErrors.CURRENCY_OR_STATUS_OR_PAYMENT_METHOD_NOT_VALID);
    }

    public static PaymentException paymentDateNotValid() {
        return new PaymentException(PaymentException.PaymentErrors.PAYMENT_DATE_NOT_VALID);
    }

    public static PaymentException paymentStatusNotValid() {
        return new PaymentException(PaymentException.PaymentErrors.PAYMENT_STATUS_NOT_VALID);
    }

    public static PaymentException paymentAmountNotValid() {
        return new PaymentException(PaymentException.PaymentErrors.PAYMENT_AMOUNT_CANNOT_BE_ZERO);
    }

    public static PaymentException paymentDoesNotExist() {
        return new PaymentException(PaymentException.PaymentErrors.PAYMENT_DOES_NOT_EXIST);
    }

    public static PaymentException paymentClientDoesNotExist() {
        return new PaymentException(PaymentException.PaymentErrors.CLIENT_DOES_NOT_EXIST);
    }

}
