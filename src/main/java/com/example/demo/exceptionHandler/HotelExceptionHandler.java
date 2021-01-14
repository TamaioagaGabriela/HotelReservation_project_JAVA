package com.example.demo.exceptionHandler;

import com.example.demo.exception.HotelException;
import com.example.demo.exception.ReviewException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class HotelExceptionHandler {
    @ExceptionHandler(HotelException.class)
    public final ResponseEntity<String> handleHotelException(HotelException exception,
                                                               WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (exception.getError() == HotelException.HotelErrors.USER_NOT_FOUND) {
            status = HttpStatus.UNAUTHORIZED;
        } else if (exception.getError() == HotelException.HotelErrors.BAD_CREDENTIALS) {
            status = HttpStatus.FORBIDDEN;
        } else if (exception.getError() == HotelException.HotelErrors.BAD_REQUEST) {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<String>(exception.getError().name(), status);
    }
}
