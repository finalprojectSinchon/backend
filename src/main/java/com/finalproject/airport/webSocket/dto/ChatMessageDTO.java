package com.finalproject.airport.webSocket.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ChatMessageDTO {
    private String from;
    private String to;
    private String message;
}
