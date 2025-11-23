package com.sparta.openai.ai.service;

import com.sparta.openai.ai.controller.dto.Message;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageConverter {
    /**
     * OpenAI 형식 메시지를 Spring AI Message로 변환
     */
    public List<org.springframework.ai.chat.messages.Message> toSpringMessages(
            List<Message> messages) {

        return messages.stream()
                .map(msg -> {
                    switch (msg.role().toLowerCase()) {
                        case "system":
                            return new SystemMessage(msg.content());
                        case "user":
                            return new UserMessage(msg.content());
                        case "assistant":
                            return new AssistantMessage(msg.content());
                        default:
                            throw new IllegalArgumentException("Unknown role: " + msg.role());
                    }
                })
                .collect(Collectors.toList());
    }

    private org.springframework.ai.chat.messages.Message toSpringMessage(Message m) {
        return switch (m.role()) {
            case "system" -> new SystemMessage(m.content());
            case "assistant" -> new AssistantMessage(m.content());
            default -> new UserMessage(m.content()); // 기본 user
        };
    }
}
