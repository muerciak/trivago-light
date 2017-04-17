package org.trivago.light.customer.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.trivago.light.AbstractAcceptanceTest;
import org.trivago.light.customer.dto.CustomerDto;

import static com.jayway.restassured.RestAssured.given;

public class CustomerRegisterAcceptanceTest extends AbstractAcceptanceTest {


    @Test
    public void shouldRegisterNewCustomer() throws JsonProcessingException {
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
                .post("/customer/")
                .then()
                .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    public void shouldReturn409WhenAddCustomerWithExistingEmail() throws JsonProcessingException {
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
                .post("/customer/")
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        given()
                .body(objectMapper.writeValueAsString(dto))
                .contentType(ContentType.JSON)
                .when()
                .post("/customer/")
                .then()
                .statusCode(HttpStatus.SC_CONFLICT);
    }

    @Test
    public void shouldReturn400WhenAddCustomerNotValid() throws JsonProcessingException {
        CustomerDto dto = CustomerDto
                .builder()
                .city("w")
                .country("a")
                .firstName("z")
                .lastName("aa").build();

        given()
                .body(objectMapper.writeValueAsString(dto))
                .contentType(ContentType.JSON)
                .when()
                .post("/customer/")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void shouldReturn400WhenEmailIsNotValid() throws JsonProcessingException {
        CustomerDto dto = CustomerDto
                .builder()
                .email("a")
                .city("w")
                .country("a")
                .firstName("z")
                .lastName("aa").build();

        given()
                .body(objectMapper.writeValueAsString(dto))
                .contentType(ContentType.JSON)
                .when()
                .post("/customer/")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

}