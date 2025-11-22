package com.sparta.demo.domain.ai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.demo.domain.ai.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final OllamaChatModel chatModel;
    private final MessageConverter messageConverter;
    private final ObjectMapper objectMapper;

    /**
     * 동기 방식 채팅 응답 생성
     */
    public ChatCompletionResponse chatSync(ChatCompletionRequest request) {
        // OpenAI 메시지를 Spring AI 메시지로 변환
        List<Message> messages = messageConverter.convertMessages(request.getMessages());

        // Ollama 옵션 설정
        OllamaOptions options = OllamaOptions.builder()
                .withModel(request.getModel() != null ? request.getModel() : "qwen3")
                .withTemperature(request.getTemperature() != null ? request.getTemperature() : 0.7)
                .build();

        // Prompt 생성 및 호출
        Prompt prompt = new Prompt(messages, options);
        ChatResponse response = chatModel.call(prompt);

        // 응답을 OpenAI 형식으로 변환
        return convertToOpenAIResponse(response, request.getModel());
    }

    /**
     * 스트리밍 방식 채팅 응답 생성
     */
    public Flux<String> chatStream(ChatCompletionRequest request) {
        // OpenAI 메시지를 Spring AI 메시지로 변환
        List<Message> messages = messageConverter.convertMessages(request.getMessages());

        // Ollama 옵션 설정
        OllamaOptions options = OllamaOptions.builder()
                .withModel(request.getModel() != null ? request.getModel() : "qwen3")
                .withTemperature(request.getTemperature() != null ? request.getTemperature() : 0.7)
                .build();

        // Prompt 생성
        Prompt prompt = new Prompt(messages, options);

        // 스트리밍 응답 생성
        String requestId = "chatcmpl-" + UUID.randomUUID().toString().substring(0, 8);
        long created = Instant.now().getEpochSecond();

        return chatModel.stream(prompt)
                .map(chatResponse -> convertToSSE(chatResponse, requestId, created, request.getModel()))
                .concatWith(Flux.just("data: [DONE]\n\n"));
    }

    /**
     * Spring AI ChatResponse를 OpenAI 형식으로 변환
     */
    private ChatCompletionResponse convertToOpenAIResponse(ChatResponse response, String model) {
        String requestId = "chatcmpl-" + UUID.randomUUID().toString().substring(0, 8);
        long created = Instant.now().getEpochSecond();

        // 응답 메시지 추출
        String content = response.getResult().getOutput().getContent();

        ChatMessage message = ChatMessage.builder()
                .role("assistant")
                .content(content)
                .build();

        ChatChoice choice = ChatChoice.builder()
                .index(0)
                .message(message)
                .finishReason("stop")
                .build();

        // 토큰 사용량 계산 (근사값)
        Usage usage = Usage.builder()
                .promptTokens(estimateTokens(response.getMetadata().toString()))
                .completionTokens(estimateTokens(content))
                .totalTokens(estimateTokens(response.getMetadata().toString()) + estimateTokens(content))
                .build();

        return ChatCompletionResponse.builder()
                .id(requestId)
                .object("chat.completion")
                .created(created)
                .model(model != null ? model : "qwen3")
                .choices(List.of(choice))
                .usage(usage)
                .build();
    }

    /**
     * SSE 형식으로 변환
     */
    private String convertToSSE(ChatResponse response, String requestId, long created, String model) {
        try {
            String content = response.getResult().getOutput().getContent();

            ChatDelta delta = ChatDelta.builder()
                    .content(content)
                    .build();

            ChatChoiceChunk choiceChunk = ChatChoiceChunk.builder()
                    .index(0)
                    .delta(delta)
                    .finishReason(null)
                    .build();

            ChatCompletionChunk chunk = ChatCompletionChunk.builder()
                    .id(requestId)
                    .object("chat.completion.chunk")
                    .created(created)
                    .model(model != null ? model : "qwen3")
                    .choices(List.of(choiceChunk))
                    .build();

            return "data: " + objectMapper.writeValueAsString(chunk) + "\n\n";
        } catch (JsonProcessingException e) {
            log.error("Error converting to SSE", e);
            return "";
        }
    }

    /**
     * 토큰 수 추정 (간단한 근사값)
     */
    private int estimateTokens(String text) {
        if (text == null) return 0;
        // 영어는 대략 4자당 1토큰, 한글은 2자당 1토큰으로 근사
        return (int) Math.ceil(text.length() / 3.0);
    }
}
