package guru.springframework.reactiveexamples;

import guru.springframework.reactiveexamples.domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

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
        Integer idToSearch = 1;
        Mono<Person> personMono = personRepository.getById(idToSearch);

        StepVerifier.create(personMono).expectNextCount(1).verifyComplete();

    }

    @Test
    void getByIdSubscribeNotFound() {
        Integer idToSearch = 100;
        Mono<Person> personMono = personRepository.getById(idToSearch);

        StepVerifier.create(personMono).verifyComplete();

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

    @Test
    void fluxTestBlockFirst() {
        Flux<Person> personFlux = personRepository.findAll();
        Person person = personFlux.blockFirst();
        System.out.println(person.toString());
    }

    @Test
    void fluxTestSubscribe() {
        Flux<Person> personFlux = personRepository.findAll();
        personFlux.subscribe(person -> System.out.println(person.toString()));
    }

    @Test
    void testFluxToListMono() {
        Flux<Person> personFlux = personRepository.findAll();
        Mono<List<Person>> personListMono = personFlux.collectList();
        personListMono.subscribe(personList ->
                personList.forEach(person ->
                        System.out.println(person.toString())
                )
        );
    }

    @Test
    void testFluxToFlux() {
        Flux<Person> personFlux = personRepository.findAll();
        final Integer idToFind = 3;
        Mono<Person> filteredPerson = personFlux.filter(person -> idToFind.equals(person.getId())).next();
        filteredPerson.subscribe(person -> System.out.println(person.toString()));
    }

    @Test
    void testFluxToFluxNotFound() {
        Flux<Person> personFlux = personRepository.findAll();
        final Integer idToFind = 100;
        Mono<Person> filteredPerson = personFlux.filter(person -> idToFind.equals(person.getId())).next();
        filteredPerson.subscribe(person -> System.out.println(person.toString()));
    }

    @Test
    void testFluxToFluxNotFoundWithException() {
        Flux<Person> personFlux = personRepository.findAll();
        final Integer idToFind = 100;
        Mono<Person> personMono = personFlux.filter(person -> idToFind.equals(person.getId())).single();
        personMono
                .doOnError(throwable -> System.out.println("Person not found!"))
                .onErrorReturn(Person.builder().build())
                .subscribe(person -> System.out.println(person.toString()));
    }

}