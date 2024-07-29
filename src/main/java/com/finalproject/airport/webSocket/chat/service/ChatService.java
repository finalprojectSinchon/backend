package com.finalproject.airport.webSocket.chat.service;

import com.finalproject.airport.webSocket.chat.dto.ChatMessageDTO;
import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final Firestore firestore;

    public void saveMessage(ChatMessageDTO chatMessageDTO) {
        Map<String, Object> chatData = new HashMap<>();
        chatData.put("from", chatMessageDTO.getFrom());
        chatData.put("to", chatMessageDTO.getTo());
        chatData.put("message", chatMessageDTO.getMessage());
        chatData.put("timestamp", chatMessageDTO.getTimestamp());

        firestore.collection("messages")
                .document(chatMessageDTO.getFrom() + "_" + chatMessageDTO.getTo())
                .collection("chats")
                .add(chatData);
    }

}
