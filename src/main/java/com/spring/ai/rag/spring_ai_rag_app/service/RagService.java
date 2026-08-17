package com.spring.ai.rag.spring_ai_rag_app.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import com.spring.ai.rag.spring_ai_rag_app.dto.QuestionResponse;

import java.util.List;
import java.util.stream.Collectors;

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

        List<Document> documents =
                vectorStore.similaritySearch(searchRequest);

        if (documents == null || documents.isEmpty()) {
            return new QuestionResponse(
                    question,
                    "I could not find this information in the HR Policy.",
                    0,
                    List.of()
            );
        }

        String context = documents.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n---\n\n"));

        String prompt = """
                Answer the question using only the context below.

                If the answer cannot be found in the context, say:
                "I could not find this information in the HR Policy."

                HR Policy Context:
                %s

                User Question:
                %s
                """.formatted(context, question);

        String answer = chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();

        List<QuestionResponse.SourceChunk> sources =
                documents.stream()
                        .map(document -> new QuestionResponse.SourceChunk(
                                document.getText(),
                                document.getMetadata()
                        ))
                        .toList();

        return new QuestionResponse(
                question,
                answer,
                documents.size(),
                sources
        );
    }
}