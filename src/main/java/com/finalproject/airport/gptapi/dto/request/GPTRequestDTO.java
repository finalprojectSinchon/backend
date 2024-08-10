package com.finalproject.airport.gptapi.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GPTRequestDTO {

    private String model;
    private List<Message> messages;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Message {

        private String role;

        private String content;


    }
}
