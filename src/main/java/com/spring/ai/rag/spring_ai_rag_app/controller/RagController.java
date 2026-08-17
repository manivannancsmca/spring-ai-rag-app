package com.spring.ai.rag.spring_ai_rag_app.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spring.ai.rag.spring_ai_rag_app.dto.QuestionRequest;
import com.spring.ai.rag.spring_ai_rag_app.dto.QuestionResponse;
import com.spring.ai.rag.spring_ai_rag_app.service.RagService;

@RestController
@RequestMapping("/api/v1/rag")
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    @PostMapping("/ask")
    public ResponseEntity<QuestionResponse> ask(
            @Valid @RequestBody QuestionRequest request
    ) {
        return ResponseEntity.ok(
                ragService.ask(request.question())
        );
    }
}