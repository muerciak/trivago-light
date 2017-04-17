package org.trivago.light.hotel.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No found hotel")  // 404
public class HotelEntityNotFoundException extends RuntimeException {
    public HotelEntityNotFoundException(Long hotelId) {
        super();
    }
}
