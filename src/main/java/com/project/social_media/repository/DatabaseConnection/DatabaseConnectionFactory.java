package com.project.social_media.repository.DatabaseConnection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConnectionFactory {
    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    public DatabaseConnection createConnection() {
        return DatabaseConnection.getInstance(databaseUrl, username, password);
    }
}
