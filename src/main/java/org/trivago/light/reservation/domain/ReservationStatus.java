package org.trivago.light.reservation.domain;

public enum ReservationStatus {

    CANCEL(0), ASK_FOR_RESERVATION(1);

    private Integer status;

    ReservationStatus(Integer status) {
        this.status = status;
    }

}
