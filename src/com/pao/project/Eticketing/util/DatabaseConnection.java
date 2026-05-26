package com.pao.project.Eticketing.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private final String url;
    private final String user;
    private final String password;
    private Connection reusableConnection;
    private DatabaseConnection() {
        try {
            Properties props = new Properties();
            try (InputStream in = getClass().getClassLoader().getResourceAsStream("db.properties")) {
                if (in == null) {
                    throw new RuntimeException("db.properties nu a fost gasit in resources/");
                }
                props.load(in);
            }
            this.url = props.getProperty("db.url");
            this.user = props.getProperty("db.user");
            this.password = props.getProperty("db.password");
        } catch (IOException e) {
            throw new RuntimeException("Eroare la citirea configuratiei bazei de date", e);
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (reusableConnection == null || reusableConnection.isClosed()) {
            reusableConnection = DriverManager.getConnection(url, user, password);
        }
        return reusableConnection;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Conexiune inchisa.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
