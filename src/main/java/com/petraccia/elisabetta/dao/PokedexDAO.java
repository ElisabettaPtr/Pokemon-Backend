package com.petraccia.elisabetta.dao;

import com.petraccia.elisabetta.model.Pokedex;
import com.petraccia.elisabetta.utility.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PokedexDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public Pokedex addToPokedex(Pokedex pokedex) {
        // Verifica se esiste già un record con lo stesso id_user e national_number
        String checkSql = "SELECT 1 FROM pokedex WHERE id_user = ? AND national_number = ?";
        try (PreparedStatement checkPs = connection.prepareStatement(checkSql)) {
            checkPs.setInt(1, pokedex.getIdUser());
            checkPs.setInt(2, pokedex.getNationalNumber());

            try (ResultSet checkRs = checkPs.executeQuery()) {
                if (checkRs.next()) {
                    // Se il record esiste già, lanciamo un'eccezione per segnalare il conflitto
                    throw new RuntimeException("Pokedex entry for user with this national number already exists.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while checking existing Pokedex entry.", e);
        }

        // Se il record non esiste, procediamo con l'inserimento
        String sql = "INSERT INTO pokedex (id_user, national_number) VALUES (?, ?) RETURNING id_pokedex";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, pokedex.getIdUser());
            ps.setInt(2, pokedex.getNationalNumber());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    pokedex.setIdPokedex(rs.getInt("id_pokedex"));
                } else {
                    throw new RuntimeException("Failed to add Pokedex entry, no rows affected.");
                }
            }

            return pokedex;
        } catch (SQLException e) {
            throw new RuntimeException("Error while adding Pokedex entry to the database.", e);
        }
    }

    public List<Map<String, Object>> getPokedexWithDetailsByUserId(int userId) {
        String sql = """
        SELECT
            p.id_pokedex,
            p.national_number,
            po.*
        FROM
            pokedex p
        JOIN
            pokemon po
        ON
            p.national_number = po.national_number
        WHERE
            p.id_user = ?
       """;

        List<Map<String, Object>> pokedexList = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("idPokedex", rs.getInt("id_pokedex"));
                    entry.put("nationalNumber", rs.getInt("national_number"));

                    Map<String, Object> pokemon = new HashMap<>();
                    pokemon.put("nationalNumber", rs.getInt("national_number"));
                    pokemon.put("gen", rs.getString("gen"));
                    pokemon.put("englishName", rs.getString("english_name"));
                    pokemon.put("primaryType", rs.getString("primary_type"));
                    pokemon.put("secondaryType", rs.getString("secondary_type"));
                    pokemon.put("classification", rs.getString("classification"));
                    pokemon.put("percentMale", rs.getBigDecimal("percent_male"));
                    pokemon.put("percentFemale", rs.getBigDecimal("percent_female"));
                    pokemon.put("heightM", rs.getBigDecimal("height_m"));
                    pokemon.put("weightKg", rs.getBigDecimal("weight_kg"));
                    pokemon.put("captureRate", rs.getInt("capture_rate"));
                    pokemon.put("hp", rs.getInt("hp"));
                    pokemon.put("attack", rs.getInt("attack"));
                    pokemon.put("defense", rs.getInt("defense"));
                    pokemon.put("speed", rs.getInt("speed"));
                    pokemon.put("abilities0", rs.getString("abilities_0"));
                    pokemon.put("abilities1", rs.getString("abilities_1"));
                    pokemon.put("abilitiesSpecial", rs.getString("abilities_special"));
                    pokemon.put("isSubLegendary", rs.getInt("is_sublegendary"));
                    pokemon.put("isLegendary", rs.getInt("is_legendary"));
                    pokemon.put("isMythical", rs.getInt("is_mythical"));
                    pokemon.put("evoChain0", rs.getString("evochain_0"));
                    pokemon.put("evoChain2", rs.getString("evochain_2"));
                    pokemon.put("evoChain4", rs.getString("evochain_4"));
                    pokemon.put("megaEvolution", rs.getString("mega_evolution"));
                    pokemon.put("description", rs.getString("description"));

                    entry.put("pokemon", pokemon);

                    pokedexList.add(entry);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while fetching Pokedex entries for user with ID: " + userId, e);
        }

        return pokedexList;
    }

    public void deleteFromPokedex(int idPokedex) {
        String sql = "DELETE FROM pokedex WHERE id_pokedex = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idPokedex);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("No Pokedex entry found with ID: " + idPokedex);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while deleting Pokedex entry with ID: " + idPokedex, e);
        }
    }
}
