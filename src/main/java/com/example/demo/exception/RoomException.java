package com.example.demo.exception;

public class RoomException extends RuntimeException{

    private RoomException.RoomErrors error;

    private RoomException(RoomException.RoomErrors error) {
        this.error = error;
    }

    public RoomException.RoomErrors getError() {
        return error;
    }


    public enum RoomErrors {
        USER_NOT_FOUND,
        BAD_CREDENTIALS,
        BAD_REQUEST,
        ROOM_WITH_SAME_NUMBER_ROOM_ALREADY_EXISTS,
        ROOM_COULD_NOT_BE_UPDATED,
        ID_HOTEL_NOT_FOUND,
        ROOM_COULD_NOT_BE_SAVED,
        MAXIMUM_NUMBER_OF_ROOMS_IS_REACHED,
        PAYMENT_COULD_NOT_BE_PROCESSED,
        CLIENT_WITH_SAME_EMAIL_ALREADY_EXISTS,
        CLIENT_WITH_NO_LASTNAME_OR_FIRSTNAME,
        ROOM_DOES_NOT_EXIST
    }

    public static RoomException roomWithSameNumberRoomAlreadyExists() {
        return new RoomException(RoomException.RoomErrors.ROOM_WITH_SAME_NUMBER_ROOM_ALREADY_EXISTS);
    }

    public static RoomException roomDoesNotExist() {
        return new RoomException(RoomException.RoomErrors.ROOM_DOES_NOT_EXIST);
    }

    public static RoomException roomHotelDoesNotExist() {
        return new RoomException(RoomException.RoomErrors.ID_HOTEL_NOT_FOUND);
    }

    public static RoomException roomNotUpdated(){
        return new RoomException(RoomException.RoomErrors.ROOM_COULD_NOT_BE_UPDATED);
    }

    public static RoomException roomNotSaved(){
        return new RoomException(RoomException.RoomErrors.MAXIMUM_NUMBER_OF_ROOMS_IS_REACHED);
    }
}
