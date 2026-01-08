package com.mcp.testserver.dto;

import com.mcp.testserver.model.Document;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentAnalysisRequest {
    
    private Document document;
    private String analysisType;
    private boolean includeRequirements;
    private boolean includeTestCases;
    private String outputFormat;
}
