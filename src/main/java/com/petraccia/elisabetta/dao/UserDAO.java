package com.petraccia.elisabetta.dao;

import com.petraccia.elisabetta.model.User;
import com.petraccia.elisabetta.utility.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public List<User> getAllUsers() {
        String getAllUsersSQL = "SELECT * FROM users";
        List<User> users = new ArrayList<>();

        try {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(getAllUsersSQL);
            while (rs.next()) {
                users.add(
                        new User(
                                rs.getInt("id_user"),
                                rs.getString("first_name"),
                                rs.getString("last_name"),
                                rs.getDate("date_of_birth").toLocalDate(),
                                rs.getString("username"),
                                rs.getString("email"),
                                rs.getBoolean("is_active")
                        )
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while retrieving users from the database.", e);
        }

        return users;
    }

    public User getUserById(int idUser) {
        String getUserByIdSQL = "SELECT * FROM users WHERE id_user = ?";
        try {
            PreparedStatement psUserById = connection.prepareStatement(getUserByIdSQL);
            psUserById.setInt(1, idUser);
            ResultSet rs = psUserById.executeQuery();

            if (rs.next()) {
                User user = new User(
                        rs.getInt("id_user"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("date_of_birth").toLocalDate(),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getBoolean("is_active")
                );

                return user;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error while retrieving user from the database.", e);
        }

        return null;
    }

    public User getUserByUsername(String username) {
        String getUserByUsernameSQL = "SELECT * FROM users WHERE username = ?";
        try {
            PreparedStatement psUserByUsername = connection.prepareStatement(getUserByUsernameSQL);
            psUserByUsername.setString(1, username);
            ResultSet rs = psUserByUsername.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id_user"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("date_of_birth").toLocalDate(),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getBoolean("is_active"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while retrieving user from the database.", e);
        }
        return null;
    }

    public User createUser(User user) {
        String createUserSQL = "INSERT INTO users (first_name, last_name, date_of_birth, username, email, password, is_active, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_user";

        try (PreparedStatement psCreateUser = connection.prepareStatement(createUserSQL)) {
            psCreateUser.setString(1, user.getFirstName());
            psCreateUser.setString(2, user.getLastName());
            psCreateUser.setDate(3, java.sql.Date.valueOf(user.getDateOfBirth()));
            psCreateUser.setString(4, user.getUsername());
            psCreateUser.setString(5, user.getEmail());
            psCreateUser.setString(6, user.getPassword());
            psCreateUser.setBoolean(7, user.getIsActive());
            psCreateUser.setTimestamp(8, java.sql.Timestamp.valueOf(user.getCreatedAt()));

            try (ResultSet rs = psCreateUser.executeQuery()) {
                if (rs.next()) {
                    user.setIdUser(rs.getInt("id_user"));
                } else {
                    throw new RuntimeException("User creation failed, no rows affected.");
                }
            }

            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Error while creating user in the database.", e);
        }
    }

    public boolean deleteUserById(int idUser) {
        String deleteUserSQL = "DELETE FROM users WHERE id_user = ?";

        try (PreparedStatement psDeleteUser = connection.prepareStatement(deleteUserSQL)) {
            psDeleteUser.setInt(1, idUser);

            int affectedRows = psDeleteUser.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error while deleting user from the database.", e);
        }
    }

    public boolean isEmailUnique(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) == 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error checking email uniqueness", e);
        }
    }

    public boolean isUsernameUnique(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) == 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error checking username uniqueness", e);
        }
    }
}
