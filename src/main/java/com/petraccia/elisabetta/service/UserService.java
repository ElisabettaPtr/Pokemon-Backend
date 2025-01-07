package com.petraccia.elisabetta.service;

import com.petraccia.elisabetta.dao.UserDAO;
import com.petraccia.elisabetta.model.User;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class UserService {
    private final UserDAO userDAO = new UserDAO();

    Dotenv dotenv = Dotenv.load();

    private final String JWT_SECRET = dotenv.get("JWT_SECRET");

    public String login(String username, String password) {
        // Recupera l'utente dal DB per username
        User user = userDAO.getUserByUsername(username);

        if (user == null || !password.equals(user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        SecretKey key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());

        Instant expirationInstant = Instant.now().plusSeconds(86400); // Il token scade dopo 24 ore


        return Jwts.builder()
                .setSubject(String.valueOf(user.getIdUser()))
                .setExpiration(Date.from(expirationInstant))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public User getUserById(int idUser) {
        return userDAO.getUserById(idUser);
    }

    public User createUser(User user) {
        return userDAO.createUser(user);
    }

    public boolean deleteUserById(int id) {
         return userDAO.deleteUserById(id);
    }

    public boolean isEmailUnique(String email) {
        return userDAO.isEmailUnique(email);
    }

    public boolean isUsernameUnique(String username) {
        return userDAO.isUsernameUnique(username);
    }
}
