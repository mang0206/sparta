package com.sparta.demo.domain.ai.controller;

import com.sparta.demo.domain.ai.dto.ChatCompletionRequest;
import com.sparta.demo.domain.ai.dto.ChatCompletionResponse;
import com.sparta.demo.domain.ai.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/v1/chat")
@RequiredArgsConstructor
@Tag(name = "Chat Completions", description = "OpenAI 호환 Chat Completions API")
public class ChatCompletionsController {

    private final ChatService chatService;

    @PostMapping("/completions")
    @Operation(summary = "채팅 완성 API", description = "사용자 메시지에 대한 AI 응답을 생성합니다. stream=true인 경우 SSE로 스트리밍됩니다.")
    public ResponseEntity<?> chatCompletions(@RequestBody ChatCompletionRequest request) {
        log.info("Chat completion request received: model={}, stream={}", request.getModel(), request.getStream());

        // 스트리밍 모드 여부 확인
        boolean isStream = request.getStream() != null && request.getStream();

        if (isStream) {
            // 스트리밍 응답
            Flux<String> streamResponse = chatService.chatStream(request);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_EVENT_STREAM)
                    .body(streamResponse);
        } else {
            // 동기 응답
            ChatCompletionResponse response = chatService.chatSync(request);
            return ResponseEntity.ok(response);
        }
    }
}
