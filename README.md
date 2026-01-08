# MCP Test Server

MCP (Model Context Protocol) Server hỗ trợ Tester viết Manual Test & Auto Test với Spring Boot.

## 🎯 Mục tiêu

- Hỗ trợ Tester viết test case manual từ SRS / PRD / Figma
- Tự động sinh test case theo chuẩn kỹ thuật test
- Hỗ trợ viết script Auto Test theo chuẩn Robot Framework
- Tích hợp vào quy trình test hiện tại (Manual + Automation)
- Kết nối với các hệ thống: Confluence, Test Management, Jira, Bitbucket, Jenkins, Device Farm

## 🚀 Tính năng

### 1. Phân tích tài liệu
- **SRS (Software Requirement Specification)**: Trích xuất yêu cầu chức năng và phi chức năng
- **PRD (Product Requirement Document)**: Phân tích user stories và yêu cầu business
- **Figma**: Phân tích thiết kế UI/UX và tạo yêu cầu giao diện
- **User Stories**: Trích xuất role, action, và value từ user stories

### 2. Tự động sinh Test Case
- **Functional Tests**: Positive, Negative, Boundary tests
- **UI/UX Tests**: Kiểm tra giao diện và trải nghiệm người dùng
- **API Tests**: Kiểm tra API endpoints
- **Performance Tests**: Kiểm tra hiệu năng
- **Security Tests**: Kiểm tra bảo mật
- **Integration Tests**: Kiểm tra tích hợp hệ thống

### 3. Robot Framework Script Generation
- Tự động sinh script Robot Framework từ test case
- Hỗ trợ Data-Driven Testing
- Include keywords và test libraries
- Tùy chỉnh browser, timeout, và environment

## 📋 Yêu cầu hệ thống

- Java 17+
- Maven 3.6+
- Spring Boot 3.2.0
- H2 Database (development) / PostgreSQL (production)

## 🛠️ Cài đặt

### Clone repository
```bash
git clone <repository-url>
cd mcp-test-server
```

### Build và chạy
```bash
# Build project
mvn clean install

# Chạy với development profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Chạy với production profile
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### Docker (optional)
```bash
# Build Docker image
docker build -t mcp-test-server .

# Chạy container
docker run -p 8080:8080 mcp-test-server
```

## 📚 API Documentation

### Base URL
```
http://localhost:8080/mcp-test-server/api/mcp
```

### Endpoints

#### 1. Phân tích tài liệu
```http
POST /documents/analyze
Content-Type: application/json

{
  "document": {
    "title": "SRS Document",
    "content": "FR-001: User login functionality...",
    "type": "SRS"
  },
  "analysisType": "requirements",
  "includeRequirements": true,
  "includeTestCases": false
}
```

#### 2. Sinh Test Case
```http
POST /testcases/generate
Content-Type: application/json

{
  "requirementId": "FR-001",
  "requirement": {
    "title": "User Login",
    "description": "User should be able to login with valid credentials",
    "type": "FUNCTIONAL"
  },
  "testCaseType": "FUNCTIONAL",
  "priority": "HIGH",
  "includeNegativeTests": true,
  "includeBoundaryTests": true
}
```

#### 3. Sinh Robot Framework Script
```http
POST /automation/robot-framework
Content-Type: application/json

{
  "testCaseId": "TC-FR-001-001",
  "testCase": {
    "title": "Positive Test: User Login",
    "description": "Verify user can login with valid credentials",
    "testSteps": "1. Navigate to login page\n2. Enter username\n3. Enter password\n4. Click login",
    "expectedResult": "User successfully logged in"
  },
  "options": {
    "baseUrl": "https://localhost:8080",
    "browser": "chrome",
    "timeout": "10s",
    "includeDataDriver": true
  }
}
```

#### 4. Health Check
```http
GET /health
```

#### 5. Server Info
```http
GET /info
```

## 🔧 Configuration

### Application Properties
- `application.properties`: Default configuration
- `application-dev.properties`: Development settings
- `application-prod.properties`: Production settings

### Key Configuration Options
```properties
# Server
server.port=8080
server.servlet.context-path=/mcp-test-server

# Database
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop

# Robot Framework
robot.framework.default.browser=chrome
robot.framework.default.timeout=10s

# Test Generation
test.generation.default.priority=MEDIUM
test.generation.include.negative.tests=true
```

## 📊 Database Schema

### Tables
- `documents`: Lưu trữ tài liệu đầu vào (SRS, PRD, Figma)
- `requirements`: Yêu cầu được trích xuất từ tài liệu
- `test_cases`: Test cases được sinh tự động
- `test_steps": Chi tiết các bước test
- `automation_scripts`: Script tự động (Robot Framework)

