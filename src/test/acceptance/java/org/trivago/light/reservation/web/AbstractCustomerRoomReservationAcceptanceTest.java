package org.trivago.light.reservation.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.trivago.light.AbstractAcceptanceTest;
import org.trivago.light.customer.dto.CustomerDto;
import org.trivago.light.hotel.dto.HotelDto;
import org.trivago.light.hotel.dto.RoomDto;

import java.math.BigDecimal;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;

public class AbstractCustomerRoomReservationAcceptanceTest extends AbstractAcceptanceTest {

    protected void addRoom(Long hotelId) throws JsonProcessingException {
        RoomDto roomDto = RoomDto.builder().floor(1).name("1").size(2).price(new BigDecimal(4.5)).build();

        given()
                .body(objectMapper.writeValueAsString(roomDto))
                .contentType(ContentType.JSON)
                .when()
                .post("/hotel/" + hotelId + "/room");
    }

    protected Integer addHotel() throws JsonProcessingException {
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
                .extract()
                .response();

        return get(response.getHeaders().getValue(HttpHeaders.LOCATION)).andReturn().path("id");

    }

    protected void addTestCustomer() throws JsonProcessingException {
        CustomerDto dto = CustomerDto
                .builder()
                .city("w")
                .country("a")
                .email("mu@gmail.com")
                .firstName("z")
                .lastName("aa").build();

        given()
                .body(objectMapper.writeValueAsString(dto))
                .contentType(ContentType.JSON)
                .when()
                .post("/customer/");
    }

}
