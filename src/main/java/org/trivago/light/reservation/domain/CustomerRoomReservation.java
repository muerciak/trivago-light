package org.trivago.light.reservation.domain;

import lombok.Data;
import lombok.ToString;
import org.trivago.light.application.domain.Entity;
import org.trivago.light.customer.domain.Customer;
import org.trivago.light.hotel.domain.Hotel;
import org.trivago.light.hotel.domain.Room;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@javax.persistence.Entity
@Data
@ToString
public class CustomerRoomReservation extends Entity {

    @ManyToOne
    private Room room;

    @ManyToOne
    private Hotel hotel;

    private LocalDate dateFrom;

    private LocalDate dateTo;

    @ManyToOne
    private Customer customer;

    @Enumerated(EnumType.ORDINAL)
    private ReservationStatus status = ReservationStatus.ASK_FOR_RESERVATION;

}
