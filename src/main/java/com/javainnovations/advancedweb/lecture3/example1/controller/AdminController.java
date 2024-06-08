package com.javainnovations.advancedweb.lecture3.example1.controller;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("/")
    public Mono<String> adminRoot() {
        Mono<String> message =
                ReactiveSecurityContextHolder.getContext()

                        .map(ctx -> ctx.getAuthentication())

                        .map(auth -> "Admin Root " + auth.getName()+" P "+auth.getPrincipal()+" C "+auth.getCredentials()
                                +" D "+auth.getDetails()+" Roles "+
                                ((User)auth.getPrincipal()).getAuthorities()
                        );
        return message;
    }
}
