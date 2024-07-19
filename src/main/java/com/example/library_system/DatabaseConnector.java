package com.example.library_system;

import javafx.scene.control.Alert;
import java.sql.*;

public class DatabaseConnector {
    private static DatabaseConnector instance;
    private Connection connection;

    private DatabaseConnector() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library_system", "root", "new_password");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseConnector getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnector.class) {
                if (instance == null) {
                    instance = new DatabaseConnector();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (this.connection == null || this.connection.isClosed()) {
                this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library_system", "root", "new_password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.connection;
    }
}
