package org.trivago.light.reservation.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.trivago.light.AbstractAcceptanceTest;
import org.trivago.light.customer.dto.CustomerDto;
import org.trivago.light.hotel.dto.HotelDto;
import org.trivago.light.hotel.dto.RoomDto;
import org.trivago.light.reservation.dto.RoomReservationDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;

public abstract class AbstractCustomerRoomReservationAcceptanceTest extends AbstractAcceptanceTest {

    protected void addRoom(Long hotelId) throws JsonProcessingException {
        RoomDto roomDto = RoomDto.builder().floor(1).name("1").size(2).price(new BigDecimal(4.5)).build();
        given()
                .body(objectMapper.writeValueAsString(roomDto))
                .contentType(ContentType.JSON)
                .when()
                .post("/hotel/" + hotelId + "/room");
    }



    protected Integer addTestCustomer() throws JsonProcessingException {
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

        return get("/customer/email/mu@gmail.com").andReturn().path("id");
    }

    private Integer findRoomReadyToBook() {
        LocalDate now = LocalDate.now();
        LocalDate nowPlus2 = now.plusDays(2);

        return get("/hotel/room/search?dateFrom=" + now.format(DateTimeFormatter.ISO_LOCAL_DATE) + "&dateTo=" + nowPlus2.format(DateTimeFormatter.ISO_LOCAL_DATE))
                .andReturn().path("[0].id");
    }

    protected RoomReservationDto getRoomReservationDto() throws JsonProcessingException {
        Integer customerId = addTestCustomer();
        Integer id = addHotel();
        addRoom(id.longValue());

        Integer roomId = findRoomReadyToBook();

        return RoomReservationDto
                .builder()
                .roomId(roomId.longValue())
                .hotelId(addHotel().longValue())
                .customerId(customerId.longValue())
                .dateFrom(LocalDate.now())
                .dateTo(LocalDate.now().plusDays(2))
                .build();
    }
}
