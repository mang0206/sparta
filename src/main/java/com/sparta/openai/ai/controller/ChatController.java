package com.sparta.openai.ai.controller;

import com.sparta.openai.ai.controller.dto.ChatRequest;
import com.sparta.openai.ai.controller.dto.ChatResponse;
import com.sparta.openai.ai.controller.dto.ModelsResponse;
import com.sparta.openai.ai.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.model.ModelResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/v1")
@Slf4j
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/models")
    public ResponseEntity<ModelsResponse> getModels() {
        return ResponseEntity.ok(chatService.models());
    }

    @PostMapping("/chat/completions")
    public ResponseEntity<?> chatCompletions(@RequestBody ChatRequest request) {
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
}
