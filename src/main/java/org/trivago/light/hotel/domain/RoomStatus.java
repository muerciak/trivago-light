package org.trivago.light.hotel.domain;

import org.trivago.light.hotel.web.exception.StatusNotFoundException;

public enum RoomStatus {

    READY_FOR_ORDER(0), OUT_OF_ORDER(1);

    public Integer status;

    RoomStatus(Integer status) {
        this.status = status;
    }

    public static RoomStatus find(Integer status) {
        for (RoomStatus roomStatus : values()) {
            if (roomStatus.status.equals(status)) {
                return roomStatus;
            }
        }
        throw new StatusNotFoundException();
    }
}
