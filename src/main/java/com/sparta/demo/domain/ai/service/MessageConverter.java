package com.sparta.demo.domain.ai.service;

import com.sparta.demo.domain.ai.dto.ChatMessage;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageConverter {

    /**
     * OpenAI 형식의 메시지를 Spring AI Message 객체로 변환
     */
    public List<Message> convertMessages(List<ChatMessage> chatMessages) {
        return chatMessages.stream()
                .map(this::convertToSpringAI)
                .collect(Collectors.toList());
    }

    /**
     * 개별 ChatMessage를 Spring AI Message로 변환
     */
    private Message convertToSpringAI(ChatMessage chatMessage) {
        String role = chatMessage.getRole();
        String content = chatMessage.getContent();

        return switch (role.toLowerCase()) {
            case "system" -> new SystemMessage(content);
            case "user" -> new UserMessage(content);
            case "assistant" -> new AssistantMessage(content);
            default -> throw new IllegalArgumentException("Unsupported role: " + role);
        };
    }

    /**
     * Spring AI Message를 OpenAI 형식의 ChatMessage로 역변환
     */
    public ChatMessage convertFromSpringAI(Message message) {
        String role;
        if (message instanceof SystemMessage) {
            role = "system";
        } else if (message instanceof UserMessage) {
            role = "user";
        } else if (message instanceof AssistantMessage) {
            role = "assistant";
        } else {
            role = "assistant"; // 기본값
        }

        return ChatMessage.builder()
                .role(role)
                .content(message.getContent())
                .build();
    }
}
