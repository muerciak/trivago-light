package org.trivago.light.reservation.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.trivago.light.reservation.dto.RoomReservationDto;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;

public class CustomerCheckOwnReservationAcceptanceTest extends AbstractCustomerRoomReservationAcceptanceTest {


    @Test
    public void shouldReturnListOfConsumerReservation() throws JsonProcessingException {
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
                .body("hotelId", hasItem(roomReservationDto.getHotelId().intValue()))
                .body("roomId", hasItem(roomReservationDto.getRoomId().intValue()))
                .body("dateFrom", hasItem(roomReservationDto.getDateFrom().format(DateTimeFormatter.ISO_DATE)))
                .body("dateTo", hasItem(roomReservationDto.getDateTo().format(DateTimeFormatter.ISO_DATE)))
                .body("size()", is(1));

        roomReservationDto = getRoomReservationDto(Optional.of(roomReservationDto.getCustomerId()));

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
                .body("hotelId", hasItem(roomReservationDto.getHotelId().intValue()))
                .body("roomId", hasItem(roomReservationDto.getRoomId().intValue()))
                .body("dateFrom", hasItem(roomReservationDto.getDateFrom().format(DateTimeFormatter.ISO_DATE)))
                .body("dateTo", hasItem(roomReservationDto.getDateTo().format(DateTimeFormatter.ISO_DATE)))
                .body("size()", is(2));

        roomReservationDto = getRoomReservationDto(Optional.of(roomReservationDto.getCustomerId()));

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
                .body("size()", is(3))
                .body("hotelId", hasItem(roomReservationDto.getHotelId().intValue()))
                .body("roomId", hasItem(roomReservationDto.getRoomId().intValue()))
                .body("dateFrom", hasItem(roomReservationDto.getDateFrom().format(DateTimeFormatter.ISO_DATE)))
                .body("dateTo", hasItem(roomReservationDto.getDateTo().format(DateTimeFormatter.ISO_DATE)));
    }

    @Test
    public void shouldNotShowInListReservationAfterCancel() throws JsonProcessingException {
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
                .body("hotelId", hasItem(roomReservationDto.getHotelId().intValue()))
                .body("roomId", hasItem(roomReservationDto.getRoomId().intValue()))
                .body("dateFrom", hasItem(roomReservationDto.getDateFrom().format(DateTimeFormatter.ISO_DATE)))
                .body("dateTo", hasItem(roomReservationDto.getDateTo().format(DateTimeFormatter.ISO_DATE)))
                .body("size()", is(1));

        Integer resId = get("/customer/" + roomReservationDto.getCustomerId() + "/reservation").andReturn().path("[0].id");

        given()
                .pathParam("resId", resId)
                .when()
                .put("/customer/reservation/{resId}/cancel")
                .then()
                .statusCode(HttpStatus.SC_OK);

        given()
                .pathParam("customerId", roomReservationDto.getCustomerId())
                .when()
                .get("/customer/{customerId}/reservation")
                .then()
                .body("size()", is(0));
    }

}
