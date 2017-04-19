package org.trivago.light.hotel.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.trivago.light.application.domain.Entity;
import org.trivago.light.hotel.assembler.HotelAssembler;
import org.trivago.light.hotel.assembler.RoomAssembler;
import org.trivago.light.hotel.domain.Hotel;
import org.trivago.light.hotel.domain.Room;
import org.trivago.light.hotel.domain.RoomStatus;
import org.trivago.light.hotel.dto.HotelDto;
import org.trivago.light.hotel.dto.RoomDto;
import org.trivago.light.hotel.repository.HotelRepository;
import org.trivago.light.hotel.web.exception.HotelEntityNotFoundException;
import org.trivago.light.hotel.web.exception.RoomEntityNotFoundException;
import org.trivago.light.hotel.web.exception.StatusNotFoundException;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@RestController
@RequestMapping("/hotel")
@Slf4j
@Transactional
public class HotelController {

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    HotelAssembler hotelAssembler;

    @Autowired
    RoomAssembler roomAssembler;

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> registerHotel(@Valid @RequestBody HotelDto hotel, UriComponentsBuilder uriComponentsBuilder) {
        log.info("register new hotel {}", hotel);
        Hotel savedHotel = hotelRepository.save(hotelAssembler.entity(hotel));
        return returnResultPost(uriComponentsBuilder, savedHotel);
    }

    @RequestMapping(value = "/{hotelId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public HotelDto getHotel(@PathVariable Long hotelId) {
        Optional<Hotel> hotel = hotelRepository.findOne(hotelId);
        if (!hotel.isPresent()) {
            throw new HotelEntityNotFoundException(hotelId);
        }
        return hotelAssembler.dto(hotel.get());
    }

    private ResponseEntity<Void> returnResultPost(UriComponentsBuilder uriComponentsBuilder, Entity entity) {
        HttpHeaders headers = new HttpHeaders();
        UriComponents uriComponents = uriComponentsBuilder.path("/hotel/{id}").buildAndExpand(entity.getId());
        headers.setLocation(uriComponents.toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{hotelId}/room", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void registerRoom(@Valid @RequestBody RoomDto roomDto, @PathVariable("hotelId") Long hotelId) {
        log.info("register new roomDto in hotel {}", roomDto);
        Optional<Hotel> hotel = hotelRepository.findOne(hotelId);
        if (hotel.isPresent()) {
            Room room = roomAssembler.entity(roomDto);
            room.setHotel(hotel.get());
            room.setRoomStatus(RoomStatus.READY_FOR_ORDER);
            hotel.get().getRoomList().add(room);
        } else {
            throw new HotelEntityNotFoundException(hotelId);
        }
    }

    @RequestMapping(value = "/{hotelId}/room/{id}/status/{status}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void changeRoomStatus(@PathVariable("id") Long roomId, @PathVariable("status") Integer status, @PathVariable("hotelId") Long hotelId) {
        log.info("update room status {}", roomId);
        Optional<Room> room = hotelRepository.findRoomById(roomId);
        if (room.isPresent()) {
            room.get().setRoomStatus(RoomStatus.find(status));
        } else {
            throw new RoomEntityNotFoundException();
        }
    }

    @RequestMapping(value = "/{hotelId}/room", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<RoomDto> getRoomList(@PathVariable("hotelId") Long hotelId) {
        log.info("looking for hotel {}", hotelId);
        Optional<Hotel> hotel = hotelRepository.findOne(hotelId);
        if (hotel.isPresent()) {
            return hotel.get().getRoomList().stream().map(roomAssembler::dto).collect(toList());
        } else {
            throw new HotelEntityNotFoundException(hotelId);
        }
    }
}
