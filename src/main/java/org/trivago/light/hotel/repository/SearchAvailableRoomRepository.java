package org.trivago.light.hotel.repository;

import org.springframework.data.repository.Repository;
import org.trivago.light.hotel.domain.Room;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public interface SearchAvailableRoomRepository extends Repository<Room, Long> {

    List<Room> findAvailableRoomsAndPriceRangeAndCity(LocalDate dateFrom,
                                                      LocalDate dateTo,
                                                      Optional<String> city,
                                                      Optional<BigDecimal> priceFrom,
                                                      Optional<BigDecimal> priceTo);

}
