package org.trivago.light.hotel.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.trivago.light.AbstractAcceptanceTest;
import org.trivago.light.hotel.dto.HotelDto;
import org.trivago.light.hotel.dto.RoomDto;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;


public class RoomRegisterAcceptanceTest extends AbstractAcceptanceTest {

    public static final String ID = "id";

    @Test
    public void shoulRegisterRoomWhenDataIsProper() throws Exception {

        Integer hotelId = getHotelId();

        RoomDto roomDto = RoomDto.builder().floor(1).name("1").size(2).hotelId(hotelId.longValue()).build();

        given()
                .body(objectMapper.writeValueAsString(roomDto))
                .contentType(ContentType.JSON)
                .when()
                .post("/hotel/room")
                .then()
                .statusCode(HttpStatus.SC_CREATED);

    }

    @Test
    public void shouldGet400WhenHotelNotExists() throws JsonProcessingException {
        Integer hotelId = 9393923; //not exist hotel id

        RoomDto roomDto = RoomDto.builder().floor(1).name("1").size(2).hotelId(hotelId.longValue()).build();

        given()
                .body(objectMapper.writeValueAsString(roomDto))
                .contentType(ContentType.JSON)
                .when()
                .post("/hotel/room")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void shouldGet400WhenRequestDataIsNotComplete() throws JsonProcessingException {
        Integer hotelId = getHotelId();

        RoomDto roomDto = RoomDto.builder().name("1").size(2).hotelId(hotelId.longValue()).build();

        given()
                .body(objectMapper.writeValueAsString(roomDto))
                .contentType(ContentType.JSON)
                .when()
                .post("/hotel/room")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);

        roomDto = RoomDto.builder().name("1").floor(2).hotelId(hotelId.longValue()).build();

        given()
                .body(objectMapper.writeValueAsString(roomDto))
                .contentType(ContentType.JSON)
                .when()
                .post("/hotel/room")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);

        roomDto = RoomDto.builder().floor(1).size(2).hotelId(hotelId.longValue()).build();

        given()
                .body(objectMapper.writeValueAsString(roomDto))
                .contentType(ContentType.JSON)
                .when()
                .post("/hotel/room")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);

        roomDto = RoomDto.builder().build();

        given()
                .body(objectMapper.writeValueAsString(roomDto))
                .contentType(ContentType.JSON)
                .when()
                .post("/hotel/room")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }


    private Integer getHotelId() throws JsonProcessingException {
        HotelDto hotel = HotelDto
                .builder()
                .city("W")
                .country("P")
                .name("H1")
                .build();

        Response response = given()
                .body(objectMapper.writeValueAsString(hotel))
                .contentType(ContentType.JSON)
                .when()
                .post("/hotel/")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
//                .header(HttpHeaders.LOCATION, n)
                .extract()
                .response();

        return get(response.getHeaders().getValue(HttpHeaders.LOCATION)).andReturn().path(ID);
    }

}