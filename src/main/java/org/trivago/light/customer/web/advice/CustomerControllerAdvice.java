package org.trivago.light.customer.web.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.trivago.light.customer.web.exception.CustomerNotFoundException;

@ControllerAdvice
public class CustomerControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomerNotFoundException.class)
    public void handleCustomerNotFound() {
    }
}
