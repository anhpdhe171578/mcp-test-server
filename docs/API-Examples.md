# API Examples

## Document Analysis Examples

### 1. Analyze SRS Document

**Request:**
```bash
curl -X POST http://localhost:8080/mcp-test-server/api/mcp/documents/analyze \
  -H "Content-Type: application/json" \
  -d '{
    "document": {
      "title": "E-commerce Login System SRS",
      "content": "FR-001: User shall be able to login with valid email and password.\nFR-002: System shall display error message for invalid credentials.\nFR-003: User shall be able to reset password using email.\n\nAcceptance Criteria:\n1. User enters valid email and password\n2. System authenticates credentials\n3. User is redirected to dashboard\n4. Session is established",
      "type": "SRS",
      "description": "Login system requirements for e-commerce platform"
    },
    "analysisType": "requirements",
    "includeRequirements": true,
    "includeTestCases": false,
    "outputFormat": "json"
  }'
```

**Response:**
```json
{
  "documentId": 1,
  "requirements": [
    {
      "id": 1,
      "requirementId": "FR-001",
      "title": "User shall be able to login with valid email and password",
      "description": "User shall be able to login with valid email and password",
      "type": "FUNCTIONAL",
      "priority": "MUST_HAVE",
      "status": "DRAFT",
      "acceptanceCriteria": "1. User enters valid email and password\n2. System authenticates credentials\n3. User is redirected to dashboard\n4. Session is established"
    },
    {
      "id": 2,
      "requirementId": "FR-002",
      "title": "System shall display error message for invalid credentials",
      "description": "System shall display error message for invalid credentials",
      "type": "FUNCTIONAL",
      "priority": "SHOULD_HAVE",
      "status": "DRAFT"
    }
  ],
  "analysisSummary": "Analysis complete. Found 2 requirements: 2 functional, 0 non-functional, 0 business, 0 UI/UX.",
  "status": "SUCCESS"
}
```

### 2. Analyze PRD Document

**Request:**
```bash
curl -X POST http://localhost:8080/mcp-test-server/api/mcp/documents/analyze \
  -H "Content-Type: application/json" \
  -d '{
    "document": {
      "title": "Mobile App PRD",
      "content": "As a registered user, I want to receive push notifications for order updates so that I can stay informed about my purchases.\n\nAs a guest user, I want to browse products without creating an account so that I can evaluate the platform before committing.",
      "type": "PRD"
    }
  }'
```

## Test Case Generation Examples

### 1. Generate Functional Test Cases

**Request:**
```bash
curl -X POST http://localhost:8080/mcp-test-server/api/mcp/testcases/generate \
  -H "Content-Type: application/json" \
  -d '{
    "requirementId": "FR-001",
    "requirement": {
      "id": 1,
      "requirementId": "FR-001",
      "title": "User shall be able to login with valid email and password",
      "description": "User shall be able to login with valid email and password",
      "type": "FUNCTIONAL",
      "priority": "MUST_HAVE",
      "acceptanceCriteria": "1. User enters valid email and password\n2. System authenticates credentials\n3. User is redirected to dashboard\n4. Session is established"
    },
    "testCaseType": "FUNCTIONAL",
    "priority": "HIGH",
    "includeNegativeTests": true,
    "includeBoundaryTests": true,
    "outputFormat": "json"
  }'
```

**Response:**
```json
{
  "requirementId": "FR-001",
  "testCases": [
    {
      "id": 1,
      "testCaseId": "TC-FR-001-001",
      "title": "Positive Test: User shall be able to login with valid email and password",
      "description": "Verify that the system behaves correctly when valid inputs are provided",
      "preconditions": "User is logged in with valid credentials",
      "testSteps": "1. Navigate to the relevant screen/page\n2. Enter valid data in all required fields\n3. Click submit/execute button\n4. Verify the system processes the request successfully\n5. Confirm the expected result is displayed",
      "expectedResult": "1. User enters valid email and password\n2. System authenticates credentials\n3. User is redirected to dashboard\n4. Session is established",
      "testType": "FUNCTIONAL",
      "priority": "HIGH",
      "status": "DRAFT",
      "module": "Authentication",
      "feature": "User shall be able to login with valid email and password"
    },
    {
      "id": 2,
      "testCaseId": "TC-FR-001-002",
      "title": "Negative Test: User shall be able to login with valid email and password",
      "description": "Verify that the system handles invalid inputs appropriately",
      "preconditions": "User is logged in with valid credentials",
      "testSteps": "1. Navigate to the relevant screen/page\n2. Enter invalid data in required fields\n3. Click submit/execute button\n4. Verify appropriate error message is displayed\n5. Confirm system does not process invalid data",
      "expectedResult": "System should display appropriate error message and not process invalid data",
      "testType": "FUNCTIONAL",
      "priority": "MEDIUM",
      "status": "DRAFT",
      "module": "Authentication",
      "feature": "User shall be able to login with valid email and password"
    }
  ],
  "generationSummary": "Generated 3 test cases for requirement FR-001",
  "status": "SUCCESS"
}
```

