package org.trivago.light.reservation.web.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.trivago.light.reservation.web.exception.ReservationNotFoundException;
import org.trivago.light.reservation.web.exception.RoomAlreadyReservatedException;
import org.trivago.light.reservation.web.exception.RoomOutOfOrderException;

@ControllerAdvice
public class CustomerReservationControllerAdvice {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(RoomAlreadyReservatedException.class)
    public void handleRoomAlreadyReservated() {
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RoomOutOfOrderException.class)
    public void handleRoomOutOfOrder() {
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ReservationNotFoundException.class)
    public void handleReservationNotFound() {
    }

}
