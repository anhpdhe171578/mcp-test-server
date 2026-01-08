package com.mcp.testserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/test")
public class TestController {
    
    @GetMapping("/simple")
    public ResponseEntity<Map<String, Object>> simpleTest() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "MCP Test Server is working!");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/echo")
    public ResponseEntity<Map<String, Object>> echo(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        response.put("echo", request);
        response.put("status", "echoed");
        return ResponseEntity.ok(response);
    }
}
