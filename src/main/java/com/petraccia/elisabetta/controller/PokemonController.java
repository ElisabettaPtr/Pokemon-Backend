package com.petraccia.elisabetta.controller;

import com.petraccia.elisabetta.middleware.AuthMiddleware;
import com.petraccia.elisabetta.model.Pokemon;
import com.petraccia.elisabetta.service.PokemonService;
import io.javalin.Javalin;

import java.util.List;

public class PokemonController {
    private PokemonService pokemonService = new PokemonService();
    AuthMiddleware authMiddleware = new AuthMiddleware();
    private String apiVersionV1 = "/api/v1";

    public void registerRoutes(Javalin app) {

        // getAllPokemon --- "http://localhost:8000/api/v1/pokemon" ---
        app.get(apiVersionV1 + "/pokemon", ctx -> {
            authMiddleware.handle(ctx);
            if (ctx.status().getCode() == 401) {
                return;
            }

            List<Pokemon> pokemon = pokemonService.getAllPokemon();
            ctx.json(pokemon);
        });

        // getPokemonByNationalNumber --- "http://localhost:8000/api/v1/pokemon/national-number" + nationalNumber ---
        app.get(apiVersionV1 + "/pokemon/national-number/{nationalNumber}", ctx -> {
            authMiddleware.handle(ctx);
            if (ctx.status().getCode() == 401) {
                return;
            }

            String pokemonNationalNumber = ctx.pathParam("nationalNumber");
            Pokemon pokemon = pokemonService.getPokemonByNationalNumber(Integer.parseInt(pokemonNationalNumber));
            ctx.json(pokemon);
        });

        // getPokemonByEnglishName --- "http://localhost:8000/api/v1/pokemon/english-name" + englishName ---
        app.get(apiVersionV1 + "/pokemon/english-name/{englishName}", ctx -> {
            authMiddleware.handle(ctx);
            if (ctx.status().getCode() == 401) {
                return;
            }

            String pokemonEnglishName = ctx.pathParam("englishName");
            Pokemon pokemon = pokemonService.getPokemonByEnglishName(pokemonEnglishName);
            ctx.json(pokemon);
        });

        // getAllPokemonByType --- "http://localhost:8000/api/v1/pokemon/type" + type ---
        app.get(apiVersionV1 + "/pokemon/type/{type}", ctx -> {
            authMiddleware.handle(ctx);
            if (ctx.status().getCode() == 401) {
                return;
            }

            String pokemonType = ctx.pathParam("type");
            List<Pokemon> pokemon = pokemonService.getAllPokemonByType(pokemonType);
            ctx.json(pokemon);
        });
    }
}
