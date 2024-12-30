package com.petraccia.elisabetta.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pokedex {
    private int idPokedex;
    private int idUser;
    private int nationalNumber;
}