### 2. Generate UI Test Cases

**Request:**
```bash
curl -X POST http://localhost:8080/mcp-test-server/api/mcp/testcases/generate \
  -H "Content-Type: application/json" \
  -d '{
    "requirementId": "UI-001",
    "requirement": {
      "id": 3,
      "requirementId": "UI-001",
      "title": "Login form shall be responsive on mobile devices",
      "description": "Login form should adapt to different screen sizes",
      "type": "USER_INTERFACE"
    },
    "testCaseType": "UI",
    "priority": "HIGH"
  }'
```

## Robot Framework Script Generation Examples

### 1. Generate Basic Robot Framework Script

**Request:**
```bash
curl -X POST http://localhost:8080/mcp-test-server/api/mcp/automation/robot-framework \
  -H "Content-Type: application/json" \
  -d '{
    "testCaseId": "TC-FR-001-001",
    "testCase": {
      "testCaseId": "TC-FR-001-001",
      "title": "Positive Test: User Login",
      "description": "Verify user can login with valid credentials",
      "preconditions": "User is logged in with valid credentials",
      "testSteps": "1. Navigate to login page\n2. Enter valid email in email field\n3. Enter valid password in password field\n4. Click login button\n5. Verify dashboard is displayed",
      "expectedResult": "User successfully logged in and redirected to dashboard",
      "testType": "FUNCTIONAL",
      "priority": "HIGH",
      "module": "Authentication"
    },
    "options": {
      "baseUrl": "https://demo.example.com",
      "browser": "chrome",
      "timeout": "10s",
      "seleniumSpeed": "0.5 seconds",
      "username": "testuser@example.com",
      "password": "testpass123",
      "includeDataDriver": false,
      "includeScreenshots": true,
      "testEnvironment": "staging"
    }
  }'
```

**Response:**
```json
{
  "testCaseId": "TC-FR-001-001",
  "scriptName": "TC-FR-001-001.robot",
  "scriptContent": "*** Settings ***\nDocumentation     Verify user can login with valid credentials\nSuite Setup       Open Browser    https://demo.example.com    chrome\nSuite Teardown    Close Browser\nTest Setup        Set Selenium Speed    0.5 seconds\nLibrary           SeleniumLibrary\nLibrary           String\nLibrary           Collections\n\n*** Variables ***\n${BROWSER}         chrome\n${BASE_URL}        https://demo.example.com\n${TIMEOUT}         10s\n${USERNAME}        testuser@example.com\n${PASSWORD}        testpass123\n\n*** Test Cases ***\nPositive Test User Login\n    [Documentation]    Verify user can login with valid credentials\n    [Tags]    functional    high    authentication\n    [Setup]    Setup Test Preconditions\n    Go To    ${BASE_URL}/login\n    Input Text    id=email    ${USERNAME}\n    Input Text    id=password    ${PASSWORD}\n    Click Button    id=login-button\n    Wait Until Element Is Visible    id=dashboard    timeout=10s\n    [Teardown]    Verify Test Results\n\n*** Keywords ***\nSetup Test Preconditions\n    [Documentation]    Setup test preconditions\n    Set Selenium Timeout    ${TIMEOUT}\n    Login To Application    ${USERNAME}    ${PASSWORD}\n\nLogin To Application\n    [Arguments]    ${username}    ${password}\n    [Documentation]    Login to the application\n    Go To    ${BASE_URL}/login\n    Input Text    id=username    ${username}\n    Input Text    id=password    ${password}\n    Click Button    id=login-button\n    Wait Until Element Is Visible    id=dashboard    timeout=10s\n\nVerify Test Results\n    [Documentation]    Verify test results\n    # Expected: User successfully logged in and redirected to dashboard\n    Capture Page Screenshot",
  "generationSummary": "Robot Framework script generated successfully",
  "status": "SUCCESS"
}
```

