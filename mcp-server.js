#!/usr/bin/env node

const { spawn } = require('child_process');
const path = require('path');

// MCP Server wrapper cho Spring Boot application
class MCPServer {
  constructor() {
    this.javaProcess = null;
    this.serverUrl = 'http://localhost:8080/mcp-test-server';
  }

  async start() {
    console.error('Starting MCP Test Server wrapper...');
    
    // Start Spring Boot application
    const javaProcess = spawn('cmd', ['/c', 'mvn spring-boot:run'], {
      cwd: process.cwd(),
      stdio: ['inherit', 'pipe', 'pipe'],
      shell: true,
      env: {
        ...process.env,
        PATH: process.env.PATH
      }
    });

    this.javaProcess = javaProcess;

    // Wait cho server start
    await this.waitForServer();

    // Start MCP protocol
    this.startMCPProtocol();
  }

  async waitForServer() {
    const maxWait = 60000; // 60 seconds
    const startTime = Date.now();

    while (Date.now() - startTime < maxWait) {
      try {
        const response = await fetch(`${this.serverUrl}/api/mcp/health`);
        if (response.ok) {
          console.error('MCP Test Server is ready!');
          return;
        }
      } catch (error) {
        // Server chưa ready
      }
      await new Promise(resolve => setTimeout(resolve, 1000));
    }
    
    throw new Error('MCP Test Server failed to start within 60 seconds');
  }

  startMCPProtocol() {
    console.error('Starting MCP protocol...');
    
    // MCP Server implementation
    const mcpServer = {
      name: 'mcp-test-server',
      version: '1.0.0',
      tools: [
        {
          name: 'analyze_document',
          description: 'Analyze document and extract requirements',
          inputSchema: {
            type: 'object',
            properties: {
              document: { type: 'string' },
              type: { type: 'string', enum: ['SRS', 'PRD', 'FIGMA'] }
            }
          }
        },
        {
          name: 'generate_test_cases',
          description: 'Generate test cases from requirements',
          inputSchema: {
            type: 'object',
            properties: {
              requirement: { type: 'string' },
              testType: { type: 'string' }
            }
          }
        },
        {
          name: 'generate_robot_script',
          description: 'Generate Robot Framework script',
          inputSchema: {
            type: 'object',
            properties: {
              testCase: { type: 'string' },
              options: { type: 'object' }
            }
          }
        }
      ]
    };

    // Send MCP server info
    console.log(JSON.stringify(mcpServer));

    // Handle MCP commands
    process.stdin.on('data', async (data) => {
      try {
        const request = JSON.parse(data.toString());
        const response = await this.handleMCPRequest(request);
        console.log(JSON.stringify(response));
      } catch (error) {
        console.error('Error handling MCP request:', error);
      }
    });
  }

  async handleMCPRequest(request) {
    const { method, params } = request;

    try {
      switch (method) {
        case 'tools/call':
          return await this.callTool(params);
        default:
          throw new Error(`Unknown method: ${method}`);
      }
    } catch (error) {
      return {
        error: {
          code: -1,
          message: error.message
        }
      };
    }
  }

  async callTool(params) {
    const { name, arguments: args } = params;

    try {
      const response = await fetch(`${this.serverUrl}/api/mcp/${this.getEndpoint(name)}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(args)
      });

      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }

      const result = await response.json();
      return {
        result: {
          content: [
            {
              type: 'text',
              text: JSON.stringify(result, null, 2)
            }
          ]
        }
      };
    } catch (error) {
      throw new Error(`Tool call failed: ${error.message}`);
    }
  }

  getEndpoint(toolName) {
    const endpoints = {
      'analyze_document': 'documents/analyze',
      'generate_test_cases': 'testcases/generate',
      'generate_robot_script': 'automation/robot-framework'
    };
    return endpoints[toolName] || 'unknown';
  }

  async stop() {
    if (this.javaProcess) {
      this.javaProcess.kill();
    }
  }
}

// Start MCP server
const server = new MCPServer();

server.start().catch(error => {
  console.error('Failed to start MCP server:', error);
  process.exit(1);
});

// Handle shutdown
process.on('SIGINT', async () => {
  await server.stop();
  process.exit(0);
});

process.on('SIGTERM', async () => {
  await server.stop();
  process.exit(0);
});
