package com.sparta.openai.ai.service;

import com.sparta.openai.ai.controller.dto.ChatRequest;
import com.sparta.openai.ai.controller.dto.ChatResponse;
import com.sparta.openai.ai.controller.dto.Usage;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {
    private final ChatClient chatClient;

    public ChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public ChatResponse chat(ChatRequest request) {
        List<Message> messages =
                messageConverter.convertToSpringAI(request.getMessages());
    }
}

