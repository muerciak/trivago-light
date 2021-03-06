package org.trivago.light.customer.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.trivago.light.customer.assembler.CustomerAssembler;
import org.trivago.light.customer.domain.Customer;
import org.trivago.light.customer.dto.CustomerDto;
import org.trivago.light.customer.repository.CustomerRepository;
import org.trivago.light.customer.web.exception.CustomerNotFoundException;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/customer")
@Slf4j
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerAssembler customerAssembler;

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void registerCustomer(@Valid @RequestBody CustomerDto customerDto) {
        log.info("register customer {} ", customerDto);
        customerRepository.save(customerAssembler.entity(customerDto));
    }

    @RequestMapping(value = "/email/{email:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public CustomerDto findCustomerByEmail(@Valid @PathVariable String email) {
        log.info("looking for customer via email {} ", email);
        Optional<Customer> customer = customerRepository.findByEmail(email);
        if (customer.isPresent()) {
            return customerAssembler.dto(customer.get());
        } else {
            throw new CustomerNotFoundException();
        }

    }

}
