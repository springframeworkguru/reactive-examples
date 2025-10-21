package guru.springframework.reactiveexamples;

import guru.springframework.reactiveexamples.domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

class PersonRepositoryImplTest {

    PersonRepositoryImpl personRepository;

    @BeforeEach
    void setUp() {
        personRepository = new PersonRepositoryImpl();
    }

    @Test
    void getByIdBlock() {
        Mono<Person> personMono = personRepository.getById(1);
        Person person = personMono.block();
        System.out.println(person.toString());
    }

    @Test
    void getByIdSubscribe() {
        Mono<Person> personMono = personRepository.getById(1);
        personMono.subscribe(person -> System.out.println(person.toString()));
    }

    @Test
    void getByMapFunction() {
        Mono<Person> personMono = personRepository.getById(1);
        personMono
                .map(person -> {
                    person.setFirstName("Changed Name");
                    return person;
                })
                .subscribe(person -> System.out.println(person.toString()));
    }
}