package br.com.moisesdias.rest_with_spring_boot_and_java.services;

import br.com.moisesdias.rest_with_spring_boot_and_java.exceptions.ResourceNotFoundException;
import br.com.moisesdias.rest_with_spring_boot_and_java.models.Person;
import br.com.moisesdias.rest_with_spring_boot_and_java.repositories.PersonRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServicesTest {

    @Mock
    private PersonRepository repository;
    @InjectMocks
    private PersonServices services;

    private Person person0;

    @BeforeEach
    public void setup() {
        //Given / Arrange
        person0 = new Person("Moises",
                "Dias",
                "Campinas - São Paulo",
                "Male",
                "m.dias009@gmail.com");
    }

    @DisplayName("JUnit test for Given Person Object When Save Person then Return Person Object")
    @Test
    void testGivenPersonObject_WhenSavePerson_thenReturnPersonObject() {

        //Given / Arrange
        given(repository.findByEmail(anyString())).willReturn(Optional.empty());
        given(repository.save(person0)).willReturn(person0);

        //When / Act
        Person savedPerson = services.create(person0);

        //Then / Assert
        assertNotNull(savedPerson);
        assertEquals( "Moises", savedPerson.getFirstName());

    }

    @DisplayName("JUnit test for Given Existing Email When Save Person then Throws Exception")
    @Test
    void testGivenExistingEmail_WhenSavePerson_thenThrowsException() {

        //Given / Arrange
        given(repository.findByEmail(anyString())).willReturn(Optional.of(person0));

        //When / Act
        assertThrows(ResourceNotFoundException.class, () -> {
            services.create(person0);
        });

        //Then / Assert
        verify(repository, never()).save(any(Person.class));

    }

    @DisplayName("JUnit test for Given Persons List When Return Persons List")
    @Test
    void testGivenPersonsList_WhenFindAllPersons_thenReturnPersonsList() {

        //Given / Arrange
        Person person1 = new Person("Marcos",
                "Araujo",
                "São Paulo - São Paulo",
                "Male",
                "maraujo@gmail.com");

        given(repository.findAll()).willReturn(List.of(person0, person1));
        //When / Act
        List<Person> personsList = services.findAll();

        //Then / Assert
        assertNotNull(personsList);
        assertEquals(2, personsList.size());

    }

    @DisplayName("JUnit test for Given Empty Persons List When Return Empty Persons List")
    @Test
    void testGivenEmptyPersonsList_WhenFindAllPersons_thenReturnEmptyPersonsList() {

        //Given / Arrange
        given(repository.findAll()).willReturn(Collections.emptyList());
        //When / Act
        List<Person> personsList = services.findAll();

        //Then / Assert
        assertNotNull(personsList.isEmpty());
        assertEquals(0, personsList.size());

    }

    @DisplayName("JUnit test for Given PersonId When FindById then Return Person Object")
    @Test
    void testGivenPersonId_WhenFindById_thenReturnPersonObject() {

        //Given / Arrange
        given(repository.findById(anyLong())).willReturn(Optional.of(person0));

        //When / Act
        Person savedPerson = services.findById(1L);

        //Then / Assert
        assertNotNull(savedPerson);
        assertEquals( "Moises", savedPerson.getFirstName());

    }

    @DisplayName("JUnit test for Given Person Object When Update Person then Return Updated Person Object")
    @Test
    void testGivenPersonObject_WhenUpdatePerson_thenReturnUpdatedPersonObject() {

        //Given / Arrange
        person0.setId(1L);
        given(repository.findById(anyLong())).willReturn(Optional.of(person0));

        person0.setEmail("maraujo@gmail.com");
        person0.setFirstName("Marcos");

        given(repository.save(person0)).willReturn(person0);

        //When / Act
        Person UpdatedPerson = services.update(person0);

        //Then / Assert
        assertNotNull(UpdatedPerson);
        assertEquals( "Marcos", UpdatedPerson.getFirstName());

    }

    @DisplayName("JUnit test for Given PersonID Object When Delete Person then do Nothing")
    @Test
    void testGivenPersonID_WhenDeletePerson_thenReturnUpdatedPersonObject() {

        //Given / Arrange
        person0.setId(1L);
        given(repository.findById(anyLong())).willReturn(Optional.of(person0));
        willDoNothing().given(repository).delete(person0);

        //When / Act
        services.delete(person0.getId());

        //Then / Assert
        verify(repository, times(1)).delete(person0);

    }
}