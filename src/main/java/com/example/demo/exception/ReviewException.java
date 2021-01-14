package com.example.demo.exception;

public class ReviewException extends RuntimeException{

    private ReviewException.ReviewErrors error;

    private ReviewException(ReviewException.ReviewErrors error) {
        this.error = error;
    }



    public ReviewException.ReviewErrors getError() {
        return error;
    }


    public enum ReviewErrors {
        USER_NOT_FOUND,
        BAD_CREDENTIALS,
        BAD_REQUEST,
        REVIEW_DOES_NOT_EXIST,
        REVIEW_COULD_NOT_BE_UPDATED,
        REVIEW_COULD_NOT_BE_SAVED,
        REVIEW_RATE_TOO_HIGH,
        REVIEW_WITH_ID_HOTEL_NOT_FOUND,
        HOTEL_NOT_FOUND,
        CLIENT_NOT_FOUND,
        REVIEW_WITH_ID_CLIENT_NOT_FOUND
    }


    public static ReviewException reviewWithNoId(){
        return new ReviewException(ReviewException.ReviewErrors.REVIEW_DOES_NOT_EXIST);
    }

    public static ReviewException fieldsNull(){
        return new ReviewException(ReviewException.ReviewErrors.REVIEW_COULD_NOT_BE_UPDATED);
    }

    public static ReviewException fieldsNullNotSaved(){
        return new ReviewException(ReviewException.ReviewErrors.REVIEW_COULD_NOT_BE_SAVED);
    }

    public static ReviewException rateTooHigh(){
        return new ReviewException(ReviewException.ReviewErrors.REVIEW_RATE_TOO_HIGH);
    }

    public static ReviewException reviewNotSaved(){
        return new ReviewException(ReviewErrors.REVIEW_COULD_NOT_BE_SAVED);
    }

    public static ReviewException reviewNotFound() {
        return new ReviewException(ReviewErrors.REVIEW_DOES_NOT_EXIST);
    }

    public static ReviewException reviewWithIdHotelNotFound() {
        return new ReviewException(ReviewErrors.REVIEW_WITH_ID_HOTEL_NOT_FOUND);
    }

    public static ReviewException hotelNotFound() {
        return new ReviewException(ReviewErrors.HOTEL_NOT_FOUND);
    }

    public static ReviewException clientNotFound() {
        return new ReviewException(ReviewErrors.CLIENT_NOT_FOUND);
    }

    public static ReviewException reviewWithIdClientNotFound() {
        return new ReviewException(ReviewErrors.REVIEW_WITH_ID_CLIENT_NOT_FOUND);
    }
}
