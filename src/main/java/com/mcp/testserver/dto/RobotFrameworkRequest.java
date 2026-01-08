package com.mcp.testserver.dto;

import com.mcp.testserver.model.TestCase;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RobotFrameworkRequest {
    
    private String testCaseId;
    private TestCase testCase;
    private GenerationOptions options;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GenerationOptions {
        private String baseUrl;
        private String browser;
        private String timeout;
        private String seleniumSpeed;
        private String username;
        private String password;
        private boolean includeDataDriver;
        private boolean includeScreenshots;
        private boolean includeErrorHandling;
        private String testEnvironment;
    }
}
