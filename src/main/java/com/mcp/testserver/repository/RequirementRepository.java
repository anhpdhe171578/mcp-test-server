package com.mcp.testserver.repository;

import com.mcp.testserver.model.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequirementRepository extends JpaRepository<Requirement, Long> {
    
    List<Requirement> findByDocumentId(Long documentId);
    
    List<Requirement> findByType(Requirement.Type type);
    
    List<Requirement> findByStatus(Requirement.Status status);
    
    Optional<Requirement> findByRequirementId(String requirementId);
    
    List<Requirement> findByPriority(Requirement.Priority priority);
}
