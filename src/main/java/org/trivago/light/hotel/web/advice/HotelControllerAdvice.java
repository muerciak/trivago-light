package org.trivago.light.hotel.web.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.trivago.light.hotel.web.exception.HotelEntityNotFoundException;

@ControllerAdvice
public class HotelControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 400
    @ExceptionHandler(HotelEntityNotFoundException.class)
    public void handleConflict() {
    }
}
