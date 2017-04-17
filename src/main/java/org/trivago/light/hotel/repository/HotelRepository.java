package org.trivago.light.hotel.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.trivago.light.hotel.domain.Hotel;
import org.trivago.light.hotel.domain.Room;

import java.util.Optional;

@org.springframework.stereotype.Repository
public interface HotelRepository extends Repository<Hotel, Long> {

    Optional<Hotel> findOne(Long id);

    @Query("select r from Hotel h join h.roomList r where r.id = :roomId")
    Optional<Room> findRoomById(@Param("roomId") Long id);

    Hotel save(Hotel hotel);
}
