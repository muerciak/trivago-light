package org.trivago.light.reservation.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.trivago.light.hotel.domain.RoomStatus;
import org.trivago.light.reservation.dto.RoomReservationDto;

import java.util.Optional;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class CustomerRoomReservationAcceptanceTest extends AbstractCustomerRoomReservationAcceptanceTest {

    @Test
    public void shouldReserveRoomWhenAvailable() throws JsonProcessingException {
        RoomReservationDto roomReservationDto = getRoomReservationDto(Optional.empty());

        given()
                .body(objectMapper.writeValueAsString(roomReservationDto))
                .contentType(ContentType.JSON)
                .pathParam("customerId", roomReservationDto.getCustomerId())
        .when()
                .post("/customer/{customerId}/reservation")
        .then()
                .statusCode(HttpStatus.SC_CREATED);

        given()
                .pathParam("customerId", roomReservationDto.getCustomerId())
        .when()
                .get("/customer/{customerId}/reservation")
        .then()
                .body("size()", is(1));

    }

    @Test
    public void shouldGet400WhenRoomAlreadyReserved() throws JsonProcessingException {
        RoomReservationDto roomReservationDto = getRoomReservationDto(Optional.empty());

        given()
                .body(objectMapper.writeValueAsString(roomReservationDto))
                .contentType(ContentType.JSON)
                .pathParam("customerId", roomReservationDto.getCustomerId())
                .when()
                .post("/customer/{customerId}/reservation")
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        given()
                .pathParam("customerId", roomReservationDto.getCustomerId())
                .when()
                .get("/customer/{customerId}/reservation")
                .then()
                .body("size()", is(1));

        given()
                .body(objectMapper.writeValueAsString(roomReservationDto))
                .contentType(ContentType.JSON)
                .pathParam("customerId", roomReservationDto.getCustomerId())
        .when()
                .post("/customer/{customerId}/reservation")
        .then()
                .statusCode(HttpStatus.SC_CONFLICT);

    }

    @Test
    public void shouldGet400ReserveRoomIsOutOfOrder() throws JsonProcessingException {
        RoomReservationDto roomReservationDto = getRoomReservationDto(Optional.empty());

        given()
                .contentType(ContentType.JSON)
                .pathParam("hotelId", roomReservationDto.getHotelId())
                .pathParam("id", roomReservationDto.getRoomId())
                .pathParam("status", RoomStatus.OUT_OF_ORDER.status)
        .when()
                .put("/hotel/{hotelId}/room/{id}/status/{status}")
        .then()
                .statusCode(HttpStatus.SC_OK);

        given()
                .body(objectMapper.writeValueAsString(roomReservationDto))
                .contentType(ContentType.JSON)
                .pathParam("customerId", roomReservationDto.getCustomerId())
                .when()
        .post("/customer/{customerId}/reservation")
                .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST);

    }


}