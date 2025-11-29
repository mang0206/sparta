package com.sparta.openai.ai.controller;

import com.sparta.openai.ai.controller.dto.ChatRequest;
import com.sparta.openai.ai.controller.dto.ChatResponse;
import com.sparta.openai.ai.controller.dto.ModelResponse;
import com.sparta.openai.ai.service.ChatService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/v1/models")
    public ResponseEntity<ModelResponse> getModels() {
        return ResponseEntity.ok(ModelResponse.builder()
                .object("list")
                .data(List.of(
                        ModelResponse.ModelData.builder()
                                .id("gpt-3.5-turbo")
                                .object("model")
                                .created(System.currentTimeMillis() / 1000)
                                .owned_by("openai")
                                .build(),
                        ModelResponse.ModelData.builder()
                                .id("gpt-4")
                                .object("model")
                                .created(System.currentTimeMillis() / 1000)
                                .owned_by("openai")
                                .build()
                ))
                .build());
    }

    @PostMapping(value = {"/v1/chat/completions", "/api/chat/completions"})
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
