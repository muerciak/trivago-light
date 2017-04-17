package org.trivago.light.reservation.repository;

import org.springframework.data.repository.CrudRepository;
import org.trivago.light.reservation.domain.CustomerRoomReservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends CrudRepository<CustomerRoomReservation, Long> {

    List<CustomerRoomReservation> findByCustomerId(Long customerId);

    Long countByRoomIdAndDateFromGreaterThanEqualAndDateToLessThanEqual(Long roomId, LocalDate dateFrom, LocalDate dateTo);

    Optional<CustomerRoomReservation> getById(Long id);
}
