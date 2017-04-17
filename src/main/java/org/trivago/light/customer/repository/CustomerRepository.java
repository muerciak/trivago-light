package org.trivago.light.customer.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.trivago.light.customer.domain.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
