package com.mcp.testserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/mcp")
@CrossOrigin(origins = "*")
public class SimpleMcpController {
    
    @PostMapping("/documents/analyze")
    public ResponseEntity<Map<String, Object>> analyzeDocument(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "MOCK");
        response.put("message", "Document analysis service temporarily disabled - working on Lombok configuration issues");
        response.put("documentId", request.get("document"));
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/testcases/generate")
    public ResponseEntity<Map<String, Object>> generateTestCases(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "MOCK");
        response.put("message", "Test case generation service temporarily disabled - working on Lombok configuration issues");
        response.put("requirementId", request.get("requirementId"));
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/automation/robot-framework")
    public ResponseEntity<Map<String, Object>> generateRobotFrameworkScript(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "MOCK");
        response.put("message", "Robot Framework service temporarily disabled - working on Lombok configuration issues");
        response.put("testCaseId", request.get("testCaseId"));
        response.put("scriptName", "mock-script.robot");
        response.put("scriptContent", "*** Settings ***\nDocumentation Mock script\nLibrary SeleniumLibrary\n\n*** Test Cases ***\nMock Test\n    Log Hello World");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("MCP Test Server is running - Core services operational");
    }
    
    @GetMapping("/info")
    public ResponseEntity<String> getInfo() {
        return ResponseEntity.ok("""
            MCP Test Server v1.0.0 - RUNNING
            ========================================
            ✅ Core Services: Operational
            ⚠️  Advanced Services: Temporarily Disabled (Lombok Configuration Issues)
            
            Available Endpoints:
            - GET /api/mcp/health - Health Check
            - GET /api/mcp/info - Server Information
            - POST /api/mcp/documents/analyze - Document Analysis (Mock)
            - POST /api/mcp/testcases/generate - Test Case Generation (Mock)
            - POST /api/mcp/automation/robot-framework - Robot Framework Generation (Mock)
            
            Status: Server is running with mock responses for advanced services.
            Working on: Resolving Lombok annotation processing issues.
            """);
    }
}
