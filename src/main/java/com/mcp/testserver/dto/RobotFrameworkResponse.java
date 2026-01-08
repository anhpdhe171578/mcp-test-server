package com.mcp.testserver.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RobotFrameworkResponse {
    
    private String testCaseId;
    private String scriptName;
    private String scriptContent;
    private String generationSummary;
    private String status;
    private String message;
}