### 2. Generate Data-Driven Robot Framework Script

**Request:**
```bash
curl -X POST http://localhost:8080/mcp-test-server/api/mcp/automation/robot-framework \
  -H "Content-Type: application/json" \
  -d '{
    "testCaseId": "TC-FR-001-002",
    "testCase": {
      "testCaseId": "TC-FR-001-002",
      "title": "Data-Driven Login Test",
      "description": "Test login with multiple user credentials",
      "testSteps": "1. Navigate to login page\n2. Enter username\n3. Enter password\n4. Click login\n5. Verify result",
      "expectedResult": "Login result matches expectation"
    },
    "options": {
      "baseUrl": "https://demo.example.com",
      "browser": "chrome",
      "includeDataDriver": true,
      "testEnvironment": "test"
    }
  }'
```

## Error Handling Examples

### 1. Invalid Document Type

**Request:**
```bash
curl -X POST http://localhost:8080/mcp-test-server/api/mcp/documents/analyze \
  -H "Content-Type: application/json" \
  -d '{
    "document": {
      "title": "Invalid Document",
      "content": "Some content",
      "type": "INVALID_TYPE"
    }
  }'
```

**Response:**
```json
{
  "status": "ERROR",
  "message": "Failed to analyze document: Unsupported document type: INVALID_TYPE"
}
```

### 2. Missing Required Fields

**Request:**
```bash
curl -X POST http://localhost:8080/mcp-test-server/api/mcp/testcases/generate \
  -H "Content-Type: application/json" \
  -d '{
    "requirementId": "REQ-001"
  }'
```

**Response:**
```json
{
  "status": "ERROR",
  "message": "Failed to generate test cases: Requirement not found for ID: REQ-001"
}
```

## Health Check Examples

### 1. Basic Health Check

**Request:**
```bash
curl -X GET http://localhost:8080/mcp-test-server/api/mcp/health
```

**Response:**
```
MCP Test Server is running
```

### 2. Server Information

**Request:**
```bash
curl -X GET http://localhost:8080/mcp-test-server/api/mcp/info
```

**Response:**
```
MCP Test Server v1.0.0
Supported Operations:
- Document Analysis (SRS, PRD, Figma, User Stories)
- Test Case Generation (Manual & Automated)
- Robot Framework Script Generation
- Integration with Test Management Systems
```

## Integration Examples

### 1. Complete Workflow

```bash
# Step 1: Analyze document
ANALYSIS_RESPONSE=$(curl -s -X POST http://localhost:8080/mcp-test-server/api/mcp/documents/analyze \
  -H "Content-Type: application/json" \
  -d '{"document": {"title": "Test SRS", "content": "FR-001: User login functionality", "type": "SRS"}}')

# Step 2: Extract requirement ID and generate test cases
REQUIREMENT_ID=$(echo $ANALYSIS_RESPONSE | jq -r '.requirements[0].requirementId')

curl -X POST http://localhost:8080/mcp-test-server/api/mcp/testcases/generate \
  -H "Content-Type: application/json" \
  -d "{\"requirementId\": \"$REQUIREMENT_ID\", \"testCaseType\": \"FUNCTIONAL\"}"

# Step 3: Generate Robot Framework script for first test case
curl -X POST http://localhost:8080/mcp-test-server/api/mcp/automation/robot-framework \
  -H "Content-Type: application/json" \
  -d '{"testCaseId": "TC-FR-001-001", "testCase": {...}, "options": {...}}'
```

### 2. Batch Processing

```bash
# Process multiple documents
for file in *.srs; do
  content=$(cat "$file")
  curl -X POST http://localhost:8080/mcp-test-server/api/mcp/documents/analyze \
    -H "Content-Type: application/json" \
    -d "{\"document\": {\"title\": \"$file\", \"content\": \"$content\", \"type\": \"SRS\"}}"
done
```

## Tips and Best Practices

1. **Document Formatting**: Use consistent formatting for requirements (FR-001, NFR-001, etc.)
2. **Test Case IDs**: Follow naming convention: TC-{REQUIREMENT_ID}-{SEQUENCE}
3. **Error Handling**: Always check response status before processing results
4. **Batch Operations**: Use async processing for large document sets
5. **Caching**: Cache analysis results to avoid reprocessing
6. **Validation**: Validate input data before sending to API
7. **Rate Limiting**: Implement client-side rate limiting for bulk operations
