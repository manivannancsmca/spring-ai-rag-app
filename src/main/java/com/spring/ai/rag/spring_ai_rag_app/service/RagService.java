package com.spring.ai.rag.spring_ai_rag_app.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import com.spring.ai.rag.spring_ai_rag_app.dto.QuestionResponse;

import java.util.List;

@Service
public class RagService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public RagService(
            ChatClient chatClient,
            VectorStore vectorStore
    ) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    public QuestionResponse ask(String question) {
        SearchRequest searchRequest = SearchRequest.builder()
                .query(question)
                .topK(5)
                .similarityThreshold(0.70)
                .filterExpression("documentType == 'HR_POLICY'")
                .build();

        List<Document> retrievedDocuments =
                vectorStore.similaritySearch(searchRequest);

        if (retrievedDocuments == null || retrievedDocuments.isEmpty()) {
            return new QuestionResponse(
                    question,
                    "I could not find this information in the HR Policy.",
                    0,
                    List.of()
            );
        }

        String answer = chatClient
                .prompt()
                .user(question)
                .call()
                .content();

        List<QuestionResponse.SourceChunk> sources =
                retrievedDocuments.stream()
                        .map(document -> new QuestionResponse.SourceChunk(
                                document.getText(),
                                document.getMetadata()
                        ))
                        .toList();

        return new QuestionResponse(
                question,
                answer,
                retrievedDocuments.size(),
                sources
        );
    }
}
