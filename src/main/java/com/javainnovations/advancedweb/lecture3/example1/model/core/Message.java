package com.javainnovations.advancedweb.lecture3.example1.model.core;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    private String id;
    private String text;
}
