package com.finalproject.airport.webSocket.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.cloud.Timestamp;
import lombok.*;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;


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

