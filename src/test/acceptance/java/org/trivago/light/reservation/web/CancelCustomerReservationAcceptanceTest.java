package org.trivago.light.reservation.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.trivago.light.reservation.dto.RoomReservationDto;

import java.time.LocalDate;

import static com.jayway.restassured.RestAssured.given;

public class CancelCustomerReservationAcceptanceTest extends AbstractCustomerRoomReservationAcceptanceTest {

    @Test
    public void test() throws JsonProcessingException {
        addTestCustomer();
        Integer id = addHotel();
        addRoom(id.longValue());
        RoomReservationDto roomReservationDto = RoomReservationDto
                .builder()
                .roomId(1L)
                .hotelId(1L)
                .customerId(1L)
                .dateFrom(LocalDate.now())
                .dateTo(LocalDate.now().plusDays(2))
                .build();

        given()
                .body(objectMapper.writeValueAsString(roomReservationDto))
                .contentType(ContentType.JSON)
                .when()
                .post("/customer/reservation")
                .then()
                .statusCode(HttpStatus.SC_CREATED);


    }
}
