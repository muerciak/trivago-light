package org.trivago.light.hotel.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.trivago.light.AbstractAcceptanceTest;
import org.trivago.light.hotel.domain.RoomStatus;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class ChangeRoomStatusAcceptanceTest extends AbstractAcceptanceTest {

    @Test
    public void shouldChangeRoomStatus() throws JsonProcessingException {
        Integer hotelId = addHotel();

        Integer roomId = addRoom(hotelId);

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
                .pathParam("hotelId", hotelId)
        .when()
                .get("/hotel/{hotelId}/room")
        .then()
               .body("[0].roomStatus", equalTo(RoomStatus.OUT_OF_ORDER.name()));

    }

    @Test
    public void shouldGet404WhenRoomNotFound() throws JsonProcessingException {
        Integer hotelId = addHotel();
        given()
                .contentType(ContentType.JSON)
                .pathParam("hotelId", hotelId)
                .pathParam("id", 13131)
                .pathParam("status", RoomStatus.OUT_OF_ORDER.status)
        .when()
                .put("/hotel/{hotelId}/room/{id}/status/{status}")
        .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void shouldGet400WhenRoomStatusNotRecognize() throws JsonProcessingException {
        Integer hotelId = addHotel();
        Integer roomId = addRoom(hotelId);
        given()
                .contentType(ContentType.JSON)
                .pathParam("hotelId", hotelId)
                .pathParam("id", roomId)
                .pathParam("status", 5212)
        .when()
                .put("/hotel/{hotelId}/room/{id}/status/{status}")
        .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }


}
