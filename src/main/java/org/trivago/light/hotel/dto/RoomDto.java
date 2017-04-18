package org.trivago.light.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {

    @NotNull
    BigDecimal price;

    @NotEmpty
    private String name;

    @NotNull
    private Integer size;

    @NotNull
    private Integer floor;

    private String roomStatus;

    private Long id;
}
