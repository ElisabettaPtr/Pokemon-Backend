package com.petraccia.elisabetta.dao;

import com.petraccia.elisabetta.model.Wishlist;
import com.petraccia.elisabetta.utility.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WishlistDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public Wishlist addToWishlist(Wishlist wishlist) {
        // Verifica se esiste già un record nella tabella Pokedex
        String checkPokedexSql = "SELECT 1 FROM pokedex WHERE id_user = ? AND national_number = ?";
        try (PreparedStatement checkPs = connection.prepareStatement(checkPokedexSql)) {
            checkPs.setInt(1, wishlist.getIdUser());
            checkPs.setInt(2, wishlist.getNationalNumber());

            try (ResultSet checkRs = checkPs.executeQuery()) {
                if (checkRs.next()) {
                    throw new RuntimeException("This Pokémon is already in your Pokedex.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while checking Pokedex entries.", e);
        }

        // Verifica se esiste già un record con lo stesso id_user e national_number
        String checkSql = "SELECT 1 FROM wishlist WHERE id_user = ? AND national_number = ?";
        try (PreparedStatement checkPs = connection.prepareStatement(checkSql)) {
            checkPs.setInt(1, wishlist.getIdUser());
            checkPs.setInt(2, wishlist.getNationalNumber());

            try (ResultSet checkRs = checkPs.executeQuery()) {
                if (checkRs.next()) {
                    // Se il record esiste già, lanciamo un'eccezione per segnalare il conflitto
                    throw new RuntimeException("Wishlist entry for user with this national number already exists.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while checking existing Wishlist entry.", e);
        }

        String sql = "INSERT INTO wishlist (id_user, national_number) VALUES (?, ?) RETURNING id_wishlist";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, wishlist.getIdUser());
            ps.setInt(2, wishlist.getNationalNumber());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    wishlist.setIdWishlist(rs.getInt("id_wishlist"));
                } else {
                    throw new RuntimeException("Failed to add Wishlist entry, no rows affected.");
                }
            }

            return wishlist;
        } catch (SQLException e) {
            throw new RuntimeException("Error while adding Wishlist entry to the database.", e);
        }
    }

    public List<Map<String, Object>> getWishlistWithDetailsByUserId(int userId) {
        String sql = """
        SELECT
            w.id_wishlist,
            w.national_number,
            po.*
        FROM
            wishlist w
        JOIN
            pokemon po
        ON
            w.national_number = po.national_number
        WHERE
            w.id_user = ?
       """;

        List<Map<String, Object>> wishlistList = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("isWishlist", rs.getInt("id_wishlist"));
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

                    wishlistList.add(entry);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while fetching Wishlist entries for user with ID: " + userId, e);
        }

        return wishlistList;
    }

    public void deleteFromWishlist(int idWishlist) {
        String sql = "DELETE FROM wishlist WHERE id_wishlist = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idWishlist);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("No Wishlist entry found with ID: " + idWishlist);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while deleting Wishlist entry with ID: " + idWishlist, e);
        }
    }
}
