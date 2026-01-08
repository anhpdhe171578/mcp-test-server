package com.mcp.testserver.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "documents")
public class Document {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String content;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentType type;
    
    private String sourceUrl;
    
    @Column(length = 500)
    private String description;
    
    // Remove circular references to avoid JPA mapping issues
    // @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<TestCase> testCases;
    
    // @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<Requirement> requirements;
    
    @Builder.Default
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Builder.Default
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum DocumentType {
        SRS("Software Requirement Specification"),
        PRD("Product Requirement Document"),
        FIGMA("Figma Design"),
        USER_STORY("User Story"),
        TECHNICAL_SPEC("Technical Specification");
        
        private final String description;
        
        DocumentType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
}
