package com.finalproject.airport.webSocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.airport.webSocket.dto.ChatMessageDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

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



    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("전송한 메시지 : " + message.getPayload());
        ChatMessageDTO messageDTO = objectMapper.readValue(message.getPayload(), ChatMessageDTO.class); // JSON 파싱
        String to = messageDTO.getTo(); // 누구에게 보낼꺼니

        // 수신자에게 메시지 전송
        WebSocketSession sessionTo = userSessions.get(to);
        if (sessionTo != null) {
            sessionTo.sendMessage(new TextMessage(messageDTO.getMessage()));
        }
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
