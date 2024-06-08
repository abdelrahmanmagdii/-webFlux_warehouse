package com.javainnovations.advancedweb.lecture3.example1.controller;

import com.javainnovations.advancedweb.lecture3.example1.Example1Application;
import com.javainnovations.advancedweb.lecture3.example1.config.SecurityConfig;
import com.javainnovations.advancedweb.lecture3.example1.model.core.Message;
import com.javainnovations.advancedweb.lecture3.example1.model.dto.AppUser;
import com.javainnovations.advancedweb.lecture3.example1.repository.AppUserRepo;
import com.javainnovations.advancedweb.lecture3.example1.service.security.CustomReactiveUserDetailsService;
import com.javainnovations.advancedweb.lecture3.example1.service.template.DatabaseTemplate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockUser;


@WebFluxTest(controllers = MainController.class)

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = { Example1Application.class })
@Import(SecurityConfig.class)
class MainControllerTest {

    @Autowired
    WebTestClient webClient;

    @MockBean
    DatabaseTemplate db;

    @MockBean
    AppUserRepo repo;

    @MockBean
    CustomReactiveUserDetailsService securityService;


    @Test
    void root() {

        when(securityService.findByUsername(anyString()))
                .thenReturn(Mono.just(new User("a", "a",
                        AuthorityUtils.commaSeparatedStringToAuthorityList("1"))));

        String response = webClient
                .mutateWith(mockUser("a").password("a").roles("1")
                        .authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("1"))
                )
                .get()
                .uri("/")
                //.body(BodyInserters.fromObject(mo))
                .exchange().expectStatus().isOk()
                .expectHeader().contentType("text/plain;charset=UTF-8")
                .expectBody(String.class)
                .consumeWith(EntityExchangeResult::getResponseBody)
                .returnResult()
                .getResponseBody();
        assertEquals("HI", response);
    }

    @Test
    void getUser() {

        when(securityService.findByUsername(anyString()))
                .thenReturn(Mono.just(new User("a", "a",
                        AuthorityUtils.commaSeparatedStringToAuthorityList("1"))));
        lenient().when(db.find(Mockito.anyLong()))
                .thenReturn(Mono.just(AppUser.builder()
                                .id(1L)
                                .email("a")
                                .password("a")
                                .roleId(1)
                        .build()));

        AppUser response = webClient
                .mutateWith(mockUser("a").password("a").roles("1")
                        .authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("1"))
                )
                .get()
                .uri("/test/1")
                //.body(BodyInserters.fromObject(mo))
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody(AppUser.class)
                .consumeWith(EntityExchangeResult::getResponseBody)
                .returnResult()
                .getResponseBody();
        assertEquals(1L, response.getId());
        assertEquals("a", response.getEmail());
    }

    @Test
    void processMsg() {

        when(securityService.findByUsername(anyString()))
                .thenReturn(Mono.just(new User("a", "a",
                        AuthorityUtils.commaSeparatedStringToAuthorityList("1"))));
        lenient().when(db.find(Mockito.anyLong()))
                .thenReturn(Mono.just(AppUser.builder()
                        .id(1L)
                        .email("a")
                        .password("a")
                        .roleId(1)
                        .build()));

        Message response = webClient
                .mutateWith(mockUser("a").password("a").roles("1")
                        .authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("1"))
                )
                .post()
                .uri("/msg")
                .body(BodyInserters.fromObject(
                        Message.builder()
                                .id("1")
                                .text("TEST")
                                .build()
                ))
                //.body(BodyInserters.fromObject(mo))
                .exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBody(Message.class)
                .consumeWith(EntityExchangeResult::getResponseBody)
                .returnResult()
                .getResponseBody();
        assertEquals("1", response.getId());
        assertEquals("TEST was read", response.getText());
    }


}