package guru.springframework.reactiveexamples;

import guru.springframework.reactiveexamples.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by jt on 2/27/21.
 */
public interface PersonRepository {

    Mono<Person> getById(Integer id);

    Flux<Person> findAll();
}
