package com.javainnovations.advancedweb.lecture3.example1.service.template;

import com.javainnovations.advancedweb.lecture3.example1.model.dto.AppUser;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface DatabaseTemplate {
    Mono<AppUser> find(Long id);
    Mono<AppUser> findByEmail(String email);

    Mono<List<AppUser>> findByName(String namePart);

    Flux<AppUser> findByNameFlux(String name);
}
