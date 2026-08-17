package com.spring.ai.rag.spring_ai_rag_app.config;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    ChatClient chatClient(
            ChatClient.Builder chatClientBuilder,
            VectorStore vectorStore
    ) {
        QuestionAnswerAdvisor questionAnswerAdvisor =
                QuestionAnswerAdvisor.builder(vectorStore)
                        .searchTopK(5)
                        .build();

        return chatClientBuilder
                .defaultSystem("""
                        You are an HR policy assistant.

                        Answer questions only using the supplied HR Policy context.
                        If the answer is not present in the context, say:
                        "I could not find this information in the HR Policy."

                        Do not invent policies, dates, percentages, benefits,
                        approval rules, or legal advice.

                        Answer clearly and concisely.
                        """)
                .defaultAdvisors(questionAnswerAdvisor)
                .build();
    }
}