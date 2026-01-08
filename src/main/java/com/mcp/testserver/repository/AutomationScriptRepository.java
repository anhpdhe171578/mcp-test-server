package com.mcp.testserver.repository;

import com.mcp.testserver.model.AutomationScript;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutomationScriptRepository extends JpaRepository<AutomationScript, Long> {
    
    List<AutomationScript> findByTestCaseId(Long testCaseId);
    
    List<AutomationScript> findByScriptType(AutomationScript.ScriptType scriptType);
    
    List<AutomationScript> findByStatus(AutomationScript.Status status);
    
    List<AutomationScript> findByLanguage(String language);
    
    List<AutomationScript> findByFramework(String framework);
    
    Optional<AutomationScript> findByScriptName(String scriptName);
}
