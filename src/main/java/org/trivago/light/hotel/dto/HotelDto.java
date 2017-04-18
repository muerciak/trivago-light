package org.trivago.light.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelDto {

    List<RoomDto> roomList;
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String city;
    @NotEmpty
    private String country;
}
