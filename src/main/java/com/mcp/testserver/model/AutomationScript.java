package com.mcp.testserver.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "automation_scripts")
public class AutomationScript {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String scriptName;
    
    @Column(nullable = false)
    private String scriptContent;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScriptType scriptType;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
    
    private String language;
    private String framework;
    private String tags;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_case_id")
    private TestCase testCase;
    
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
    
    public enum ScriptType {
        ROBOT_FRAMEWORK("Robot Framework"),
        SELENIUM("Selenium"),
        REST_ASSURED("Rest Assured"),
        CYPRESS("Cypress"),
        PLAYWRIGHT("Playwright"),
        JMETER("JMeter"),
        POSTMAN("Postman");
        
        private final String description;
        
        ScriptType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    public enum Status {
        DRAFT("Draft"),
        READY("Ready"),
        IN_REVIEW("In Review"),
        APPROVED("Approved"),
        DEPRECATED("Deprecated");
        
        private final String description;
        
        Status(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
}
