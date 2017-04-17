package org.trivago.light.hotel.assembler;

import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Service;
import org.trivago.light.application.assembler.Assembler;
import org.trivago.light.hotel.domain.Room;
import org.trivago.light.hotel.dto.RoomDto;

@Service
public class RoomAssembler extends Assembler<RoomDto, Room> {

    public RoomAssembler() {
        super(RoomDto.class, Room.class);
        modelMapper.addMappings(new PropertyMap<RoomDto, Room>() {
            protected void configure() {
                map().setId(null);
            }
        });
    }


}
