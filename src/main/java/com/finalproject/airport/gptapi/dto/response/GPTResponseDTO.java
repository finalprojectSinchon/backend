package com.finalproject.airport.gptapi.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GPTResponseDTO {
    private List<Choice> choices;

    @Getter
    @Setter
    public static class Choice {
        private Message message;


        @Getter
        @Setter
        public static class Message {
            private String role;
            private String content;


        }
    }
}
