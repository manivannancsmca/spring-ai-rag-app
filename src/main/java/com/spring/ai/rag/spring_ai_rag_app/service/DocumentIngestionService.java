package com.spring.ai.rag.spring_ai_rag_app.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.spring.ai.rag.spring_ai_rag_app.dto.IngestionResponse;

import java.util.List;

@Service
public class DocumentIngestionService {

    private static final String DOCUMENT_PATH = "documents/hr-policy.txt";

    private final VectorStore vectorStore;

    public DocumentIngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public IngestionResponse ingestHrPolicy() {
        ClassPathResource resource = new ClassPathResource(DOCUMENT_PATH);

        TextReader textReader = new TextReader(resource);
        textReader.getCustomMetadata()
                .put("documentName", "hr-policy.txt");
        textReader.getCustomMetadata()
                .put("documentType", "HR_POLICY");
        textReader.getCustomMetadata()
                .put("source", "classpath");

        List<Document> documents = textReader.get();

        TokenTextSplitter splitter = TokenTextSplitter.builder()
                .withChunkSize(800)
                .withMinChunkSizeChars(200)
                .withMinChunkLengthToEmbed(50)
                .withMaxNumChunks(1000)
                .withKeepSeparator(true)
                .build();

        List<Document> chunks = splitter.apply(documents);

        chunks.forEach(chunk -> {
            chunk.getMetadata().put("documentName", "hr-policy.txt");
            chunk.getMetadata().put("documentType", "HR_POLICY");
            chunk.getMetadata().put("version", "1.0");
        });

        vectorStore.add(chunks);

        return new IngestionResponse(
                "hr-policy.txt",
                chunks.size(),
                "HR Policy document was successfully embedded and stored"
        );
    }
}
