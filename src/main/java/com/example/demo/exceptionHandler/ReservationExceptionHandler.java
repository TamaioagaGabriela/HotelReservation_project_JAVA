package com.example.demo.exceptionHandler;

import com.example.demo.exception.ReservationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ReservationExceptionHandler {

    @ExceptionHandler(ReservationException.class)
    public final ResponseEntity<String> handleReservationException(ReservationException exception,
                                                            WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (exception.getError() == ReservationException.ReservationErrors.NOT_FOUND) {
            status = HttpStatus.UNAUTHORIZED;
        } else if (exception.getError() == ReservationException.ReservationErrors.BAD_CREDENTIALS) {
            status = HttpStatus.FORBIDDEN;
        } else if (exception.getError() == ReservationException.ReservationErrors.BAD_REQUEST) {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<String>(exception.getError().name(), status);
    }
}
