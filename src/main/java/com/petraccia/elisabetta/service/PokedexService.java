package com.petraccia.elisabetta.service;

import com.petraccia.elisabetta.dao.PokedexDAO;
import com.petraccia.elisabetta.model.Pokedex;

import java.util.List;
import java.util.Map;

public class PokedexService {
    private final PokedexDAO pokedexDAO = new PokedexDAO();

    public Pokedex addToPokedex(Pokedex pokedex) {
        return pokedexDAO.addToPokedex(pokedex);
    }

    public List<Map<String, Object>> getPokedexWithDetailsByUserId(int userId) {
        return pokedexDAO.getPokedexWithDetailsByUserId(userId);
    }

    public void deleteFromPokedex(int idPokedex) {
        pokedexDAO.deleteFromPokedex(idPokedex);
    }
}
