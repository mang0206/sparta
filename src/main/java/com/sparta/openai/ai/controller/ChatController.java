package com.sparta.openai.ai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.openai.ai.controller.dto.ChatRequest;
import com.sparta.openai.ai.controller.dto.ChatResponse;
import com.sparta.openai.ai.controller.dto.ModelsResponse;
import com.sparta.openai.ai.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.model.ModelResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
@RequestMapping("/v1")
@Slf4j
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final ObjectMapper objectMapper;

    @GetMapping("/models")
    public ResponseEntity<ModelsResponse> getCahtModels() {
        log.info("📢 /v2/models Open WebUI 모델 목록 요청: model");
        return ResponseEntity.ok(chatService.models());
    }

    @PostMapping("/chat/completions")
    public ResponseEntity<?> chatCompletions(@RequestBody ChatRequest request) {
        log.info("Chat completions request: {}", request);
        if (Boolean.TRUE.equals(request.stream())) {
            Flux<ServerSentEvent<String>> eventStream = chatService.chatStream(request)
                    .map(data -> ServerSentEvent.builder(data).build());

            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_EVENT_STREAM)
                    .body(eventStream);
        }
        ChatResponse response = chatService.chatSync(request);
        return ResponseEntity.ok(response);
    }

    /**
     * [수정됨] ChatRequest 대신 Map으로 받아서 일단 로그부터 찍어봅니다.
     */
    /*
    @PostMapping("/chat/completions")
    public ResponseEntity<Flux<String>> chatCompletions(@RequestBody Map<String, Object> rawRequest) {
        log.info("📢 [1] Open WebUI Raw Request 도착: {}", rawRequest);

        // 1. Map을 ChatRequest DTO로 수동 변환 (여기서 에러나면 로그로 확인 가능)
        ChatRequest request;
        try {
            request = objectMapper.convertValue(rawRequest, ChatRequest.class);
            log.info("✅ [2] DTO 변환 성공: model={}, stream={}", request.model(), request.stream());
        } catch (IllegalArgumentException e) {
            log.error("❌ [ERROR] DTO 변환 실패! 필드 불일치: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        // 2. 스트리밍 처리
        if (Boolean.TRUE.equals(request.stream())) {
            Flux<String> stream = chatService.chatStream(request)
                    .map(chunkJson -> "data: " + chunkJson + "\n\n");

            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_EVENT_STREAM)
                    .body(stream);
        }

        // 3. 일반 처리
        ChatResponse response = chatService.chatSync(request);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Flux.just(response.toString()));
    }*/
}
