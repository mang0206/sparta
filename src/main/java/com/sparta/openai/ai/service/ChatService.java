package com.sparta.openai.ai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.openai.ai.controller.dto.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final MessageConverter messageConverter;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<String, ChatClient> allChatClients; // Set<ChatClient> -> Spring이 자동으로 ChatClient Type Bean Set 주입해줘요.
    private Map<String, ChatClient> chatClientMap = new HashMap<>();
    private final Map<String, List<Message>> conversations = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        chatClientMap = allChatClients.entrySet().stream()
                .collect(toMap(
                        element -> element.getKey().split("ChatClient")[0],
                        Map.Entry::getValue
                ));
        log.info("=== Initialized chat clients: {} ===", chatClientMap.keySet());
    }

    public ModelsResponse models() {
        log.info("=== ModelsResponse: {} ===", chatClientMap.keySet());
        return ModelsResponse.of(chatClientMap.keySet());
    }

    private ChatClient getChatClient(String model) {
        // 1. 요청된 모델 이름으로 찾기
        if (model != null && chatClientMap.containsKey(model)) {
            return chatClientMap.get(model);
        }

        // 2. 모델 이름에 "gpt"가 포함되어 있거나 매핑되지 않은 경우 기본값으로 첫 번째 available client 사용
        return chatClientMap.values().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No ChatClient available"));
    }

    public ChatResponse chatSync(ChatRequest chatRequest) {
        // 1. Message DTO → Spring AI Message로 변환
        ChatClient chatClient = getChatClient(chatRequest.model());

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

        log.info("=== content: {} ===", content);
        log.info("=== totalTokens: {} ===", usage.totalTokens());

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

