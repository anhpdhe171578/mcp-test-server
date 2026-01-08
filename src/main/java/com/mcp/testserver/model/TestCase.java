package com.mcp.testserver.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "test_cases")
public class TestCase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String testCaseId;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(columnDefinition = "TEXT")
    private String preconditions;
    
    @Column(columnDefinition = "TEXT")
    private String testSteps;
    
    @Column(columnDefinition = "TEXT")
    private String expectedResult;
    
    @Column(columnDefinition = "TEXT")
    private String actualResult;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TestType testType;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
    
    private String module;
    private String feature;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private Document document;
    
    // Remove circular reference to avoid JPA mapping issues
    // @OneToMany(mappedBy = "testCase", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<TestStep> steps;
    
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
    
    public enum TestType {
        FUNCTIONAL("Functional"),
        UI("UI/UX"),
        API("API"),
        INTEGRATION("Integration"),
        PERFORMANCE("Performance"),
        SECURITY("Security"),
        REGRESSION("Regression"),
        SMOKE("Smoke"),
        SANITY("Sanity");
        
        private final String description;
        
        TestType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    public enum Priority {
        HIGH("High"),
        MEDIUM("Medium"),
        LOW("Low");
        
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
        READY("Ready"),
        IN_PROGRESS("In Progress"),
        PASSED("Passed"),
        FAILED("Failed"),
        BLOCKED("Blocked"),
        SKIPPED("Skipped");
        
        private final String description;
        
        Status(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
}
