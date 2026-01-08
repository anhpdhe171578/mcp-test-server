package com.mcp.testserver.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("com.mcp.testserver.model")
@EnableJpaRepositories("com.mcp.testserver.repository")
public class DatabaseConfig {
}
