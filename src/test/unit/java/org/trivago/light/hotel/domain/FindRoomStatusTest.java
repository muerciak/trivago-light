package org.trivago.light.hotel.domain;

import org.junit.Test;
import org.trivago.light.hotel.web.exception.StatusNotFoundException;

import static org.junit.Assert.assertEquals;

public class FindRoomStatusTest {

    @Test
    public void shouldFindExistStatuss() {
        assertEquals(RoomStatus.find(RoomStatus.OUT_OF_ORDER.status), RoomStatus.OUT_OF_ORDER);
        assertEquals(RoomStatus.find(RoomStatus.READY_FOR_ORDER.status), RoomStatus.READY_FOR_ORDER);
    }

    @Test(expected = StatusNotFoundException.class)
    public void shouldThrowStatusNotFoundExceptionWhenStatusNotExist() {
        RoomStatus.find(1002);
    }
}