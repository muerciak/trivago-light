package org.trivago.light.hotel.domain;

import lombok.Data;
import lombok.ToString;
import org.trivago.light.application.domain.Entity;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@javax.persistence.Entity
@Data
@ToString
public class Room extends Entity {

    @ManyToOne
    private Hotel hotel;

    @Column
    private String name;

    @Column
    private Integer size;

    @Column
    private Integer floor;

    @Column
    private BigDecimal price;

    @Enumerated(EnumType.ORDINAL)
    private RoomStatus roomStatus = RoomStatus.READY_FOR_ORDER;

}
