# MCP Test Server - Hướng Dẫn Toàn Diện

## 📖 Giới Thiệu

MCP (Model Context Protocol) Test Server là một hệ thống hỗ trợ Tester viết Manual Test & Auto Test được xây dựng trên nền tảng Spring Boot. Đây là giải pháp toàn diện giúp tự động hóa quy trình testing từ việc phân tích tài liệu yêu cầu đến việc sinh test case và script tự động.

## 🎯 Mục Tiêu Chính

- **Phân Tích Tài Liệu Tự Động**: Hỗ trợ phân tích SRS, PRD, Figma Design
- **Sinh Test Case Thông Minh**: Tự động tạo test case theo chuẩn kỹ thuật
- **Tự Động Hóa Robot Framework**: Sinh script Robot Framework từ test case
- **Tích Hệ Thống**: Kết nối với Jira, Confluence, Bitbucket, Jenkins
- **Quản Lý Test**: Database persistence cho documents, requirements, test cases

## 🏗️ Kiến Trúc Hệ Thống

```
mcp-test-server/
├── src/main/java/com/mcp/testserver/
│   ├── controller/          # REST API Controllers
│   │   ├── McpController.java
│   │   ├── HomeController.java
│   │   └── TestController.java
│   ├── service/             # Business Logic
│   │   ├── DocumentAnalysisService.java
│   │   ├── TestCaseGenerationService.java
│   │   └── RobotFrameworkService.java
│   ├── model/               # JPA Entities
│   │   ├── Document.java
│   │   ├── Requirement.java
│   │   ├── TestCase.java
│   │   ├── TestStep.java
│   │   └── AutomationScript.java
│   ├── dto/                 # Data Transfer Objects
│   │   ├── DocumentAnalysisRequest.java
│   │   ├── DocumentAnalysisResponse.java
│   │   ├── TestCaseGenerationRequest.java
│   │   ├── TestCaseGenerationResponse.java
│   │   ├── RobotFrameworkRequest.java
│   │   └── RobotFrameworkResponse.java
│   ├── repository/           # JPA Repositories
│   │   ├── DocumentRepository.java
│   │   ├── RequirementRepository.java
│   │   ├── TestCaseRepository.java
│   │   └── AutomationScriptRepository.java
│   └── config/              # Configuration Classes
│       ├── WebConfig.java
│       ├── DatabaseConfig.java
│       └── ErrorHandlingConfig.java
├── src/main/resources/
│   ├── templates/           # Thymeleaf Templates
│   ├── static/             # Static Resources
│   └── application.properties # Configuration
├── docs/                  # Documentation
│   ├── API-Examples.md
│   └── Postman-Collection.json
├── docker-compose.yml       # Docker Deployment
├── Dockerfile             # Docker Image
└── pom.xml               # Maven Configuration
```

## 🚀 Tính Năng Nổi Bật

### 1. Phân Tích Tài Liệu (Document Analysis)

**Hỗ trợ các loại tài liệu:**
- **SRS (Software Requirement Specification)**: Trích xuất yêu cầu chức năng và phi chức năng
- **PRD (Product Requirement Document)**: Phân tích user stories và yêu cầu business
- **Figma Design**: Phân tích thiết kế UI/UX và tạo yêu cầu giao diện
- **User Stories**: Trích xuất role, action, và value từ user stories

**Quy trình phân tích:**
1. Upload tài liệu (PDF, DOCX, TXT, MD)
2. System phân tích nội dung bằng regex patterns
3. Trích xuất requirements theo chuẩn
4. Tự động gán priority và status
5. Lưu vào database với metadata

### 2. Sinh Test Case Tự Động (Test Case Generation)

**Các loại test case được hỗ trợ:**
- **Functional Tests**: Positive, Negative, Boundary tests
- **UI/UX Tests**: Kiểm tra giao diện và trải nghiệm người dùng
- **API Tests**: Kiểm tra API endpoints
- **Performance Tests**: Kiểm tra hiệu năng
- **Security Tests**: Kiểm tra bảo mật
- **Integration Tests**: Kiểm tra tích hợp hệ thống

**Quy trình sinh test case:**
1. Nhận requirement từ document analysis
2. Phân tích loại requirement và complexity
3. Sinh test steps theo template chuẩn
4. Tự động tạo preconditions và expected results
5. Gán priority và test type phù hợp

