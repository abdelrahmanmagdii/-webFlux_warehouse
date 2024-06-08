package com.javainnovations.advancedweb.lecture3.example1.service.impl;

import com.javainnovations.advancedweb.lecture3.example1.Example1Application;
import com.javainnovations.advancedweb.lecture3.example1.controller.MainController;
import com.javainnovations.advancedweb.lecture3.example1.model.dto.AppUser;
import com.javainnovations.advancedweb.lecture3.example1.repository.AppUserRepo;
import com.javainnovations.advancedweb.lecture3.example1.service.security.CustomReactiveUserDetailsService;
import com.javainnovations.advancedweb.lecture3.example1.service.template.DatabaseTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@WebFluxTest(controllers = MainController.class)

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = { Example1Application.class })
class MySqlDbTest {
    @InjectMocks
    MySqlDb service;

    @MockBean
    AppUserRepo repo;


    @Autowired
    WebTestClient webClient;

    @MockBean
    DatabaseTemplate db;

    @MockBean
    CustomReactiveUserDetailsService securityService;

    @Test
    void find() {
        lenient().when(repo.findById(Mockito.anyLong()))
                .thenReturn(Mono.just(AppUser.builder()
                        .id(1L)
                        .email("a")
                        .password("a")
                        .roleId(1)
                        .build()))
                ;

        Mono<AppUser> appUserMono = service.find(5L);

//        appUserMono.map(user -> {
//            assertNotNull(user);
//            assertEquals("bbb", user.getEmail());
//            return user;
//        }).subscribe();

        StepVerifier
                .create(appUserMono)
                .assertNext(user -> {
                    assertEquals("a", user.getEmail());
                    assertEquals("a", user.getPassword());
                })
                .verifyComplete();

    }

    @Test
    void findEmptyRepo() {
        lenient().when(repo.findById(Mockito.anyLong()))
                .thenReturn(Mono.empty());

        Mono<AppUser> appUserMono = repo.findById(5L);

        StepVerifier
                .create(appUserMono)
                .verifyComplete();
    }

    @Test
    void findEmpty() {
        lenient().when(repo.findById(Mockito.anyLong()))
                .thenReturn(Mono.empty());

        Mono<AppUser> appUserMono = service.find(5L);

        StepVerifier
                .create(appUserMono)
                .assertNext(user -> {
                    assertEquals(0L, user.getId());
                    assertNull(user.getEmail());
                })
                .verifyComplete();

    }

    @Test
    void findByName() {
        lenient().when(repo.findByNameContains(Mockito.anyString()))
                .thenReturn(Flux.just(AppUser.builder()
                        .id(1L)
                        .email("a")
                        .password("a")
                        .roleId(1)
                        .build(),
                        AppUser.builder()
                                .id(2L)
                                .email("b")
                                .password("b")
                                .roleId(3)
                                .build()
                        ));

        Flux<AppUser> result = service.findByNameFlux("TEST");

        StepVerifier
                .create(result)
                .assertNext(user -> {
                    assertEquals("a", user.getEmail());
                    assertEquals("a", user.getPassword());
                    assertEquals(1, user.getRoleId());
                })
                .assertNext(user -> {
                    assertEquals("b", user.getEmail());
                    assertEquals("b", user.getPassword());
                    assertEquals(3, user.getRoleId());
                })
                .verifyComplete();
    }

    @Test
    void findByNameEmptyList() {
        lenient().when(repo.findByNameContains(Mockito.anyString()))
                .thenReturn(Flux.empty());

        Flux<AppUser> result = service.findByNameFlux("TEST");

        StepVerifier
                .create(result)
                .verifyComplete();
    }
}