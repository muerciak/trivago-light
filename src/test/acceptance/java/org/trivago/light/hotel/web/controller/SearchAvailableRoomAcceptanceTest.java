package org.trivago.light.hotel.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.trivago.light.AbstractAcceptanceTest;
import org.trivago.light.hotel.domain.RoomStatus;
import org.trivago.light.reservation.dto.RoomReservationDto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.put;


public class SearchAvailableRoomAcceptanceTest extends AbstractAcceptanceTest {

    @Test
    public void shouldFindListOfAvailableRoomWhenAvailable() throws JsonProcessingException {
        Integer hotelId = addHotel();
        addRoom(hotelId);

        LocalDate now = LocalDate.now();
        LocalDate nowPlus2 = now.plusDays(2);

        given()
                .pathParam("dateFrom", now.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .pathParam("dateTo", nowPlus2.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .when()
                .get("/hotel/room/search?dateFrom={dateFrom}&dateTo={dateTo}")
                .then()
                .body("size", CoreMatchers.is(1));

        addRoom(hotelId);
        given()
                .pathParam("dateFrom", now.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .pathParam("dateTo", nowPlus2.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .when()
                .get("/hotel/room/search?dateFrom={dateFrom}&dateTo={dateTo}")
                .then()
                .body("size", CoreMatchers.is(2));
    }

    @Test
    public void shouldNotFindAnyAvailableRoomInNonExicstingCity() throws JsonProcessingException {
        Integer hotelId = addHotel();
        addRoom(hotelId);

        LocalDate now = LocalDate.now();
        LocalDate nowPlus2 = now.plusDays(2);

        given()
                .pathParam("dateFrom", now.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .pathParam("dateTo", nowPlus2.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .pathParam("city", "blahblah")
                .when()
                .get("/hotel/room/search?dateFrom={dateFrom}&dateTo={dateTo}&city={city}")
                .then()
                .body("size", CoreMatchers.is(0));
    }

    @Test
    public void shouldNotFindAnyAvailableRoomWithToHighPrice() throws JsonProcessingException {
        Integer hotelId = addHotel();
        addRoom(hotelId);

        LocalDate now = LocalDate.now();
        LocalDate nowPlus2 = now.plusDays(2);

        given()
                .pathParam("dateFrom", now.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .pathParam("dateTo", nowPlus2.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .when()
                .get("/hotel/room/search?dateFrom={dateFrom}&dateTo={dateTo}&priceFrom=10")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("size", CoreMatchers.is(0));
    }

    @Test
    public void shouldNotFindAnyAvailableRoomWithToLowPrice() throws JsonProcessingException {
        Integer hotelId = addHotel();
        addRoom(hotelId);

        LocalDate now = LocalDate.now();
        LocalDate nowPlus2 = now.plusDays(2);

        given()
                .pathParam("dateFrom", now.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .pathParam("dateTo", nowPlus2.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .when()
                .get("/hotel/room/search?dateFrom={dateFrom}&dateTo={dateTo}&priceTo=1")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("size", CoreMatchers.is(0));
    }

    @Test
    public void shouldFindAvailableRoomInProperCity() throws JsonProcessingException {
        Integer hotelId = addHotel();
        addRoom(hotelId);

        LocalDate now = LocalDate.now();
        LocalDate nowPlus2 = now.plusDays(2);

        given()
                .pathParam("dateFrom", now.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .pathParam("dateTo", nowPlus2.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .pathParam("city", "W")
                .when()
                .get("/hotel/room/search?dateFrom={dateFrom}&dateTo={dateTo}&city={city}")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("size", CoreMatchers.is(1));
    }

    @Test
    public void shouldFindAvailableRoomWhenPriceIsInRange() throws JsonProcessingException {
        Integer hotelId = addHotel();
        addRoom(hotelId);

        LocalDate now = LocalDate.now();
        LocalDate nowPlus2 = now.plusDays(2);

        given()
                .pathParam("dateFrom", now.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .pathParam("dateTo", nowPlus2.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .when()
                .get("/hotel/room/search?dateFrom={dateFrom}&dateTo={dateTo}&priceFrom=1&priceTo=5")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("size", CoreMatchers.is(1));
    }

    @Test
    public void shouldNotFindAnyAvailableRoomAfterChangeStatusToOutOfOrder() throws JsonProcessingException {
        Integer hotelId = addHotel();
        addRoom(hotelId);

        LocalDate now = LocalDate.now();
        LocalDate nowPlus2 = now.plusDays(2);

        given()
                .pathParam("dateFrom", now.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .pathParam("dateTo", nowPlus2.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .when()
                .get("/hotel/room/search?dateFrom={dateFrom}&dateTo={dateTo}")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("size", CoreMatchers.is(1));

        Integer roomId = get("/hotel/room/search?dateFrom=" + now.format(DateTimeFormatter.ISO_LOCAL_DATE) + "&dateTo=" + nowPlus2.format(DateTimeFormatter.ISO_LOCAL_DATE)).andReturn().path("[0].id");

        given()
                .contentType(ContentType.JSON)
                .pathParam("hotelId", hotelId)
                .pathParam("id", roomId)
                .pathParam("status", RoomStatus.OUT_OF_ORDER.status)
                .when()
                .put("/hotel/{hotelId}/room/{id}/status/{status}")
                .then()
                .statusCode(HttpStatus.SC_OK);

        given()
                .pathParam("dateFrom", now.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .pathParam("dateTo", nowPlus2.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .when()
                .get("/hotel/room/search?dateFrom={dateFrom}&dateTo={dateTo}")
                .then()
                .body("size", CoreMatchers.is(0));
    }

    @Test
    public void shouldNotFindListOfAvailableRoomAfterReservation() throws JsonProcessingException {
        Integer hotelId = addHotel();
        addRoom(hotelId);

        LocalDate now = LocalDate.now();
        LocalDate nowPlus2 = now.plusDays(2);

        given()
                .pathParam("dateFrom", now.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .pathParam("dateTo", nowPlus2.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .when()
                .get("/hotel/room/search?dateFrom={dateFrom}&dateTo={dateTo}")
                .then()
                .body("size", CoreMatchers.is(1));

        addRoom(hotelId);
        given()
                .pathParam("dateFrom", now.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .pathParam("dateTo", nowPlus2.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .when()
                .get("/hotel/room/search?dateFrom={dateFrom}&dateTo={dateTo}")
                .then()
                .body("size", CoreMatchers.is(2));

        Integer roomId = get("/hotel/room/search?dateFrom=" + now.format(DateTimeFormatter.ISO_LOCAL_DATE) + "&dateTo=" + nowPlus2.format(DateTimeFormatter.ISO_LOCAL_DATE)).andReturn().path("[0].id");
        Integer customerId = addTestCustomer();

        RoomReservationDto dto = RoomReservationDto
                .builder()
                .customerId(customerId.longValue())
                .dateFrom(now).dateTo(nowPlus2)
                .roomId(roomId.longValue())
                .hotelId(hotelId.longValue())
                .build();

        given()
                .body(objectMapper.writeValueAsString(dto))
                .contentType(ContentType.JSON)
                .pathParam("customerId", dto.getCustomerId())
                .when()
                .post("/customer/{customerId}/reservation")
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        given()
                .pathParam("dateFrom", now.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .pathParam("dateTo", nowPlus2.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .when()
                .get("/hotel/room/search?dateFrom={dateFrom}&dateTo={dateTo}")
                .then()
                .body("size", CoreMatchers.is(1));
    }

    @Test
    public void shouldFindListOfAvailableRoomAfterReservationAndCancelRervation() throws JsonProcessingException {
        Integer hotelId = addHotel();
        addRoom(hotelId);

        LocalDate now = LocalDate.now();
        LocalDate nowPlus2 = now.plusDays(2);

        given()
                .pathParam("dateFrom", now.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .pathParam("dateTo", nowPlus2.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .when()
                .get("/hotel/room/search?dateFrom={dateFrom}&dateTo={dateTo}")
                .then()
                .body("size", CoreMatchers.is(1));

        addRoom(hotelId);
        given()
                .pathParam("dateFrom", now.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .pathParam("dateTo", nowPlus2.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .when()
                .get("/hotel/room/search?dateFrom={dateFrom}&dateTo={dateTo}")
                .then()
                .body("size", CoreMatchers.is(2));

        Integer roomId = get("/hotel/room/search?dateFrom=" + now.format(DateTimeFormatter.ISO_LOCAL_DATE) + "&dateTo=" + nowPlus2.format(DateTimeFormatter.ISO_LOCAL_DATE)).andReturn().path("[0].id");
        Integer customerId = addTestCustomer();

        RoomReservationDto dto = RoomReservationDto
                .builder()
                .customerId(customerId.longValue())
                .dateFrom(now).dateTo(nowPlus2)
                .roomId(roomId.longValue())
                .hotelId(hotelId.longValue())
                .build();

        given()
                .body(objectMapper.writeValueAsString(dto))
                .contentType(ContentType.JSON)
                .pathParam("customerId", dto.getCustomerId())
                .when()
                .post("/customer/{customerId}/reservation")
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        given()
                .pathParam("dateFrom", now.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .pathParam("dateTo", nowPlus2.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .when()
                .get("/hotel/room/search?dateFrom={dateFrom}&dateTo={dateTo}")
                .then()
                .body("size", CoreMatchers.is(1));

        Integer resId = get("/customer/"+customerId+"/reservation").andReturn().path("[0].id");

        given()
                .pathParam("resId", resId)
        .when()
                .put("/customer/reservation/{resId}/cancel")
        .then()
                .statusCode(HttpStatus.SC_OK);

        given()
                .pathParam("dateFrom", now.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .pathParam("dateTo", nowPlus2.format(DateTimeFormatter.ISO_LOCAL_DATE))
        .when()
                .get("/hotel/room/search?dateFrom={dateFrom}&dateTo={dateTo}")
        .then()
                .body("size", CoreMatchers.is(2));
    }

}