### 3. Robot Framework Script Generation

**Tính năng:**
- **Tự động sinh script** từ test case
- **Hỗ trợ Data-Driven Testing**
- **Include keywords và test libraries**
- **Tùy chỉnh browser, timeout, environment**
- **Generate test data templates**

**Cấu trúc script được sinh:**
```robot
*** Settings ***
Documentation     Test Case Description
Suite Setup       Open Browser    ${BASE_URL}    ${BROWSER}
Suite Teardown    Close Browser
Library           SeleniumLibrary
Library           String
Library           Collections

*** Variables ***
${BROWSER}         chrome
${BASE_URL}        https://demo.example.com
${TIMEOUT}         10s
${USERNAME}        testuser@example.com
${PASSWORD}        testpass123

*** Test Cases ***
Test Case Name
    [Documentation]    Test description
    [Tags]    functional    high    authentication
    [Setup]    Setup Test Preconditions
    # Test steps
    [Teardown]    Verify Test Results

*** Keywords ***
Setup Test Preconditions
    [Documentation]    Setup test preconditions
    Set Selenium Timeout    ${TIMEOUT}
    Login To Application    ${USERNAME}    ${PASSWORD}
```

## 📊 Database Schema

### Tables chính:

1. **documents**: Lưu trữ tài liệu đầu vào
2. **requirements**: Yêu cầu được trích xuất
3. **test_cases**: Test cases được sinh tự động
4. **test_steps**: Chi tiết các bước test
5. **automation_scripts**: Script tự động (Robot Framework)

### Relationships:
- Document → Requirements (1:N)
- Requirement → Test Cases (1:N)
- Test Case → Test Steps (1:N)
- Test Case → Automation Scripts (1:1)

## 🔌 API Documentation

### Base URL
```
http://localhost:8080/mcp-test-server/api/mcp
```

### Endpoints:

#### 1. Health Check
```http
GET /health
```
**Response:** `MCP Test Server is running`

#### 2. Server Information
```http
GET /info
```
**Response:** Server info và supported operations

#### 3. Document Analysis
```http
POST /documents/analyze
Content-Type: application/json

{
  "document": {
    "title": "E-commerce Login System SRS",
    "content": "FR-001: User shall be able to login with valid email and password...",
    "type": "SRS",
    "description": "Login system requirements"
  },
  "analysisType": "requirements",
  "includeRequirements": true,
  "outputFormat": "json"
}
```

#### 4. Test Case Generation
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

#### 5. Robot Framework Generation
```http
POST /automation/robot-framework
Content-Type: application/json

{
  "testCaseId": "TC-FR-001-001",
  "testCase": {
    "title": "Positive Test: User Login",
    "testSteps": "1. Navigate to login page\n2. Enter username...",
    "expectedResult": "User successfully logged in"
  },
  "options": {
    "baseUrl": "https://demo.example.com",
    "browser": "chrome",
    "timeout": "10s",
    "includeDataDriver": true,
    "testEnvironment": "staging"
  }
}
```

## 🛠️ Cài Đặt và Triển Khai

### Yêu cầu hệ thống:
- **Java 17+**
- **Maven 3.6+**
- **Spring Boot 3.2.0**
- **Database**: H2 (dev) / PostgreSQL (prod)

### Cài đặt Development:
```bash
# Clone repository
git clone <repository-url>
cd mcp-test-server

# Build và run
mvn clean install
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Truy cập ứng dụng
http://localhost:8080/mcp-test-server
```

### Docker Deployment:
```bash
# Build và chạy với Docker Compose
docker-compose up -d

# Build Docker image
docker build -t mcp-test-server .

# Chạy container
docker run -p 8080:8080 mcp-test-server
```

### Production Deployment:
```bash
# Build cho production
mvn clean package -Pprod

# Chạy với production profile
java -jar target/test-server-1.0.0.jar --spring.profiles.active=prod
```

## 📋 Quy Trình Working

### 1. Input Phase
- Tester upload SRS/PRD/Figma documents
- System phân tích và trích xuất requirements
- Review và validate requirements

### 2. Analysis Phase
- Document Analysis Service xử lý content
- Trích xuất functional/non-functional requirements
- Gán priority và status tự động

