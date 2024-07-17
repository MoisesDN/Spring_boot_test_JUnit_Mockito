package br.com.moisesdias.rest_with_spring_boot_and_java.integrationtests.controller;

import br.com.moisesdias.rest_with_spring_boot_and_java.config.TestConfigs;
import br.com.moisesdias.rest_with_spring_boot_and_java.integrationtests.AbstractIntegrationTest;
import br.com.moisesdias.rest_with_spring_boot_and_java.models.Person;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.RequestBuilder;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class PersonControllerIntegrationTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper mapper;
    private static Person person;

    @BeforeAll
    public static void setup() {

        //Given / Arrange
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        specification = new RequestSpecBuilder()
                .setBasePath("/person")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        person = new Person("Moises",
                "Dias",
                "Campinas - São Paulo",
                "Male",
                "m.dias009@gmail.com");
    }

    @Test
    @Order(1)
    @DisplayName("JUnit integration Test given Person Object when Create one Person Should Return a Person Object")
    void integrationTestGivenPersonObject_When_CreateOnePerson_ShouldReturnAPersonObject() throws IOException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(person)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                        .extract()
                            .body()
                                .asString();

        Person createdPerson = mapper.readValue(content, Person.class);

        person = createdPerson;

        assertNotNull(createdPerson);
        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getEmail());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());

        assertTrue(createdPerson.getId() > 0);
        assertEquals("Moises", createdPerson.getFirstName());
        assertEquals("Dias", createdPerson.getLastName());
        assertEquals("m.dias009@gmail.com", createdPerson.getEmail());
        assertEquals("Campinas - São Paulo", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());


    }

    @Test
    @Order(2)
    @DisplayName("JUnit integration Test given Person Object when Update one Person Should Return a Updated Person Object")
    void integrationTestGivenPersonObject_When_UpdateOnePerson_ShouldReturnAUpdatedPersonObject() throws IOException {

        person.setFirstName("Beatriz");
        person.setGender("Female");
        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(person)
                .when()
                    .put()
                .then()
                    .statusCode(200)
                        .extract()
                            .body()
                                .asString();

        Person updatedPerson = mapper.readValue(content, Person.class);

        person = updatedPerson;

        assertNotNull(updatedPerson);
        assertNotNull(updatedPerson.getId());
        assertNotNull(updatedPerson.getFirstName());
        assertNotNull(updatedPerson.getLastName());
        assertNotNull(updatedPerson.getEmail());
        assertNotNull(updatedPerson.getAddress());
        assertNotNull(updatedPerson.getGender());

        assertTrue(updatedPerson.getId() > 0);
        assertEquals("Beatriz", updatedPerson.getFirstName());
        assertEquals("Dias", updatedPerson.getLastName());
        assertEquals("m.dias009@gmail.com", updatedPerson.getEmail());
        assertEquals("Campinas - São Paulo", updatedPerson.getAddress());
        assertEquals("Female", updatedPerson.getGender());


    }

    @Test
    @Order(3)
    @DisplayName("JUnit integration given Person Object when findById Should Return a Person Object")
    void integrationTestGivenPersonObject_When_findById_ShouldReturnAPersonObject() throws IOException {

        var content = given().spec(specification)
                .pathParam("id", person.getId())
                .when()
                    .get("/{id}")
                .then()
                    .statusCode(200)
                        .extract()
                            .body()
                                .asString();

        Person foundPerson = mapper.readValue(content, Person.class);

        assertNotNull(foundPerson);
        assertNotNull(foundPerson.getId());
        assertNotNull(foundPerson.getFirstName());
        assertNotNull(foundPerson.getLastName());
        assertNotNull(foundPerson.getEmail());
        assertNotNull(foundPerson.getAddress());
        assertNotNull(foundPerson.getGender());

        assertTrue(foundPerson.getId() > 0);
        assertEquals("Beatriz", foundPerson.getFirstName());
        assertEquals("Dias", foundPerson.getLastName());
        assertEquals("m.dias009@gmail.com", foundPerson.getEmail());
        assertEquals("Campinas - São Paulo", foundPerson.getAddress());
        assertEquals("Female", foundPerson.getGender());


    }

    @Test
    @Order(4)
    @DisplayName("JUnit integration given when findAll Should Return a Person List")
    void integrationTest_When_findAll_ShouldReturnAPersonList() throws IOException {

        Person person1 = new Person("Gabriela"
                , "Rodriguez"
                , "Uberlândia - Minas Gerais - Brasil"
                , "Female"
                , "gabi@gmail.com");
        given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(person1)
                .when()
                    .post()
                .then()
                    .statusCode(200);

        var content = given().spec(specification)
                .when()
                    .get()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .asString();

        Person[] myArray = mapper.readValue(content, Person[].class);
        List<Person> people = Arrays.asList(myArray);

        Person foundPersonOne = people.get(0);


        assertNotNull(foundPersonOne);
        assertNotNull(foundPersonOne.getId());
        assertNotNull(foundPersonOne.getFirstName());
        assertNotNull(foundPersonOne.getLastName());
        assertNotNull(foundPersonOne.getEmail());
        assertNotNull(foundPersonOne.getAddress());
        assertNotNull(foundPersonOne.getGender());

        assertTrue(foundPersonOne.getId() > 0);
        assertEquals("Beatriz", foundPersonOne.getFirstName());
        assertEquals("Dias", foundPersonOne.getLastName());
        assertEquals("m.dias009@gmail.com", foundPersonOne.getEmail());
        assertEquals("Campinas - São Paulo", foundPersonOne.getAddress());
        assertEquals("Female", foundPersonOne.getGender());

        Person foundPersonTwo = people.get(1);


        assertNotNull(foundPersonTwo);
        assertNotNull(foundPersonTwo.getId());
        assertNotNull(foundPersonTwo.getFirstName());
        assertNotNull(foundPersonTwo.getLastName());
        assertNotNull(foundPersonTwo.getEmail());
        assertNotNull(foundPersonTwo.getAddress());
        assertNotNull(foundPersonTwo.getGender());

        assertTrue(foundPersonTwo.getId() > 0);
        assertEquals("Gabriela", foundPersonTwo.getFirstName());
        assertEquals("Rodriguez", foundPersonTwo.getLastName());
        assertEquals("gabi@gmail.com", foundPersonTwo.getEmail());
        assertEquals("Uberlândia - Minas Gerais - Brasil", foundPersonTwo.getAddress());
        assertEquals("Female", foundPersonTwo.getGender());


    }

    @Test
    @Order(5)
    @DisplayName("JUnit integration given Person Object when delete Should Return No Content")
    void integrationTestGivenPersonObject_When_delete_ShouldReturnNoContent() throws IOException {

        given().spec(specification)
                .pathParam("id", person.getId())
                .when()
                    .delete("/{id}")
                .then()
                    .statusCode(204);


    }
}
