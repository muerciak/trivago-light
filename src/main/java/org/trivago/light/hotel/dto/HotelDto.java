package org.trivago.light.hotel.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@Builder
public class HotelDto {

    @NotEmpty
    private String name;

    @NotEmpty
    private String city;

    @NotEmpty
    private String country;

}
