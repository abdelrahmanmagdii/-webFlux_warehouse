package com.javainnovations.advancedweb.lecture3.example1.repository;

import com.javainnovations.advancedweb.lecture3.example1.model.dto.AppUser;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

//TODO read https://www.bezkoder.com/spring-r2dbc-mysql/
//TODO read https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html

public interface AppUserRepo  extends ReactiveCrudRepository<AppUser, Long> {
    Flux<AppUser> findByEmail(String email);

    Flux<AppUser> findByNameContains(String namePart);
}
