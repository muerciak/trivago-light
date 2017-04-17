package org.trivago.light.customer.domain;


import lombok.Data;
import org.trivago.light.application.domain.Entity;
import org.trivago.light.reservation.domain.CustomerRoomReservation;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.util.List;

@javax.persistence.Entity
@Data
public class Customer extends Entity {

    @Column(unique = true, updatable = false, nullable = false)
    private String email;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String phone;

    @Column
    private String country;

    @Column
    private String city;

    @OneToMany(mappedBy = "customer")
    private List<CustomerRoomReservation> reservations;

}
