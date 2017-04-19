package org.trivago.light;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.trivago.light.customer.dto.CustomerDto;
import org.trivago.light.hotel.dto.HotelDto;
import org.trivago.light.hotel.dto.RoomDto;

import java.math.BigDecimal;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public abstract class AbstractAcceptanceTest {

    protected ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        JacksonTester.initFields(this, objectMapper);
        RestAssured.port = Integer.valueOf(port);
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

    protected Integer addRoom(Integer hotelId) throws JsonProcessingException {
        RoomDto roomDto = RoomDto.builder().floor(1).name("1").size(2).price(new BigDecimal(4.5)).build();
        given()
                .body(objectMapper.writeValueAsString(roomDto))
                .pathParam("hotelId", hotelId)
                .contentType(ContentType.JSON)
                .when()
                .post("/hotel/{hotelId}/room");
        return get("/hotel/" +hotelId+ "/room").andReturn().path("[0].id");
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
}
