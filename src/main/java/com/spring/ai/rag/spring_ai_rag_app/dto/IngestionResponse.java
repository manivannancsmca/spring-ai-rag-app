package com.spring.ai.rag.spring_ai_rag_app.dto;

public record IngestionResponse(
        String documentName,
        int chunksStored,
        String message
) {
}
