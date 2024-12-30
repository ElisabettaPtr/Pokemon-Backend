package com.petraccia.elisabetta.service;

import com.petraccia.elisabetta.dao.PokemonDAO;
import com.petraccia.elisabetta.model.Pokemon;

import java.util.List;

public class PokemonService {
    private final PokemonDAO pokemonDAO = new PokemonDAO();

    public List<Pokemon> getAllPokemon() {
        return pokemonDAO.getAllPokemon();
    }

    public Pokemon getPokemonByNationalNumber(int nationalNumber) {
        return pokemonDAO.getPokemonByNationalNumber(nationalNumber);
    }

    public Pokemon getPokemonByEnglishName(String englishName) {
        return pokemonDAO.getPokemonByEnglishName(englishName);
    }

    public List<Pokemon> getAllPokemonByType(String type) {
        return pokemonDAO.getAllPokemonByType(type);
    }
}
