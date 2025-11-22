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
    public List<org.springframework.ai.chat.messages.Message> convertToSpringAI(
            List<Message> openAIMessages) {

        return openAIMessages.stream()
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

    /**
     * Spring AI Message를 OpenAI 형식으로 변환
     */
    public Message convertFromSpringAI(org.springframework.ai.chat.messages.Message springMessage) {
        String role;
        if (springMessage instanceof SystemMessage) {
            role = "system";
        } else if (springMessage instanceof UserMessage) {
            role = "user";
        } else if (springMessage instanceof AssistantMessage) {
            role = "assistant";
        } else {
            role = "assistant";
        }

        return Message.builder()
                .role(role)
                //.content(springMessage.getContent())
                .content(springMessage.getText())
                .build();
    }

}
