package org.trivago.light.reservation.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.restassured.http.ContentType;
import org.junit.Test;
import org.trivago.light.reservation.dto.RoomReservationDto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.jayway.restassured.RestAssured.given;

public class CustomerRoomReservationAcceptanceTest extends AbstractCustomerRoomReservationAcceptanceTest {

    @Test
    public void test() throws JsonProcessingException {
        addTestCustomer();
        Integer id = addHotel();
        addRoom(id.longValue());

        LocalDate now = LocalDate.now();
        LocalDate nowPlus2 = now.plusDays(2);

        String jsonString = given()
                .when()
                .get("/hotel/room/search?dateFrom=" + now.format(DateTimeFormatter.ISO_LOCAL_DATE) + "&dateTo=" + nowPlus2.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .then()
                .extract()
                .response().getBody().asString();


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
                .post("/customer/reservation");


    }


}