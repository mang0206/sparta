package com.sparta.openai.ai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.openai.ai.controller.dto.ChatChunk;
import com.sparta.openai.ai.controller.dto.ChatRequest;
import com.sparta.openai.ai.controller.dto.ChatResponse;
import com.sparta.openai.ai.controller.dto.Usage;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

@Service
public class ChatService {
    private final ChatClient chatClient;
    private final MessageConverter messageConverter;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChatService(ChatClient chatClient, MessageConverter messageConverter) {
        this.chatClient = chatClient;
        this.messageConverter = messageConverter;
    }

    public ChatResponse chatSync(ChatRequest chatRequest) {
        // 1. Message DTO → Spring AI Message로 변환
        //    (MessageConverter 안에서 role(user/system/assistant)에 따라 분기)
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

