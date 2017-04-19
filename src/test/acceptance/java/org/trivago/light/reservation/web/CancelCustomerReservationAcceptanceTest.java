package org.trivago.light.reservation.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.trivago.light.reservation.dto.RoomReservationDto;

import java.time.LocalDate;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class CancelCustomerReservationAcceptanceTest extends AbstractCustomerRoomReservationAcceptanceTest {

    @Test
    public void shodCancelReservation() throws JsonProcessingException {
        RoomReservationDto roomReservationDto = getRoomReservationDto();

        given()
            .body(objectMapper.writeValueAsString(roomReservationDto))
            .contentType(ContentType.JSON)
            .pathParam("customerId", roomReservationDto.getCustomerId())
        .when()
            .post("/customer/{customerId}/reservation")
        .then()
            .statusCode(HttpStatus.SC_CREATED);

        Integer id = get("/customer/"+roomReservationDto.getCustomerId()+"/reservation").andReturn().path("[0].id");

        given()
            .pathParam("resId", id)
        .when()
            .put("/customer/reservation/{resId}/cancel")
        .then()
            .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void shodGet404WhenReservationToCancelNotFound() throws JsonProcessingException {
        given()
            .pathParam("resId", 1)
        .when()
            .put("/customer/reservation/{resId}/cancel")
        .then()
            .statusCode(HttpStatus.SC_NOT_FOUND);
    }
}
