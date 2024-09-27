package com.example.chattest.interceptor;

import com.mongodb.lang.NonNull;
import com.mongodb.lang.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Slf4j
@Component
public class CustomHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(@NonNull ServerHttpRequest request,@NonNull ServerHttpResponse response,
                                   @NonNull WebSocketHandler wsHandler,@NonNull Map<String, Object> attributes) throws Exception {
        // 使用 UriComponentsBuilder 從 URI 中解析查詢參數
        Map<String, String> queryParams = UriComponentsBuilder.fromUri(request.getURI()).build().getQueryParams().toSingleValueMap();

        // 將 userId 和 roomName 傳遞到 WebSocketSession 的 attributes 中
        String userId = queryParams.get("userId");
        String roomName = queryParams.get("roomName");

        if (userId != null && roomName != null) {
            attributes.put("userId", userId);
            attributes.put("roomName", roomName);
            log.info("WebSocket 連線參數: userId = {}, roomName = {}", userId, roomName);
            return true;  // 確認允許握手繼續
        }

        log.warn("缺少必要的查詢參數: userId 或 roomName");
        return false;  // 如果缺少參數則拒絕握手
    }

    @Override
    public void afterHandshake(@NonNull ServerHttpRequest request,@NonNull ServerHttpResponse response,
                               @NonNull WebSocketHandler wsHandler, @Nullable Exception ex) {
        // 握手完成後的操作，可以留空，如果不需要做額外處理
    }
}

