package com.finalproject.airport.webSocket.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.airport.member.repository.UserRepository;
import com.finalproject.airport.webSocket.chat.dto.ChatMessageDTO;
import com.finalproject.airport.webSocket.chat.service.ChatService;
import com.google.cloud.Timestamp;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ChatService chatService;
    private final UserRepository userRepository;

    public WebSocketHandler(ChatService chatService, UserRepository userRepository) {
        this.chatService = chatService;
        this.userRepository = userRepository;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        URI uri = session.getUri();
        if (uri != null) {
            String query = uri.getQuery();
            if (query != null) {
                Map<String, String> queryParams = parseQueryParams(query);
                String userCode = queryParams.get("userCode");
                System.out.println("userCode = " + userCode);
                if (userCode != null) {
                    userSessions.put(userCode, session);
                    System.out.println("새로운 세션 연결 성공: " + userCode);

                    // 사용자 온라인 상태를 모든 클라이언트에게 알림
                    sendUserStatusUpdate(userCode, "online");
                    return;
                }
            }
        }
        System.err.println("쿼리스트링으로 유저코드 담아야함!!");
        session.close();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        URI uri = session.getUri();
        if (uri != null) {
            String query = uri.getQuery();
            if (query != null) {
                Map<String, String> queryParams = parseQueryParams(query);
                String userCode = queryParams.get("userCode");
                if (userCode != null) {
                    userSessions.remove(userCode);
                    System.out.println("커넥션 종료 : " + userCode);

                    // 사용자 오프라인 상태를 모든 클라이언트에게 알림
                    sendUserStatusUpdate(userCode, "offline");
                }
            }
        }
    }

    private Map<String, String> parseQueryParams(String query) {
        return List.of(query.split("&")).stream()
                .map(param -> param.split("="))
                .collect(Collectors.toMap(pair -> pair[0], pair -> pair[1]));
    }

    private void sendUserStatusUpdate(String userCode, String status) {
        Map<String, String> userStatusMap = new HashMap<>();
        userStatusMap.put(userCode, status);

        sendBatchUserStatusUpdate(userStatusMap);
    }

    private void sendBatchUserStatusUpdate(Map<String, String> userStatusMap) {
        Map<String, Object> statusUpdate = new HashMap<>();
        statusUpdate.put("type", "USER_STATUS_UPDATE");
        statusUpdate.put("statusUpdates", userStatusMap.entrySet().stream()
                .map(entry -> {
                    Map<String, String> update = new HashMap<>();
                    update.put("userCode", entry.getKey());
                    update.put("status", entry.getValue());
                    return update;
                })
                .collect(Collectors.toList())
        );

        String jsonMessage = null;
        try {
            jsonMessage = objectMapper.writeValueAsString(statusUpdate);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        for (WebSocketSession session : userSessions.values()) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(jsonMessage));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
