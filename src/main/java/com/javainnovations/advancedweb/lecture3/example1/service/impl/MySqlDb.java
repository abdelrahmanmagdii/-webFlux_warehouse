package com.javainnovations.advancedweb.lecture3.example1.service.impl;

import com.javainnovations.advancedweb.lecture3.example1.model.dto.AppUser;
import com.javainnovations.advancedweb.lecture3.example1.repository.AppUserRepo;
import com.javainnovations.advancedweb.lecture3.example1.service.template.DatabaseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class MySqlDb implements DatabaseTemplate {

    @Autowired
    AppUserRepo repo;


    @Override
    public Mono<AppUser> find(Long id) {
        return repo.findById(id)
                .switchIfEmpty(Mono.just(AppUser.builder()
                                .id(0L)
                        .build()));
    }

    @Override
    public Mono<AppUser> findByEmail(String email) {
        return Mono.from(repo.findByEmail(email));
    }

    @Override
    public Mono<List<AppUser>> findByName(String name) {
        return repo.findByNameContains(name).collectList();

    }

    @Override
    public Flux<AppUser> findByNameFlux(String name) {
        return repo.findByNameContains(name);

    }
}
