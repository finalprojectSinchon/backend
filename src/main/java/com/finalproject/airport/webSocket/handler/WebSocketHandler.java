package com.finalproject.airport.webSocket.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.finalproject.airport.member.repository.UserRepository;
import com.finalproject.airport.webSocket.chat.dto.ChatMessageDTO;
import com.finalproject.airport.webSocket.chat.service.ChatService;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.SetOptions;
import lombok.extern.slf4j.Slf4j;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
@Slf4j
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
                if (userCode != null) {
                    userSessions.put(userCode, session);
                    log.info("New session connected: {}", userCode);

                    // 사용자 온라인 상태를 모든 클라이언트에게 알림
                    sendUserStatusUpdate(userCode, "online");

                    // 연결된 클라이언트에게 모든 사용자의 현재 상태 전송
                    sendAllUserStatuses(session);
                    return;
                }
            }
        }
        log.error("User code must be provided in query string!");
        session.close();
    }





    @Scheduled(fixedRate = 5000) // 5초마다 실행
    public void sendPingMessages() {
        for (WebSocketSession session : userSessions.values()) {
            try {
                if (session.isOpen()) {
                    PingMessage pingMessage = new PingMessage(ByteBuffer.wrap("Ping".getBytes()));
                    session.sendMessage(pingMessage);
                }
            } catch (IOException e) {
                log.error("Error sending ping message: {}", e.getMessage());
                userSessions.remove(session.getId());
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("Received message: {}", message.getPayload());
        JsonNode jsonMessage = objectMapper.readTree(message.getPayload());

        JsonNode typeNode = jsonMessage.get("type");
        if (typeNode == null) {
            return;
        }

        String messageType = typeNode.asText();

        if ("REQUEST_ALL_STATUSES".equals(messageType)) {
            sendAllUserStatuses(session);
        } else if ("CHAT_MESSAGE".equals(messageType)) { // 채팅 메시지 타입 처리
            ChatMessageDTO messageDTO = objectMapper.readValue(message.getPayload(), ChatMessageDTO.class);
            log.info("Handling CHAT_MESSAGE type");
            messageDTO.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            String to = messageDTO.getTo();
            WebSocketSession sessionTo = userSessions.get(to);
            if (sessionTo != null) {
                // 채팅 메시지에 타입 추가
                ((ObjectNode) jsonMessage).put("type", "CHAT_MESSAGE");
                String jsonMessageStr = objectMapper.writeValueAsString(jsonMessage);
                sessionTo.sendMessage(new TextMessage(jsonMessageStr));
            }
            // 메시지 저장
            chatService.saveMessage(messageDTO);
        } else if ("SOS_ALERT".equals(messageType)) {
            // 긴급 상황
            broadcastSOSAlert(jsonMessage);
        }
    }

    private void broadcastSOSAlert(JsonNode alertMessage) throws IOException {
        String jsonMessage = objectMapper.writeValueAsString(alertMessage);
        for (WebSocketSession session : userSessions.values()) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(jsonMessage));
            }
        }
        log.info("Broadcasted SOS alert to all users");
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
                    log.info("Connection closed: {}", userCode);

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

    private void sendUserStatusUpdate(String userCode, String status) throws JsonProcessingException {
        Map<String, String> allStatuses = new HashMap<>();
        for (Map.Entry<String, WebSocketSession> entry : userSessions.entrySet()) {
            allStatuses.put(entry.getKey(), "online");
        }
        allStatuses.put(userCode, status);  // 업데이트된 사용자 상태

        ObjectNode statusUpdate = objectMapper.createObjectNode();
        statusUpdate.put("type", "USER_STATUS_UPDATE");
        statusUpdate.set("statusUpdates", objectMapper.valueToTree(allStatuses));

        String jsonMessage = objectMapper.writeValueAsString(statusUpdate);
        for (WebSocketSession session : userSessions.values()) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(jsonMessage));
                } catch (IOException e) {
                    log.error("Error sending user status update: {}", e.getMessage());
                }
            }
        }
    }

    private void sendAllUserStatuses(WebSocketSession session) throws IOException {
        Map<String, String> allStatuses = new HashMap<>();
        for (Map.Entry<String, WebSocketSession> entry : userSessions.entrySet()) {
            allStatuses.put(entry.getKey(), "online");
        }

        ObjectNode response = objectMapper.createObjectNode();
        response.put("type", "INITIAL_STATUS");
        response.set("statuses", objectMapper.valueToTree(allStatuses));
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
        } catch (IOException e) {
            log.error("Error sending initial statuses: {}", e.getMessage());
        }
    }
}
