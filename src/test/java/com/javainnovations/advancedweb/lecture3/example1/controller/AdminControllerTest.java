package com.javainnovations.advancedweb.lecture3.example1.controller;

import com.javainnovations.advancedweb.lecture3.example1.Example1Application;
import com.javainnovations.advancedweb.lecture3.example1.config.SecurityConfig;
import com.javainnovations.advancedweb.lecture3.example1.model.dto.AppUser;
import com.javainnovations.advancedweb.lecture3.example1.repository.AppUserRepo;
import com.javainnovations.advancedweb.lecture3.example1.service.security.CustomReactiveUserDetailsService;
import com.javainnovations.advancedweb.lecture3.example1.service.template.DatabaseTemplate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockUser;

@WebFluxTest(controllers = AdminController.class)

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = { Example1Application.class })
@Import(SecurityConfig.class)
class AdminControllerTest {

    @Autowired
    WebTestClient webClient;

    @MockBean
    DatabaseTemplate db;

    @MockBean
    AppUserRepo repo;

    @MockBean
    CustomReactiveUserDetailsService securityService;


    @Test
    void admin() {

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

        String response = webClient
                .mutateWith(mockUser("a").password("a").roles("1")
                        .authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("1"))
                )
                .get()
                .uri("/admin/")
                //.body(BodyInserters.fromObject(mo))
                .exchange().expectStatus().isOk()
                .expectHeader().contentType("text/plain;charset=UTF-8")
                .expectBody(String.class)
                .consumeWith(EntityExchangeResult::getResponseBody)
                .returnResult()
                .getResponseBody();
        assertNotNull(response);
        assertTrue(response.contains("Admin Root"));
        System.out.println(response);
    }

    @Test
    void badAdmin() {

//        when(securityService.findByUsername(anyString()))
//                .thenReturn(Mono.just(new User("c", "c",
//                        AuthorityUtils.commaSeparatedStringToAuthorityList("3"))));
//        lenient().when(db.find(Mockito.anyLong()))
//                .thenReturn(Mono.just(AppUser.builder()
//                        .id(1L)
//                        .email("c")
//                        .password("c").roleId(4)
//                        .build()));

        webClient
                .mutateWith(mockUser("d").password("d").roles("5")
                        .authorities(AuthorityUtils.commaSeparatedStringToAuthorityList("5"))
                )
                .get()
                .uri("/admin/")
                //.body(BodyInserters.fromObject(mo))
                .exchange().expectStatus()
                .isForbidden();


    }
}