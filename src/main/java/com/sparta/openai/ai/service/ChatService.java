package com.sparta.openai.ai.service;

import com.sparta.openai.ai.controller.dto.ChatRequest;
import com.sparta.openai.ai.controller.dto.ChatResponse;
import com.sparta.openai.ai.controller.dto.Usage;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.UUID;
import java.util.List;

@Service
public class ChatService {
    private final ChatClient chatClient;
    private final MessageConverter messageConverter;

    public ChatService(ChatClient chatClient, MessageConverter messageConverter) {
        this.chatClient = chatClient;
        this.messageConverter = messageConverter;
    }

    public ChatResponse chat(ChatRequest request) {
        List<Message> messages = messageConverter.convertToSpringAI(request.messages());

        var aiResponse = chatClient.prompt()
                .messages(messages)
                .call();

        String content = aiResponse.content();
        return ChatResponse.of(request.model(), content);
    }
}

