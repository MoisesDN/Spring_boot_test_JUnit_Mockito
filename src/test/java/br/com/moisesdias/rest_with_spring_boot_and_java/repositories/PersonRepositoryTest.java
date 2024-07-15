package br.com.moisesdias.rest_with_spring_boot_and_java.repositories;

import br.com.moisesdias.rest_with_spring_boot_and_java.integrationtests.AbstractIntegrationTest;
import br.com.moisesdias.rest_with_spring_boot_and_java.models.Person;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PersonRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private PersonRepository repository;

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

    @DisplayName("JUnit test for Given Person Object when Save then Return Saved Person")
    @Test
    void testGivenPersonObject_whenSave_thenReturnSavedPerson() {

        //When / Act
        Person savedPerson = repository.save(person0);
        //Then / Assert
        assertNotNull(savedPerson);
        assertTrue(savedPerson.getId() > 0);
    }

    @DisplayName("JUnit test for Given Person list when FindAll then Return Person List")
    @Test
    void testGivenPersonList_whenFindAll_thenReturnPersonList() {
        Person person1 = new Person("Marcos", "Araujo", "São Paulo - São Paulo", "Male", "maraujo@gmail.com");

        repository.save(person0);
        repository.save(person1);
        //When / Act
        List<Person> personList = repository.findAll();
        //Then / Assert
        assertNotNull(personList);
        assertEquals(2, personList.size());
    }

    @DisplayName("JUnit test for Given Person Object when FindById then Return Person Object")
    @Test
    void testGivenPersonObject_whenFindById_thenReturnPersonObject() {

        repository.save(person0);
        //When / Act
        Person savedPerson = repository.findById(person0.getId()).get();
        //Then / Assert
        assertNotNull(savedPerson);
        assertEquals(person0.getId(), savedPerson.getId());
    }

    @DisplayName("JUnit test for Given Person Object when FindByEmail then Return Person Object")
    @Test
    void testGivenPersonObject_whenFindByEmail_thenReturnPersonObject() {

        repository.save(person0);
        //When / Act
        Person savedPerson = repository.findByEmail(person0.getEmail()).get();
        //Then / Assert
        assertNotNull(savedPerson);
        assertEquals(person0.getEmail(), savedPerson.getEmail());
    }

    @DisplayName("JUnit test for Given Person Object when UpdatePerson then Return Updated Person Object")
    @Test
    void testGivenPersonObject_whenUpdatePerson_thenReturnUpdatedPersonObject() {

        repository.save(person0);
        //When / Act
        Person savedPerson = repository.findById(person0.getId()).get();
        savedPerson.setFirstName("Leonardo");
        savedPerson.setEmail("leonardo@gmail.com");

        Person updatedPerson = repository.save(savedPerson);
        //Then / Assert
        assertNotNull(savedPerson);
        assertEquals("Leonardo", updatedPerson.getFirstName());
        assertEquals("leonardo@gmail.com", updatedPerson.getEmail());
    }

    @DisplayName("JUnit test for Given Person Object when Delete then Remove Person")
    @Test
    void testGivenPersonObject_whenDelete_thenRemovePerson() {

        repository.save(person0);
        //When / Act
        repository.deleteById(person0.getId());


         Optional<Person> personOptional = repository.findById(person0.getId());
        //Then / Assert
        assertTrue(personOptional.isEmpty());
    }

    @DisplayName("JUnit test for Given FirstName And LastName when FindByJPQL then Return Person Object")
    @Test
    void testGivenFirstNameAndLastName_whenFindByJPQL_thenReturnPersonObject() {

       repository.save(person0);

        String firstName = "Moises";
        String lastName = "Dias";
        //When / Act
        Person savedPerson = repository.findByJPQL(firstName, lastName);
        //Then / Assert
        assertNotNull(savedPerson);
        assertEquals(firstName, savedPerson.getFirstName());
        assertEquals(lastName, savedPerson.getLastName());
    }

    @DisplayName("JUnit test for Given FirstName And LastName when FindByJPQLNamedParameters then Return Person Object")
    @Test
    void testGivenFirstNameAndLastName_whenFindByJPQLNamedParameters_thenReturnPersonObject() {

        repository.save(person0);

        String firstName = "Moises";
        String lastName = "Dias";
        //When / Act
        Person savedPerson = repository.findByJPQLNamedParameters(firstName, lastName);
        //Then / Assert
        assertNotNull(savedPerson);
        assertEquals(firstName, savedPerson.getFirstName());
        assertEquals(lastName, savedPerson.getLastName());
    }

    @DisplayName("JUnit test for Given FirstName And LastName when FindByNativeSQL then Return Person Object")
    @Test
    void testGivenFirstNameAndLastName_whenFindByNativeSQL_thenReturnPersonObject() {

        repository.save(person0);

        String firstName = "Moises";
        String lastName = "Dias";
        //When / Act
        Person savedPerson = repository.findByNativeSQL(firstName, lastName);
        //Then / Assert
        assertNotNull(savedPerson);
        assertEquals(firstName, savedPerson.getFirstName());
        assertEquals(lastName, savedPerson.getLastName());
    }

    @DisplayName("JUnit test for Given FirstName And LastName when FindByNativeSQLwithNamedParameters then Return Person Object")
    @Test
    void testGivenFirstNameAndLastName_whenFindByNativeSQLwithNamedParameters_thenReturnPersonObject() {

        repository.save(person0);

        String firstName = "Moises";
        String lastName = "Dias";
        //When / Act
        Person savedPerson = repository.findByNativeSQLwithNamedParameters(firstName, lastName);
        //Then / Assert
        assertNotNull(savedPerson);
        assertEquals(firstName, savedPerson.getFirstName());
        assertEquals(lastName, savedPerson.getLastName());
    }
}
