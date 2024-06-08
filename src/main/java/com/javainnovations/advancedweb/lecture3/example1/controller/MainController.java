package com.javainnovations.advancedweb.lecture3.example1.controller;

import com.javainnovations.advancedweb.lecture3.example1.exception.BadRequestApplicationError;
import com.javainnovations.advancedweb.lecture3.example1.model.core.Message;
import com.javainnovations.advancedweb.lecture3.example1.model.dto.AppUser;
import com.javainnovations.advancedweb.lecture3.example1.repository.AppUserRepo;
import com.javainnovations.advancedweb.lecture3.example1.service.template.DatabaseTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class MainController {

    @Autowired
    DatabaseTemplate db;

    @GetMapping("/")
    public String root(){
        return "HI";
    }

    @GetMapping("/test/{id}")
    public Mono<AppUser> getUser(@PathVariable("id") long id)
    {
        return db.find(id);
    }

    @GetMapping("/test2/{email}")
    public Mono<AppUser> getByEmail(@PathVariable("email") String email)
    {
        return db.findByEmail(email);
    }

    @GetMapping("/test3/{name}")
    public Mono<List<AppUser>> getByName(@PathVariable("name") String namePart)
    {
        return db.findByName(namePart);
    }

    @PostMapping("/msg")
    public Mono<Message> processMsg(@RequestBody(required = false) Message msg)
            throws BadRequestApplicationError
    {

        try {
            System.out.println("processMsg "+msg);
            return Mono.just(Message.builder()
                            .id(msg.getId())
                            .text(msg.getText()+" was read")
                    .build());
        }
        catch (Exception ex)
        {
            System.out.println("EX2");
            throw new BadRequestApplicationError(ex.getMessage());
        }
    }

    @GetMapping("/msgPath/{msg}/{id}")
    public Mono<Message> processPathMsg(@PathVariable("msg") String msg, @PathVariable("id") String id)
            throws BadRequestApplicationError
    {
        try {
            System.out.println("processMsg "+msg);
            return Mono.just(Message.builder()
                    .id(id)
                    .text(msg+" was read")
                    .build());
        }
        catch (Exception ex)
        {
            System.out.println("EX2");
            throw new BadRequestApplicationError(ex.getMessage());
        }
    }


    @PostMapping("/msgForm")
    public Mono<Message>processParamMsg(@RequestParam String msg, @RequestParam String id)
            throws BadRequestApplicationError
    {
        try {
            System.out.println("processMsg "+msg);
            return Mono.just(Message.builder()
                    .id(id)
                    .text(msg+" was read successfully")
                    .build());
        }
        catch (Exception ex)
        {
            System.out.println("EX2");
            throw new BadRequestApplicationError(ex.getMessage());
        }
    }

    @PostMapping("/msgHeader")
    public Mono<Message> processHeaderMsg(
            @RequestHeader(value = "msg", required = false) String msg,
            @RequestHeader(value = "id", required = false) String id)
            throws BadRequestApplicationError
    {
        try {
            System.out.println("processMsg "+msg);
            return Mono.just(Message.builder()
                    .id(id)
                    .text(msg+" was read")
                    .build());
        }
        catch (Exception ex)
        {
            System.out.println("EX2");
            throw new BadRequestApplicationError(ex.getMessage());
        }
    }

}
