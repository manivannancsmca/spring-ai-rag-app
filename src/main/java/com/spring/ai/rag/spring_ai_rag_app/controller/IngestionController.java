package com.spring.ai.rag.spring_ai_rag_app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spring.ai.rag.spring_ai_rag_app.dto.IngestionResponse;
import com.spring.ai.rag.spring_ai_rag_app.service.DocumentIngestionService;

@RestController
@RequestMapping("/api/v1/ingestion")
public class IngestionController {

    private final DocumentIngestionService ingestionService;

    public IngestionController(DocumentIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping("/hr-policy")
    public ResponseEntity<IngestionResponse> ingestHrPolicy() {
        return ResponseEntity.ok(
                ingestionService.ingestHrPolicy()
        );
    }
}