package com.mcp.testserver.dto;

import com.mcp.testserver.model.Requirement;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentAnalysisResponse {
    
    private Long documentId;
    private List<Requirement> requirements;
    private String analysisSummary;
    private String status;
    private String message;
}
