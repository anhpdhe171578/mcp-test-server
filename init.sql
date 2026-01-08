-- MCP Test Server Database Initialization Script
-- Created for PostgreSQL deployment

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS mcp_test_db;

-- Use the database
\c mcp_test_db;

-- Create extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Documents table
CREATE TABLE IF NOT EXISTS documents (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    type VARCHAR(50) NOT NULL CHECK (type IN ('SRS', 'PRD', 'FIGMA', 'USER_STORY', 'TECHNICAL_SPEC')),
    source_url VARCHAR(500),
    description VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Requirements table
CREATE TABLE IF NOT EXISTS requirements (
    id BIGSERIAL PRIMARY KEY,
    requirement_id VARCHAR(50) NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    type VARCHAR(50) NOT NULL CHECK (type IN ('FUNCTIONAL', 'NON_FUNCTIONAL', 'BUSINESS', 'TECHNICAL', 'USER_INTERFACE', 'SECURITY', 'PERFORMANCE')),
    priority VARCHAR(20) NOT NULL CHECK (priority IN ('MUST_HAVE', 'SHOULD_HAVE', 'COULD_HAVE', 'WONT_HAVE')),
    status VARCHAR(20) NOT NULL CHECK (status IN ('DRAFT', 'APPROVED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')),
    acceptance_criteria TEXT,
    document_id BIGINT REFERENCES documents(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Test cases table
CREATE TABLE IF NOT EXISTS test_cases (
    id BIGSERIAL PRIMARY KEY,
    test_case_id VARCHAR(50) NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    preconditions TEXT,
    test_steps TEXT,
    expected_result TEXT,
    actual_result TEXT,
    test_type VARCHAR(50) NOT NULL CHECK (test_type IN ('FUNCTIONAL', 'UI', 'API', 'INTEGRATION', 'PERFORMANCE', 'SECURITY', 'REGRESSION', 'SMOKE', 'SANITY')),
    priority VARCHAR(10) NOT NULL CHECK (priority IN ('HIGH', 'MEDIUM', 'LOW')),
    status VARCHAR(20) NOT NULL CHECK (status IN ('DRAFT', 'READY', 'IN_PROGRESS', 'PASSED', 'FAILED', 'BLOCKED', 'SKIPPED')),
    module VARCHAR(100),
    feature VARCHAR(255),
    document_id BIGINT REFERENCES documents(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Test steps table
CREATE TABLE IF NOT EXISTS test_steps (
    id BIGSERIAL PRIMARY KEY,
    step_number INTEGER NOT NULL,
    action TEXT NOT NULL,
    expected TEXT,
    actual TEXT,
    status VARCHAR(20) CHECK (status IN ('NOT_EXECUTED', 'PASSED', 'FAILED', 'BLOCKED')),
    test_case_id BIGINT REFERENCES test_cases(id) ON DELETE CASCADE
);

-- Automation scripts table
CREATE TABLE IF NOT EXISTS automation_scripts (
    id BIGSERIAL PRIMARY KEY,
    script_name VARCHAR(255) NOT NULL,
    script_content TEXT NOT NULL,
    script_type VARCHAR(50) NOT NULL CHECK (script_type IN ('ROBOT_FRAMEWORK', 'SELENIUM', 'REST_ASSURED', 'CYPRESS', 'PLAYWRIGHT', 'JMETER', 'POSTMAN')),
    status VARCHAR(20) NOT NULL CHECK (status IN ('DRAFT', 'READY', 'IN_REVIEW', 'APPROVED', 'DEPRECATED')),
    language VARCHAR(50),
    framework VARCHAR(50),
    tags VARCHAR(500),
    test_case_id BIGINT REFERENCES test_cases(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_documents_type ON documents(type);
CREATE INDEX IF NOT EXISTS idx_documents_created_at ON documents(created_at);
CREATE INDEX IF NOT EXISTS idx_requirements_document_id ON requirements(document_id);
CREATE INDEX IF NOT EXISTS idx_requirements_type ON requirements(type);
CREATE INDEX IF NOT EXISTS idx_requirements_status ON requirements(status);
CREATE INDEX IF NOT EXISTS idx_test_cases_document_id ON test_cases(document_id);
CREATE INDEX IF NOT EXISTS idx_test_cases_test_type ON test_cases(test_type);
CREATE INDEX IF NOT EXISTS idx_test_cases_status ON test_cases(status);
CREATE INDEX IF NOT EXISTS idx_test_cases_priority ON test_cases(priority);
CREATE INDEX IF NOT EXISTS idx_automation_scripts_test_case_id ON automation_scripts(test_case_id);
CREATE INDEX IF NOT EXISTS idx_automation_scripts_script_type ON automation_scripts(script_type);

-- Insert sample data for testing
INSERT INTO documents (title, content, type, description) VALUES 
('Sample SRS Document', 'FR-001: User shall be able to login with valid email and password.\nFR-002: System shall display error message for invalid credentials.\nFR-003: User shall be able to reset password using email.', 'SRS', 'Sample login system requirements'),
('Sample PRD Document', 'As a registered user, I want to receive push notifications for order updates so that I can stay informed about my purchases.\n\nAs a guest user, I want to browse products without creating an account so that I can evaluate the platform before committing.', 'PRD', 'Sample mobile app requirements'),
('Sample Figma Design', 'UI-001: Login form should be centered on page.\nUI-002: Email field should have proper validation.\nUI-003: Password field should show/hide toggle.\n\nUX-001: Form should be responsive on mobile devices.\nUX-002: Error messages should be clear and actionable.', 'FIGMA', 'Sample UI/UX requirements')
ON CONFLICT DO NOTHING;

-- Insert sample requirements
INSERT INTO requirements (requirement_id, title, description, type, priority, status, document_id) VALUES 
('FR-001', 'User shall be able to login with valid email and password', 'User should be able to login to the system using valid email and password credentials', 'FUNCTIONAL', 'MUST_HAVE', 'APPROVED', 1),
('FR-002', 'System shall display error message for invalid credentials', 'System should display appropriate error message when user enters invalid credentials', 'FUNCTIONAL', 'SHOULD_HAVE', 'APPROVED', 1),
('FR-003', 'User shall be able to reset password using email', 'User should be able to reset password by providing email address', 'FUNCTIONAL', 'SHOULD_HAVE', 'APPROVED', 1),
('UI-001', 'Login form should be centered on page', 'Login form should be properly centered on the page', 'USER_INTERFACE', 'SHOULD_HAVE', 'APPROVED', 3),
('UI-002', 'Email field should have proper validation', 'Email input field should validate email format and show appropriate error messages', 'USER_INTERFACE', 'SHOULD_HAVE', 'APPROVED', 3),
('UX-001', 'Form should be responsive on mobile devices', 'Login form should adapt to different screen sizes on mobile devices', 'USER_INTERFACE', 'COULD_HAVE', 'APPROVED', 3)
ON CONFLICT DO NOTHING;

-- Insert sample test cases
INSERT INTO test_cases (test_case_id, title, description, preconditions, test_steps, expected_result, test_type, priority, status, module, feature, document_id) VALUES 
('TC-FR-001-001', 'Positive Test: User Login', 'Verify that the system behaves correctly when valid inputs are provided', 'User is logged in with valid credentials', '1. Navigate to the relevant screen/page\n2. Enter valid data in all required fields\n3. Click submit/execute button\n4. Verify the system processes the request successfully\n5. Confirm the expected result is displayed', 'User successfully logged in and redirected to dashboard', 'FUNCTIONAL', 'HIGH', 'READY', 'Authentication', 'User Login', 1),
('TC-FR-001-002', 'Negative Test: User Login', 'Verify that the system handles invalid inputs appropriately', 'User is logged in with valid credentials', '1. Navigate to the relevant screen/page\n2. Enter invalid data in required fields\n3. Click submit/execute button\n4. Verify appropriate error message is displayed\n5. Confirm system does not process invalid data', 'System should display appropriate error message and not process invalid data', 'FUNCTIONAL', 'MEDIUM', 'READY', 'Authentication', 'User Login', 1),
('TC-UI-001-001', 'UI Test: Login Form Responsiveness', 'Verify user interface elements and interactions', 'User is logged in and has appropriate permissions', '1. Open the application\n2. Navigate to the relevant screen\n3. Verify all UI elements are displayed correctly\n4. Test user interactions (click, hover, input)\n5. Verify responsive design on different screen sizes\n6. Confirm accessibility features are working', 'UI elements are displayed correctly and respond to user interactions', 'UI', 'HIGH', 'READY', 'UI', 'Login Form', 3)
ON CONFLICT DO NOTHING;

-- Insert sample automation scripts
INSERT INTO automation_scripts (script_name, script_content, script_type, status, language, framework, test_case_id) VALUES 
('TC-FR-001-001.robot', '*** Settings ***\nDocumentation     Verify user can login with valid credentials\nSuite Setup       Open Browser    https://demo.example.com    chrome\nSuite Teardown    Close Browser\nTest Setup        Set Selenium Speed    0.5 seconds\nLibrary           SeleniumLibrary\nLibrary           String\nLibrary           Collections\n\n*** Variables ***\n${BROWSER}         chrome\n${BASE_URL}        https://demo.example.com\n${TIMEOUT}         10s\n${USERNAME}        testuser@example.com\n${PASSWORD}        testpass123\n\n*** Test Cases ***\nPositive Test User Login\n    [Documentation]    Verify user can login with valid credentials\n    [Tags]    functional    high    authentication\n    [Setup]    Setup Test Preconditions\n    Go To    ${BASE_URL}/login\n    Input Text    id=email    ${USERNAME}\n    Input Text    id=password    ${PASSWORD}\n    Click Button    id=login-button\n    Wait Until Element Is Visible    id=dashboard    timeout=10s\n    [Teardown]    Verify Test Results\n\n*** Keywords ***\nSetup Test Preconditions\n    [Documentation]    Setup test preconditions\n    Set Selenium Timeout    ${TIMEOUT}\n    Login To Application    ${USERNAME}    ${PASSWORD}\n\nVerify Test Results\n    [Documentation]    Verify test results\n    Capture Page Screenshot', 'ROBOT_FRAMEWORK', 'APPROVED', 'Robot Framework', 'Robot Framework', 1)
ON CONFLICT DO NOTHING;

-- Create update timestamp trigger function
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers for automatic timestamp updates
CREATE TRIGGER update_documents_updated_at 
    BEFORE UPDATE ON documents 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_requirements_updated_at 
    BEFORE UPDATE ON requirements 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_test_cases_updated_at 
    BEFORE UPDATE ON test_cases 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_automation_scripts_updated_at 
    BEFORE UPDATE ON automation_scripts 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Grant permissions
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO mcp_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO mcp_user;

-- Output completion message
DO $$
BEGIN
    RAISE NOTICE 'MCP Test Server database initialized successfully';
END $$;
