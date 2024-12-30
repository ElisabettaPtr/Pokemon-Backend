package com.petraccia.elisabetta.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int idUser;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String username;
    private String email;
    private String passwordHash;
    private Boolean isActive;
    private LocalDateTime createdAt;

    public User(int idUser, String firstName, String lastName, LocalDate dateOfBirth, String username, String email, Boolean isActive) {
        this.idUser = idUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.username = username;
        this.email = email;
        this.isActive = isActive;
    }
}

