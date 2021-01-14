package com.example.demo.exception;

public class ReservationException extends RuntimeException{

    private ReservationException.ReservationErrors error;

    private ReservationException(ReservationException.ReservationErrors error) {
        this.error = error;
    }

    public ReservationException.ReservationErrors getError() {
        return error;
    }


    public enum ReservationErrors {
        NOT_FOUND,
        BAD_CREDENTIALS,
        BAD_REQUEST,
        START_DATE_OR_END_DATE_NOT_VALID,
        RESERVATION_DOES_NOT_EXIST,
        CLIENT_NOT_FOUND,
        CLIENT_DOES_NOT_EXIST,
        RESERVATION_ROOMTYPE_NOT_AVAILABLE,
        ROOM_WITH_SAME_NUMBER_ROOM_ALREADY_EXISTS,
        ROOM_COULD_NOT_BE_UPDATED,
        ROOM_COULD_NOT_BE_SAVED,
        MAXIMUM_NUMBER_OF_ROOMS_IS_REACHED,
        PAYMENT_COULD_NOT_BE_PROCESSED,
        CLIENT_WITH_SAME_EMAIL_ALREADY_EXISTS,
        CLIENT_WITH_NO_LASTNAME_OR_FIRSTNAME,
        ROOM_DOES_NOT_EXIST
    }

    public static ReservationException reservationNotSaved() {
        return new ReservationException(ReservationException.ReservationErrors.RESERVATION_ROOMTYPE_NOT_AVAILABLE);
    }

    public static ReservationException reservationDoesNotExist() {
        return new ReservationException(ReservationException.ReservationErrors.RESERVATION_DOES_NOT_EXIST);
    }

    public static ReservationException reservationClientDoesNotExist() {
        return new ReservationException(ReservationException.ReservationErrors.CLIENT_DOES_NOT_EXIST);
    }

    public static ReservationException reservationRoomDoesNotExist() {
        return new ReservationException(ReservationException.ReservationErrors.ROOM_DOES_NOT_EXIST);
    }

    public static ReservationException invalidCheckInCheckOut() {
        return new ReservationException(ReservationException.ReservationErrors.START_DATE_OR_END_DATE_NOT_VALID);
    }
}
