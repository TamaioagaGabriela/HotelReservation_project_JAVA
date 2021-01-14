package com.example.demo.exception;

public class HotelException extends RuntimeException{

    private HotelException.HotelErrors error;

    private HotelException(HotelException.HotelErrors error) {
        this.error = error;
    }

    public HotelException.HotelErrors getError() {
        return error;
    }


    public enum HotelErrors {
        USER_NOT_FOUND,
        BAD_CREDENTIALS,
        BAD_REQUEST,
        USER_HAS_NO_ACCOUNT_FOR_CURRENCY,
        ACCOUNT_HAS_NOT_ENOUGH_AMOUNT_FOR_PAYMENT,
        PAYMENT_COULD_NOT_BE_PROCESSED,
        HOTEL_WITH_SAME_ADDRESS_ALREADY_EXISTS,
        HOTEL_DOES_NOT_EXIST,
        HOTEL_MAX_NUMBER_ROOMS_TOO_SMALL,
        USER_COULD_NOT_BE_SAVED,
        USER_COULD_NOT_BE_REMOVED,
        ACCOUNT_COULD_NOT_BE_SAVED,
        ACCOUNT_COULD_NOT_BE_REMOVED
    }

    public static HotelException hotelWithSameAddressAlreadyExists() {
        return new HotelException(HotelException.HotelErrors.HOTEL_WITH_SAME_ADDRESS_ALREADY_EXISTS);
    }

    public static HotelException hotelDoesNotExist() {
        return new HotelException(HotelException.HotelErrors.HOTEL_DOES_NOT_EXIST);
    }

    public static HotelException hotelNotUpdated() {
        return new HotelException(HotelException.HotelErrors.HOTEL_MAX_NUMBER_ROOMS_TOO_SMALL);
    }

}
