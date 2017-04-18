package org.trivago.light.hotel.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.trivago.light.hotel.domain.Room;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class SearchAvailableRoomRepositoryImpl implements SearchAvailableRoomRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Room> findAvailableRoomsAndPriceRangeAndCity(LocalDate dateFrom, LocalDate dateTo, Optional<String> city, Optional<BigDecimal> priceFrom, Optional<BigDecimal> priceTo) {
        String queryStr = "select r from Room r join r.hotel h where r not in (select res.room from CustomerRoomReservation res where (:dateTo <= res.dateFrom or :dateFrom >= res.dateTo))";
        Map<String, Object> params = new HashMap<String, Object>();

        if (city.isPresent()) {
            queryStr += " and h.city like :city";
            params.put("city", city.get());
        }
        if (priceFrom.isPresent()) {
            queryStr += " and r.price >= :priceFrom";
            params.put("priceFrom", priceFrom.get());
        }

        if (priceTo.isPresent()) {
            queryStr += " and r.price <= :priceTo";
            params.put("priceTo", priceTo.get());
        }

        Query query = entityManager.createQuery(queryStr);
        params.put("dateTo", dateTo);
        params.put("dateFrom", dateFrom);
        params.keySet().stream().forEach(key -> query.setParameter(key, params.get(key)));
        return query.getResultList();
    }
}
