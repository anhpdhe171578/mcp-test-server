package com.mcp.testserver.repository;

import com.mcp.testserver.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    
    List<Document> findByType(Document.DocumentType type);
    
    Optional<Document> findByTitle(String title);
    
    List<Document> findBySourceUrlContaining(String sourceUrl);
}
