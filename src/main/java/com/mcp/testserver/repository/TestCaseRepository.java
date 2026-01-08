package com.mcp.testserver.repository;

import com.mcp.testserver.model.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Long> {
    
    List<TestCase> findByDocumentId(Long documentId);
    
    // Remove problematic query method
    // List<TestCase> findByRequirementId(String requirementId);
    
    List<TestCase> findByTestType(TestCase.TestType testType);
    
    List<TestCase> findByStatus(TestCase.Status status);
    
    List<TestCase> findByPriority(TestCase.Priority priority);
    
    Optional<TestCase> findByTestCaseId(String testCaseId);
    
    List<TestCase> findByModule(String module);
}
