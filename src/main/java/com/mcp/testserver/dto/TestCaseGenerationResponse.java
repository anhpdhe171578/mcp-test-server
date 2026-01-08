package com.mcp.testserver.dto;

import com.mcp.testserver.model.TestCase;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestCaseGenerationResponse {
    
    private String requirementId;
    private List<TestCase> testCases;
    private String generationSummary;
    private String status;
    private String message;
}
