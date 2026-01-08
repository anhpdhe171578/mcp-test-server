package com.mcp.testserver.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "test_steps")
public class TestStep {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Integer stepNumber;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String action;
    
    @Column(columnDefinition = "TEXT")
    private String expected;
    
    @Column(columnDefinition = "TEXT")
    private String actual;
    
    @Enumerated(EnumType.STRING)
    private Status status;
    
    // Remove circular reference to avoid JPA mapping issues
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "test_case_id")
    // private TestCase testCase;
    
    public enum Status {
        NOT_EXECUTED("Not Executed"),
        PASSED("Passed"),
        FAILED("Failed"),
        BLOCKED("Blocked");
        
        private final String description;
        
        Status(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
}
