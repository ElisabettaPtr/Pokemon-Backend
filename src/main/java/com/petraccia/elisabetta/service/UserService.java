package com.petraccia.elisabetta.service;

import com.petraccia.elisabetta.dao.UserDAO;
import com.petraccia.elisabetta.model.User;

import java.util.List;

public class UserService {
    private final UserDAO userDAO = new UserDAO();

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public User getUserById(int idUser) {
        return userDAO.getUserById(idUser);
    }


}
