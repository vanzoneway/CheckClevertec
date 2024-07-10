package ru.clevertec.check.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance = null;
    private final Connection connection;

    public DatabaseConnection() throws SQLException, ClassNotFoundException {

        Class.forName("org.postgresql.Driver");
        this.connection = DriverManager.getConnection(System.getProperty("datasource.url"),
                System.getProperty("datasource.username"), System.getProperty("datasource.password"));
    }

    public static DatabaseConnection getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return this.connection;
    }

}