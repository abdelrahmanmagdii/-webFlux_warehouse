package com.javainnovations.advancedweb.lecture3.example1.exception;

public class BadRequestApplicationError extends RuntimeException{
    public BadRequestApplicationError(String msg){
        super(msg);
    }
}
