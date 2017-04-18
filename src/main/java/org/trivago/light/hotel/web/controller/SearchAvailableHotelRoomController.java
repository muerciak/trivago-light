package org.trivago.light.hotel.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.trivago.light.hotel.assembler.RoomAssembler;
import org.trivago.light.hotel.domain.Room;
import org.trivago.light.hotel.dto.RoomDto;
import org.trivago.light.hotel.repository.SearchAvailableRoomRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/hotel/room/search")
public class SearchAvailableHotelRoomController {

    @Autowired
    SearchAvailableRoomRepository searchAvailableRoomRepository;

    @Autowired
    RoomAssembler roomAssembler;

    @RequestMapping("")
    public List<RoomDto> searchAvailableRoom(@RequestParam("priceFrom") Optional<BigDecimal> priceFrom,
                                             @RequestParam("priceTo") Optional<BigDecimal> priceTo,
                                             @RequestParam("dateFrom") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
                                             @RequestParam("dateTo") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
                                             @RequestParam("city") Optional<String> city) {

        List<Room> list = searchAvailableRoomRepository.findAvailableRoomsAndPriceRangeAndCity(dateFrom, dateTo, city, priceFrom, priceTo);
        return list
                .stream()
                .map(roomAssembler::dto)
                .collect(toList());
    }

}
