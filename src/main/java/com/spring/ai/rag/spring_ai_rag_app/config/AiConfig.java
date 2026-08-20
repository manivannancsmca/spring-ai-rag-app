package com.spring.ai.rag.spring_ai_rag_app.config;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem("""
                        You are an HR policy assistant.

                        Use only the HR Policy Context provided by the application.
                        If the answer is not present in the context, reply:
                        "I could not find this information in the HR Policy."

                        Do not invent facts or policies.
                        """)
                .build();
    }
}
