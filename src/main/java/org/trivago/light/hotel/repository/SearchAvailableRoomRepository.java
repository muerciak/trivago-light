package org.trivago.light.hotel.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.trivago.light.hotel.domain.Room;

import java.time.LocalDate;
import java.util.List;

@org.springframework.stereotype.Repository
public interface SearchAvailableRoomRepository extends Repository<Room, Long> {

    //SELECT e FROM Professor e WHERE NOT EXISTS (SELECT p FROM e.phones p)
    //and res.dateTo <= :dateFrom
    @Query("select r from Room r where NOT exists (select res.room from CustomerRoomReservation res where not (:dateTo <= res.dateFrom or :dateFrom >= res.dateTo))")
    List<Room> findAvailableRooms(@Param("dateFrom") LocalDate dateFrom, @Param("dateTo") LocalDate dateTo);

}
