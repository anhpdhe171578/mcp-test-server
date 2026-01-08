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
@Table(name = "requirements")
public class Requirement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String requirementId;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
    
    private String acceptanceCriteria;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private Document document;
    
    // Remove circular reference to avoid JPA mapping issues
    // @OneToMany(mappedBy = "requirement", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<TestCase> testCases;
    
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
    
    public enum Type {
        FUNCTIONAL("Functional"),
        NON_FUNCTIONAL("Non-Functional"),
        BUSINESS("Business"),
        TECHNICAL("Technical"),
        USER_INTERFACE("User Interface"),
        SECURITY("Security"),
        PERFORMANCE("Performance");
        
        private final String description;
        
        Type(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    public enum Priority {
        MUST_HAVE("Must Have"),
        SHOULD_HAVE("Should Have"),
        COULD_HAVE("Could Have"),
        WONT_HAVE("Won't Have");
        
        private final String description;
        
        Priority(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    public enum Status {
        DRAFT("Draft"),
        APPROVED("Approved"),
        IN_PROGRESS("In Progress"),
        COMPLETED("Completed"),
        CANCELLED("Cancelled");
        
        private final String description;
        
        Status(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
}
