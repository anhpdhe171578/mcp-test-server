package com.mcp.testserver.dto;

import com.mcp.testserver.model.Requirement;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestCaseGenerationRequest {
    
    private String requirementId;
    private Requirement requirement;
    private TestCaseType testCaseType;
    private Priority priority;
    private boolean includeNegativeTests;
    private boolean includeBoundaryTests;
    private String outputFormat;
    
    public enum TestCaseType {
        FUNCTIONAL,
        UI,
        API,
        PERFORMANCE,
        SECURITY,
        ALL
    }
    
    public enum Priority {
        HIGH,
        MEDIUM,
        LOW,
        ALL
    }
}
