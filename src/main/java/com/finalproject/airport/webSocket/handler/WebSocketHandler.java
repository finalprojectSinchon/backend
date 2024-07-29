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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    // ConcurrentHashMap  멀티 쓰레드 환경에 좋음
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ChatService chatService;
    private final UserRepository userRepository;

    public WebSocketHandler(ChatService chatService, UserRepository userRepository){
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
                    return;
                }
            }
        }
        System.err.println("쿼리스트링으로 유저코드 담아야함!!");
        session.close();
    }

    @Scheduled(fixedRate = 30000) // 30초마다 실행
    public void sendPingMessages() {
        for (WebSocketSession session : userSessions.values()) {
            try {
                if (session.isOpen()) {
                    PingMessage pingMessage = new PingMessage(ByteBuffer.wrap("Ping".getBytes()));
                    session.sendMessage(pingMessage);
                }
            } catch (IOException e) {
                System.err.println("Error sending ping message: " + e.getMessage());
                userSessions.remove(session.getId());
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Received message: " + message.getPayload());
        ChatMessageDTO messageDTO = objectMapper.readValue(message.getPayload(), ChatMessageDTO.class);

        // 현재 시간을 LocalDateTime으로 설정
        messageDTO.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        // 수신자에게 메시지 전송 (JSON 포맷으로 전송)
        String to = messageDTO.getTo();
        WebSocketSession sessionTo = userSessions.get(to);
        if (sessionTo != null) {
            String jsonMessage = objectMapper.writeValueAsString(messageDTO);
            sessionTo.sendMessage(new TextMessage(jsonMessage));
        }

        // 메시지 저장
        chatService.saveMessage(messageDTO);
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
                }
            }
        }
    }

    private Map<String, String> parseQueryParams(String query) {
        return List.of(query.split("&")).stream()
                .map(param -> param.split("="))
                .collect(Collectors.toMap(pair -> pair[0], pair -> pair[1]));
    }
}
