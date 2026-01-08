package com.mcp.testserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String home() {
        return "index";
    }
    
    @GetMapping("/mcp-test-server/")
    public String mcpHome() {
        return "index";
    }
    
    @GetMapping("/mcp-test-server")
    public String mcpHomeNoSlash() {
        return "redirect:/mcp-test-server/";
    }
}
