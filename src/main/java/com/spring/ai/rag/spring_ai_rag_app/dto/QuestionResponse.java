package com.spring.ai.rag.spring_ai_rag_app.dto;

import java.util.List;

public record QuestionResponse(
        String question,
        String answer,
        int retrievedChunks,
        List<SourceChunk> sources
) {
    public record SourceChunk(
            String content,
            Object metadata
    ) {
    }
}
