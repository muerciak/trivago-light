package org.trivago.light.hotel.web.controller;

import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.trivago.light.AbstractAcceptanceTest;
import org.trivago.light.hotel.dto.HotelDto;

import static com.jayway.restassured.RestAssured.given;


public class HotelRegisterAcceptanceTest extends AbstractAcceptanceTest {

    @Test
    public void shouldRegisterHotelWhenDataIsProper() throws Exception {
        HotelDto hotel = HotelDto
                .builder()
                .city("W")
                .country("P")
                .name("H1")
                .build();

        given()
                .body(objectMapper.writeValueAsString(hotel))
                .contentType(ContentType.JSON)
                .when()
                .post("/hotel/")
                .then()
                .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    public void shouldGet400WhenDataIsNotCompleted() throws Exception {
        HotelDto hotelWihoutCity = HotelDto
                .builder()
                .country("P")
                .name("H1")
                .build();

        HotelDto hotelWihoutCountry = HotelDto
                .builder()
                .city("P")
                .name("H1")
                .build();


        HotelDto hotelWihoutName = HotelDto
                .builder()
                .country("P")
                .city("H1")
                .build();

        given()
                .body(objectMapper.writeValueAsString(hotelWihoutCity))
                .contentType(ContentType.JSON)
                .when()
                .post("/hotel/")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);

        given()
                .body(objectMapper.writeValueAsString(hotelWihoutCountry))
                .contentType(ContentType.JSON)
                .when()
                .post("/hotel/")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);

        given()
                .body(objectMapper.writeValueAsString(hotelWihoutName))
                .contentType(ContentType.JSON)
                .when()
                .post("/hotel/")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);

        given()
                .body(objectMapper.writeValueAsString(HotelDto.builder().build()))
                .contentType(ContentType.JSON)
                .when()
                .post("/hotel/")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

}