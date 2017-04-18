package org.trivago.light.hotel.web.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.trivago.light.hotel.web.exception.HotelEntityNotFoundException;
import org.trivago.light.hotel.web.exception.RoomEntityNotFoundException;
import org.trivago.light.hotel.web.exception.StatusNotFoundException;

@ControllerAdvice
public class HotelControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)  // 404
    @ExceptionHandler(HotelEntityNotFoundException.class)
    public void handleConflict() {
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RoomEntityNotFoundException.class)
    public void handleRoomNotFoundException() {
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(StatusNotFoundException.class)
    public void handleStatusNotFoundException() {
    }
}
