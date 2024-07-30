package com.finalproject.airport.webSocket.chat.dto;



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
    private String timestamp;
}

