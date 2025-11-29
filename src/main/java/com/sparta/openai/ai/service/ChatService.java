package com.sparta.openai.ai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.openai.ai.controller.dto.ChatChunk;
import com.sparta.openai.ai.controller.dto.ChatRequest;
import com.sparta.openai.ai.controller.dto.ChatResponse;
import com.sparta.openai.ai.controller.dto.Usage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    // 생성자 주입으로 모든 ChatClient 빈을 맵으로 받음
    private final Map<String, ChatClient> allChatClients;
    private final MessageConverter messageConverter;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 초기화 후 사용 가능한 모델 맵
    private Map<String, ChatClient> chatClientMap;

    @PostConstruct
    public void init() {
        chatClientMap = allChatClients.entrySet().stream()
                .collect(Collectors.toMap(
                        element -> element.getKey().split("ChatClient")[0], // 예: "ollamaChatClient" -> "ollama"
                        Map.Entry::getValue
                ));
        log.info("=== Initialized chat clients: {} ===", chatClientMap.keySet());
    }

    public Set<String> getAvailableModels() {
        return chatClientMap != null ? chatClientMap.keySet() : Collections.emptySet();
    }

    private ChatClient getChatClient(String model) {
        // 1. 요청된 모델 이름으로 찾기
        if (model != null && chatClientMap.containsKey(model)) {
            return chatClientMap.get(model);
        }

        // 2. 모델 이름에 "gpt"가 포함되어 있거나 매핑되지 않은 경우 기본값으로 첫 번째 available client 사용
        //    (실제 구현에서는 default 설정을 따로 두는 것이 좋음)
        return chatClientMap.values().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No ChatClient available"));
    }

    public ChatResponse chatSync(ChatRequest chatRequest) {
        ChatClient chatClient = getChatClient(chatRequest.model());

        // 1. Message DTO → Spring AI Message로 변환
        List<org.springframework.ai.chat.messages.Message> springMessages =
                messageConverter.toSpringMessages(chatRequest.messages());

        // 2. Spring AI ChatClient 호출
        String content = chatClient
                .prompt()
                .messages(springMessages)
                .call()
                .content();

        // 3. 토큰 사용량 계산
        int promptTokens = chatRequest.messages().stream()
                .mapToInt(m -> m.content() != null ? m.content().length() / 4 : 0)
                .sum();
        int completionTokens = content != null ? content.length() / 4 : 0;

        Usage usage = Usage.of(promptTokens, completionTokens);

        return ChatResponse.of(
                chatRequest.model(),
                content,
                usage
        );
    }

    public Flux<String> chatStream(ChatRequest chatRequest) {
        ChatClient chatClient = getChatClient(chatRequest.model());

        List<org.springframework.ai.chat.messages.Message> messages =
                messageConverter.toSpringMessages(chatRequest.messages());

        String id = "chatcmpl-" + UUID.randomUUID();
        long created = System.currentTimeMillis() / 1000;

        Flux<String> tokenFlux = chatClient.prompt()
                .messages(messages)
                .stream()
                .content();

        return Flux.concat(
                Flux.just(toJson(ChatChunk.first(id, created, chatRequest.model()))),
                tokenFlux.map(token -> toJson(ChatChunk.content(id, created, chatRequest.model(), token))),
                Flux.just(toJson(ChatChunk.stop(id, created, chatRequest.model())), "[DONE]")
        );
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize response", e);
        }
    }
}
