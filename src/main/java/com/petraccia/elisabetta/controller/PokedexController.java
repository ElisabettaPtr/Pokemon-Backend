package com.petraccia.elisabetta.controller;

import com.petraccia.elisabetta.middleware.AuthMiddleware;
import com.petraccia.elisabetta.model.Pokedex;
import com.petraccia.elisabetta.service.PokedexService;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

import java.util.List;
import java.util.Map;

public class PokedexController {
    private PokedexService pokedexService = new PokedexService();
    AuthMiddleware authMiddleware = new AuthMiddleware();
    private String apiVersionV1 = "/api/v1";

    public void registerRoutes(Javalin app) {

        // addToPokedex --- "http://localhost:8000/api/v1/pokedex/add" ---
        app.post(apiVersionV1 + "/pokedex/add", ctx -> {
            authMiddleware.handle(ctx);
            if (ctx.status().getCode() == 401) {
                return;
            }

            Pokedex pokedex2create = ctx.bodyAsClass(Pokedex.class);

            try {
                Pokedex pokedexCreated = pokedexService.addToPokedex(pokedex2create);
                ctx.status(HttpStatus.CREATED).json(pokedexCreated);
            } catch (RuntimeException e) {
                // Controlla se l'errore riguarda l'esistenza del record
                if (e.getMessage().contains("already exists")) {
                    ctx.status(HttpStatus.BAD_REQUEST).result("Pokedex entry already exists for this user and national number.");
                } else {
                    // Gestione generica degli errori
                    ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).result("Failed to add Pokedex entry.");
                }
            }
        });


        // getPokedexWithDetailsByUserId --- "http://localhost:8000/api/v1/pokedex" ---
        app.get(apiVersionV1 + "/pokedex", ctx -> {
            authMiddleware.handle(ctx);
            if (ctx.status().getCode() == 401) {
                return;
            }

            // Estrai l'ID dell'utente loggato dal middleware di autenticazione
            String loggedUserId = authMiddleware.getUserIdFromToken(ctx);

            if (loggedUserId == null) {
                ctx.status(HttpStatus.UNAUTHORIZED).result("Unauthorized");
                return;
            }

            // Usa l'ID dell'utente loggato per recuperare il Pokedex
            List<Map<String, Object>> pokedexWithDetails = pokedexService.getPokedexWithDetailsByUserId(Integer.parseInt(loggedUserId));
            ctx.json(pokedexWithDetails);
        });

        // deleteFromPokedex --- "http://localhost:8000/api/v1/pokedex" + id ---
        app.delete(apiVersionV1 + "/pokedex/{id}", ctx -> {
            authMiddleware.handle(ctx);
            if (ctx.status().getCode() == 401) {
                return;
            }

            int idPokedex = Integer.parseInt(ctx.pathParam("id"));

            try {
                pokedexService.deleteFromPokedex(idPokedex);
                ctx.status(HttpStatus.OK).result("Pokedex entry deleted successfully.");
            } catch (RuntimeException e) {
                ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).result("Failed to delete Pokedex entry.");
            }
        });
    }
}
