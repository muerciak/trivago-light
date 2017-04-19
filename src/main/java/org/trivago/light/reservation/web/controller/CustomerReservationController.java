package org.trivago.light.reservation.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.trivago.light.hotel.domain.Room;
import org.trivago.light.hotel.domain.RoomStatus;
import org.trivago.light.hotel.repository.HotelRepository;
import org.trivago.light.reservation.assembler.CustomerRoomReservationAssembler;
import org.trivago.light.reservation.domain.CustomerRoomReservation;
import org.trivago.light.reservation.domain.ReservationStatus;
import org.trivago.light.reservation.dto.RoomReservationDto;
import org.trivago.light.reservation.repository.ReservationRepository;
import org.trivago.light.reservation.validator.RoomReservationValidator;
import org.trivago.light.reservation.web.exception.ReservationNotFoundException;
import org.trivago.light.reservation.web.exception.RoomAlreadyReservatedException;
import org.trivago.light.reservation.web.exception.RoomOutOfOrderException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/customer")
@Slf4j
public class CustomerReservationController {

    private final ReservationRepository reservationRepository;

    private final CustomerRoomReservationAssembler customerRoomReservationAssembler;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    public CustomerReservationController(ReservationRepository reservationRepository, CustomerRoomReservationAssembler customerRoomReservationAssembler) {
        this.reservationRepository = reservationRepository;
        this.customerRoomReservationAssembler = customerRoomReservationAssembler;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new RoomReservationValidator());
    }

    @RequestMapping(value = "/reservation/{id}/cancel", method = PUT)
    @ResponseStatus(HttpStatus.OK)
    public void cancelReservation(@PathVariable(value = "id") Long reservationId) {
        Optional<CustomerRoomReservation> roomReservation = reservationRepository.getById(reservationId);
        if (!roomReservation.isPresent()) {
            throw new ReservationNotFoundException();
        }
        roomReservation.get().setStatus(ReservationStatus.CANCEL);
        reservationRepository.save(roomReservation.get());
        log.debug("User cancel own reservation {} ", reservationId);
    }

    @RequestMapping(value = "/{id}/reservation", method = GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<RoomReservationDto> getUserReservation(@PathVariable(value = "id") Long userId) {
        List<CustomerRoomReservation> roomReservation = reservationRepository.findByCustomerId(userId);
        return roomReservation
                .stream()
                .map(customerRoomReservationAssembler::dto)
                .collect(toList());
    }

    @RequestMapping(value = "/{customerId}/reservation", method = POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void reserveRoom(@Valid @RequestBody RoomReservationDto reservationDto) {
        log.info("user ask for reservation {} ", reservationDto);
        Long i = reservationRepository.countByRoomIdAndDateFromGreaterThanEqualAndDateToLessThanEqual(reservationDto.getRoomId(), reservationDto.getDateFrom(), reservationDto.getDateTo());
        if (i > 0) {
            throw new RoomAlreadyReservatedException();
        }
        Optional<Room> room = hotelRepository.findRoomById(reservationDto.getRoomId());
        if (room.isPresent() && room.get().getRoomStatus().equals(RoomStatus.OUT_OF_ORDER)) {
            throw new RoomOutOfOrderException();
        }
        CustomerRoomReservation roomReservationEntity = customerRoomReservationAssembler.entity(reservationDto);
        reservationRepository.save(roomReservationEntity);
    }

}
