package org.trivago.light.reservation.assembler;

import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Service;
import org.trivago.light.application.assembler.Assembler;
import org.trivago.light.reservation.domain.CustomerRoomReservation;
import org.trivago.light.reservation.dto.RoomReservationDto;

@Service
public class CustomerRoomReservationAssembler extends Assembler<RoomReservationDto, CustomerRoomReservation> {

    public CustomerRoomReservationAssembler() {
        super(RoomReservationDto.class, CustomerRoomReservation.class);
        modelMapper.addMappings(new PropertyMap<RoomReservationDto, CustomerRoomReservation>() {
            protected void configure() {
                map().setId(null);
            }
        });
    }

}
