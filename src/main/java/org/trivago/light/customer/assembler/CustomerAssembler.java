package org.trivago.light.customer.assembler;

import org.springframework.stereotype.Service;
import org.trivago.light.application.assembler.Assembler;
import org.trivago.light.customer.domain.Customer;
import org.trivago.light.customer.dto.CustomerDto;

@Service
public class CustomerAssembler extends Assembler<CustomerDto, Customer> {

    public CustomerAssembler() {
        super(CustomerDto.class, Customer.class);
    }

}
