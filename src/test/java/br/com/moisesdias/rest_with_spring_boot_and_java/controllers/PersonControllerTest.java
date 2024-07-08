package br.com.moisesdias.rest_with_spring_boot_and_java.controllers;

import br.com.moisesdias.rest_with_spring_boot_and_java.exceptions.ResourceNotFoundException;
import br.com.moisesdias.rest_with_spring_boot_and_java.models.Person;
import br.com.moisesdias.rest_with_spring_boot_and_java.services.PersonServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;


@WebMvcTest
public class PersonControllerTest {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonServices service;

    private Person person;

    @BeforeEach
    public void setup() {
        //Given / Arrange
        person = new Person("Moises",
                "Dias",
                "Campinas - São Paulo",
                "Male",
                "m.dias009@gmail.com");
    }

    @Test
    @DisplayName("JUnit test for Given List of Persons when FindAll Persons then Return Persons List")
    void GivenListOfPersons_WhenFindAllPersons_thenReturnPersonsList() throws JsonProcessingException, Exception {

        //Given / Arrange
        List<Person> personsList = new ArrayList<>();
        personsList.add(person);
        personsList.add(new Person("Beatriz",
                "Dias",
                "Campinas - São Paulo",
                "Female",
                "beatriste@gmail.com"));


        given(service.findAll()).willReturn(personsList);

        // When / Act
        ResultActions response = mockMvc.perform(get("/person"));

        // Then / Assert
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(personsList.size())));
    }

    @Test
    @DisplayName("JUnit test for Given personId when FindById then Return Person Object")
    void testGivenPersonId_whenFindById_thenReturnPersonObject() throws JsonProcessingException, Exception  {
        //Given / Arrange
        long personId = 1L;
        given(service.findById(personId)).willReturn(person);

        // When / Act
        ResultActions response = mockMvc.perform(get("/person/{id}", personId));
        // Then / Assert
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())))
                .andExpect(jsonPath("$.email", is(person.getEmail())));
    }

    @Test
    @DisplayName("JUnit test for Given Invalid personId when FindById then Return NotFound")
    void testGivenInvalidPersonId_whenFindById_thenReturnNotFound() throws JsonProcessingException, Exception  {
        //Given / Arrange
        long personId = 1L;
        given(service.findById(personId)).willThrow(ResourceNotFoundException.class);

        // When / Act
        ResultActions response = mockMvc.perform(get("/person/{id}", personId));
        // Then / Assert
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("JUnit test for Given Person Object when Create Person the Return Saved Person")
    void testGivenPersonObject_whenCreatePerson_thenReturnSavedPerson() throws JsonProcessingException, Exception  {
        //Given / Arrange
        given(service.create(any(Person.class))).willAnswer((invocation) -> invocation.getArgument(0));

        // When / Act
       ResultActions response = mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(person)));
        // Then / Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(person.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(person.getLastName())))
                .andExpect(jsonPath("$.email", is(person.getEmail())));
    }

    @Test
    @DisplayName("JUnit test for Given Person when Update then Return Person Object")
    void testGivenPerson_whenUpdate_thenReturnPersonObject() throws JsonProcessingException, Exception  {
        //Given / Arrange
        long personId = 1L;
        given(service.findById(personId)).willReturn(person);
        given(service.update(any(Person.class))).willAnswer((invocation) -> invocation.getArgument(0));

        // When / Act
        Person updatedPerson = new Person("Beatriz",
                "Dias",
                "Campinas - São Paulo",
                "Female",
                "beatriste@gmail.com");
        ResultActions response = mockMvc.perform(put("/person", personId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedPerson)));

        // Then / Assert
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedPerson.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedPerson.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedPerson.getEmail())));
    }

    @Test
    @DisplayName("JUnit test for Given Unexistent Person when Update then Return Not Found")
    void testGivenUnexistentPerson_whenUpdate_thenReturnNotFound() throws JsonProcessingException, Exception  {
        //Given / Arrange
        long personId = 1L;
        given(service.findById(personId)).willThrow(ResourceNotFoundException.class);;
        given(service.update(any(Person.class))).willAnswer((invocation) -> invocation.getArgument(1));

        // When / Act
        Person updatedPerson = new Person("Beatriz",
                "Dias",
                "Campinas - São Paulo",
                "Female",
                "beatriste@gmail.com");
        ResultActions response = mockMvc.perform(put("/person", personId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedPerson)));

        // Then / Assert
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("JUnit test for Given PersonId when Delete then Return Not Content")
    void testGivenPersonId_whenDelete_thenReturnNotContent() throws JsonProcessingException, Exception  {
        //Given / Arrange
        long personId = 1L;
        willDoNothing().given(service).delete(personId);

        // When / Act

        ResultActions response = mockMvc.perform(delete("/person/{id}", personId));

        // Then / Assert
        response.andExpect(status().isNoContent())
                .andDo(print());
    }
}