### Relationships
- Document → Requirements (1:N)
- Requirement → Test Cases (1:N)
- Test Case → Test Steps (1:N)
- Test Case → Automation Scripts (1:1)

## 🔌 Integrations

### Jira
```properties
integration.jira.url=https://your-company.atlassian.net
integration.jira.username=your-email@company.com
integration.jira.token=your-api-token
```

### Confluence
```properties
integration.confluence.url=https://your-company.atlassian.net/wiki
integration.confluence.username=your-email@company.com
integration.confluence.token=your-api-token
```

### Bitbucket
```properties
integration.bitbucket.url=https://bitbucket.org/your-team
integration.bitbucket.username=your-username
integration.bitbucket.token=your-app-password
```

### Jenkins
```properties
integration.jenkins.url=http://jenkins.your-company.com
integration.jenkins.username=your-username
integration.jenkins.token=your-api-token
```

## 🧪 Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn verify -P integration-test
```

### API Testing với Postman
Import collection từ `docs/postman-collection.json`

## 📝 Usage Examples

### Example 1: Phân tích SRS Document
```java
// Create document
Document document = Document.builder()
    .title("Login System SRS")
    .content("FR-001: User shall be able to login with valid credentials...")
    .type(Document.DocumentType.SRS)
    .build();

// Analyze document
DocumentAnalysisRequest request = DocumentAnalysisRequest.builder()
    .document(document)
    .includeRequirements(true)
    .build();

DocumentAnalysisResponse response = documentAnalysisService.analyzeDocument(request);
```

### Example 2: Generate Test Cases
```java
// Create requirement
Requirement requirement = Requirement.builder()
    .requirementId("FR-001")
    .title("User Login")
    .description("User should be able to login with valid credentials")
    .type(Requirement.Type.FUNCTIONAL)
    .build();

// Generate test cases
TestCaseGenerationRequest request = TestCaseGenerationRequest.builder()
    .requirement(requirement)
    .testCaseType(TestCaseGenerationRequest.TestCaseType.FUNCTIONAL)
    .includeNegativeTests(true)
    .build();

TestCaseGenerationResponse response = testCaseGenerationService.generateTestCases(request);
```

### Example 3: Generate Robot Framework Script
```java
// Create test case
TestCase testCase = TestCase.builder()
    .testCaseId("TC-FR-001-001")
    .title("Positive Test: User Login")
    .testSteps("1. Navigate to login page\n2. Enter username\n3. Enter password\n4. Click login")
    .expectedResult("User successfully logged in")
    .build();

// Generate Robot Framework script
RobotFrameworkRequest request = RobotFrameworkRequest.builder()
    .testCase(testCase)
    .options(RobotFrameworkRequest.GenerationOptions.builder()
        .baseUrl("https://localhost:8080")
        .browser("chrome")
        .includeDataDriver(true)
        .build())
    .build();

RobotFrameworkResponse response = robotFrameworkService.generateRobotFrameworkScript(request);
```

## 🔄 Workflow

1. **Input**: Upload/Provide SRS/PRD/Figma documents
2. **Analysis**: Server phân tích và trích xuất requirements
3. **Generation**: Tự động sinh test cases từ requirements
4. **Automation**: Tạo Robot Framework scripts từ test cases
5. **Integration**: Push test cases đến Test Management System
6. **Execution**: Run automated tests via CI/CD pipeline
7. **Reporting**: Generate test reports and update Jira tickets

## 🛡️ Security

- Input validation và sanitization
- Rate limiting cho API endpoints
- Authentication và Authorization (sẽ implement trong phase sau)
- HTTPS encryption cho production
- Secure token management cho integrations

## 🚀 Performance

- Async processing cho document analysis
- Caching cho frequently accessed data
- Connection pooling cho database
- Optimized queries với proper indexing

## 📈 Monitoring

- Spring Boot Actuator endpoints
- Custom metrics cho test generation
- Logging với structured format
- Health checks cho external integrations

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

- Email: support@your-company.com
- Documentation: [Wiki](https://github.com/your-org/mcp-test-server/wiki)
- Issues: [GitHub Issues](https://github.com/your-org/mcp-test-server/issues)

## 🗺️ Roadmap

### Phase 1 (Current)
- [x] Document Analysis (SRS, PRD, Figma)
- [x] Manual Test Case Generation
- [x] Robot Framework Script Generation
- [x] Basic MCP API

### Phase 2 (Next)
- [ ] AI-powered test case optimization
- [ ] Advanced error handling and recovery
- [ ] Test execution scheduling
- [ ] Enhanced reporting dashboard

### Phase 3 (Future)
- [ ] AI Vision for UI validation
- [ ] Performance testing automation
- [ ] Security testing automation
- [ ] Machine learning for test maintenance

---

**MCP Test Server** - Empowering testers with intelligent test automation! 🚀
