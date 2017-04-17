package org.trivago.light.hotel.assembler;

import org.springframework.stereotype.Service;
import org.trivago.light.application.assembler.Assembler;
import org.trivago.light.hotel.domain.Hotel;
import org.trivago.light.hotel.dto.HotelDto;

@Service
public class HotelAssembler extends Assembler<HotelDto, Hotel> {

    protected HotelAssembler() {
        super(HotelDto.class, Hotel.class);
    }

}
