package com.project.social_media.repository.DatabaseConnection;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {}

    private DatabaseConnection(String databaseUrl, String username, String password) {
        try {
            System.out.println("Connecting to database...");
            System.out.println("Database URL: " + databaseUrl);
            connection = DriverManager.getConnection(databaseUrl, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }

    public static synchronized DatabaseConnection getInstance(String databaseUrl, String username, String password) {
        if (instance == null) {
            instance = new DatabaseConnection(databaseUrl, username, password);
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void query(String sql) {
        // Thực thi SQL logic ở đây nếu cần
    }
}
