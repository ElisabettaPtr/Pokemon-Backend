package com.petraccia.elisabetta.dao;

import com.petraccia.elisabetta.model.Pokemon;
import com.petraccia.elisabetta.utility.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PokemonDAO {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    public List<Pokemon> getAllPokemon() {
        String getAllPokemonSQL = "SELECT * FROM pokemon";
        List<Pokemon> pokemon = new ArrayList<>();

        try {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(getAllPokemonSQL);
            while (rs.next()) {
                pokemon.add(
                        new Pokemon(
                                rs.getInt("national_number"),
                                rs.getString("gen"),
                                rs.getString("english_name"),
                                rs.getString("primary_type"),
                                rs.getString("secondary_type"),
                                rs.getString("classification"),
                                rs.getBigDecimal("percent_male"),
                                rs.getBigDecimal("percent_female"),
                                rs.getBigDecimal("height_m"),
                                rs.getBigDecimal("weight_kg"),
                                rs.getInt("capture_rate"),
                                rs.getInt("hp"),
                                rs.getInt("attack"),
                                rs.getInt("defense"),
                                rs.getInt("speed"),
                                rs.getString("abilities_0"),
                                rs.getString("abilities_1"),
                                rs.getString("abilities_special"),
                                rs.getInt("is_sublegendary"),
                                rs.getInt("is_legendary"),
                                rs.getInt("is_mythical"),
                                rs.getString("evochain_0"),
                                rs.getString("evochain_2"),
                                rs.getString("evochain_4"),
                                rs.getString("mega_evolution"),
                                rs.getString("description")
                                )
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while retrieving Pokémon from the database.", e);
        }

        return pokemon;
    }

    public Pokemon getPokemonByNationalNumber(int nationalNumber) {
        String getPokemonByNationalNumberSQL = "SELECT * FROM pokemon WHERE national_number = ?";
        try {
            PreparedStatement psPokemonByNationalNumber = connection.prepareStatement(getPokemonByNationalNumberSQL);
            psPokemonByNationalNumber.setInt(1, nationalNumber);
            ResultSet rs = psPokemonByNationalNumber.executeQuery();

            if (rs.next()) {
                Pokemon pokemon = new Pokemon(
                        rs.getInt("national_number"),
                        rs.getString("gen"),
                        rs.getString("english_name"),
                        rs.getString("primary_type"),
                        rs.getString("secondary_type"),
                        rs.getString("classification"),
                        rs.getBigDecimal("percent_male"),
                        rs.getBigDecimal("percent_female"),
                        rs.getBigDecimal("height_m"),
                        rs.getBigDecimal("weight_kg"),
                        rs.getInt("capture_rate"),
                        rs.getInt("hp"),
                        rs.getInt("attack"),
                        rs.getInt("defense"),
                        rs.getInt("speed"),
                        rs.getString("abilities_0"),
                        rs.getString("abilities_1"),
                        rs.getString("abilities_special"),
                        rs.getInt("is_sublegendary"),
                        rs.getInt("is_legendary"),
                        rs.getInt("is_mythical"),
                        rs.getString("evochain_0"),
                        rs.getString("evochain_2"),
                        rs.getString("evochain_4"),
                        rs.getString("mega_evolution"),
                        rs.getString("description")
                );

                return pokemon;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error while retrieving Pokémon from the database.", e);
        }

        return null;
    }

    public Pokemon getPokemonByEnglishName(String englishName) {
        String getPokemonByEnglishNameSQL = "SELECT * FROM pokemon WHERE english_name = ?";
        try {
            String toLowerCase = englishName.toLowerCase();
            String englishNameFinal = toLowerCase.substring(0, 1).toUpperCase() + toLowerCase.substring(1);

            PreparedStatement psPokemonByEnglishName = connection.prepareStatement(getPokemonByEnglishNameSQL);
            psPokemonByEnglishName.setString(1, englishNameFinal);
            ResultSet rs = psPokemonByEnglishName.executeQuery();

            if (rs.next()) {
                Pokemon pokemon = new Pokemon(
                        rs.getInt("national_number"),
                        rs.getString("gen"),
                        rs.getString("english_name"),
                        rs.getString("primary_type"),
                        rs.getString("secondary_type"),
                        rs.getString("classification"),
                        rs.getBigDecimal("percent_male"),
                        rs.getBigDecimal("percent_female"),
                        rs.getBigDecimal("height_m"),
                        rs.getBigDecimal("weight_kg"),
                        rs.getInt("capture_rate"),
                        rs.getInt("hp"),
                        rs.getInt("attack"),
                        rs.getInt("defense"),
                        rs.getInt("speed"),
                        rs.getString("abilities_0"),
                        rs.getString("abilities_1"),
                        rs.getString("abilities_special"),
                        rs.getInt("is_sublegendary"),
                        rs.getInt("is_legendary"),
                        rs.getInt("is_mythical"),
                        rs.getString("evochain_0"),
                        rs.getString("evochain_2"),
                        rs.getString("evochain_4"),
                        rs.getString("mega_evolution"),
                        rs.getString("description")
                );

                return pokemon;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public List<Pokemon> getAllPokemonByType(String type) {
        String getAllPokemonByTypeSQL = "SELECT * FROM pokemon WHERE primary_type = ? OR secondary_type = ?";
        List<Pokemon> pokemon = new ArrayList<>();

        try (PreparedStatement psPokemonByType = connection.prepareStatement(getAllPokemonByTypeSQL)) {
            String toLowerCase = type.toLowerCase();
            String typeFinal = toLowerCase.substring(0, 1).toUpperCase() + toLowerCase.substring(1);

            psPokemonByType.setString(1, typeFinal);
            psPokemonByType.setString(2, typeFinal);



            try (ResultSet rs = psPokemonByType.executeQuery()) {
                while (rs.next()) {
                    pokemon.add(
                            new Pokemon(
                                    rs.getInt("national_number"),
                                    rs.getString("gen"),
                                    rs.getString("english_name"),
                                    rs.getString("primary_type"),
                                    rs.getString("secondary_type"),
                                    rs.getString("classification"),
                                    rs.getBigDecimal("percent_male"),
                                    rs.getBigDecimal("percent_female"),
                                    rs.getBigDecimal("height_m"),
                                    rs.getBigDecimal("weight_kg"),
                                    rs.getInt("capture_rate"),
                                    rs.getInt("hp"),
                                    rs.getInt("attack"),
                                    rs.getInt("defense"),
                                    rs.getInt("speed"),
                                    rs.getString("abilities_0"),
                                    rs.getString("abilities_1"),
                                    rs.getString("abilities_special"),
                                    rs.getInt("is_sublegendary"),
                                    rs.getInt("is_legendary"),
                                    rs.getInt("is_mythical"),
                                    rs.getString("evochain_0"),
                                    rs.getString("evochain_2"),
                                    rs.getString("evochain_4"),
                                    rs.getString("mega_evolution"),
                                    rs.getString("description")
                            )
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while retrieving Pokémon: " + e.getMessage(), e);
        }

        return pokemon;
    }
}