### 3. Generation Phase
- TestCase Generation Service sinh test cases
- Tạo positive, negative, boundary tests
- Robot Framework Service sinh automation scripts

### 4. Validation Phase
- Review test cases và scripts
- Execute tests trong môi trường test
- Log results và update status

### 5. Integration Phase
- Push test cases đến Test Management System
- Execute automated tests qua CI/CD
- Generate reports và update Jira tickets

## 🔌 Integrations

### Jira Integration
```properties
integration.jira.url=https://your-company.atlassian.net
integration.jira.username=your-email@company.com
integration.jira.token=your-api-token
```

### Confluence Integration
```properties
integration.confluence.url=https://your-company.atlassian.net/wiki
integration.confluence.username=your-email@company.com
integration.confluence.token=your-api-token
```

### Bitbucket Integration
```properties
integration.bitbucket.url=https://bitbucket.org/your-team
integration.bitbucket.username=your-username
integration.bitbucket.token=your-app-password
```

### Jenkins Integration
```properties
integration.jenkins.url=http://jenkins.your-company.com
integration.jenkins.username=your-username
integration.jenkins.token=your-api-token
```

## 📈 Monitoring và Logging

### Application Metrics
- Spring Boot Actuator endpoints
- Custom metrics cho test generation
- Performance monitoring
- Health checks

### Logging Strategy
- Structured logging với JSON format
- Log levels: DEBUG (dev), INFO (prod)
- Log aggregation với ELK stack

## 🔒 Security

### Authentication & Authorization
- JWT-based authentication (phase 2)
- Role-based access control
- API rate limiting

### Data Protection
- Input validation và sanitization
- HTTPS encryption cho production
- Secure token management

## 🧪 Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn verify -Pintegration-test
```

### API Testing
- Postman collection có sẵn
- Automated API tests
- Contract testing

## 📚 Documentation

### Available Documents:
- **README.md**: Hướng dẫn toàn diện
- **API-Examples.md**: Ví dụ API calls
- **Postman-Collection.json**: Postman import file
- **Architecture.md**: Thiết kế hệ thống
- **Deployment.md**: Hướng dẫn deployment

### Code Documentation:
- JavaDoc cho tất cả classes
- API documentation với Swagger/OpenAPI
- Database schema documentation

## 🚀 Roadmap

### Phase 1 (Current - ✅ Completed)
- [x] Core Spring Boot application
- [x] Document Analysis (SRS, PRD, Figma)
- [x] Manual Test Case Generation
- [x] Robot Framework Script Generation
- [x] Basic MCP API
- [x] Database persistence

### Phase 2 (Next - 🔄 In Progress)
- [ ] Lombok configuration fixes
- [ ] Enhanced error handling
- [ ] Input validation improvements
- [ ] Performance optimization

### Phase 3 (Future - 📋 Planned)
- [ ] AI-powered test case optimization
- [ ] Advanced error handling và recovery
- [ ] Test execution scheduling
- [ ] Enhanced reporting dashboard
- [ ] AI Vision cho UI validation
- [ ] Performance testing automation
- [ ] Security testing automation
- [ ] Machine learning cho test maintenance

## 🤝 Contributing

### Development Setup:
1. Fork repository
2. Create feature branch
3. Install dependencies: `mvn clean install`
4. Run tests: `mvn test`
5. Submit pull request

### Code Standards:
- Follow Spring Boot best practices
- Use proper exception handling
- Write unit tests cho new features
- Update documentation

## 📞 Support và Contact

### Technical Support:
- **Email**: support@your-company.com
- **Documentation**: [Wiki](https://github.com/your-org/mcp-test-server/wiki)
- **Issues**: [GitHub Issues](https://github.com/your-org/mcp-test-server/issues)
- **Discussions**: [GitHub Discussions](https://github.com/your-org/mcp-test-server/discussions)

### Community:
- **Slack**: #mcp-test-server
- **Discord**: MCP Test Server Community
- **Stack Overflow**: Tag với `mcp-test-server`

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏‍♂️ Acknowledgments

- Spring Boot team cho excellent framework
- Lombok team cho reducing boilerplate code
- Robot Framework community
- Open source contributors

---

**MCP Test Server v1.0.0** - Empowering testers with intelligent test automation! 🚀

*Built with ❤️ by the MCP Testing Team*
