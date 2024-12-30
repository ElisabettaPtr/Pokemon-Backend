package com.petraccia.elisabetta.utility.database;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    Dotenv dotenv = Dotenv.load();

    private final String UrlDB = dotenv.get("URL_DB");
    private final String userDB = dotenv.get("USER_DB");
    private final String pwdDB= dotenv.get("PWD_DB");

    private DatabaseConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(UrlDB, userDB, pwdDB);
            connection.setAutoCommit(true);

            if (isDatabaseEmpty()) {
                System.out.println("Database vuoto. Eseguo gli script di creazione e popolamento...");
                executeScript("src/main/resources/db/ddl/create_tables.sql");
                executeScript("src/main/resources/db/dml/insert_data.sql");
            } else {
                System.out.println("Database già popolato. Nessuna azione necessaria.");
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DatabaseConnection getInstance() {
        if(instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private boolean isDatabaseEmpty() {
        String checkTablesQuery = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'public'";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(checkTablesQuery)) {
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void executeScript(String scriptPath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(scriptPath));
             Statement stmt = connection.createStatement()) {

            String line;
            StringBuilder sql = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                sql.append(line).append("\n");
            }

            stmt.execute(sql.toString());
            System.out.println("Script eseguito con successo: " + scriptPath);

        } catch (IOException | SQLException e) {
            System.err.println("Errore durante l'esecuzione dello script: " + scriptPath);
        }
    }
}
