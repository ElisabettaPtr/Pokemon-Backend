package com.petraccia.elisabetta.controller;

import com.petraccia.elisabetta.middleware.AuthMiddleware;
import com.petraccia.elisabetta.model.Wishlist;
import com.petraccia.elisabetta.service.WishlistService;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

import java.util.List;
import java.util.Map;

public class WishlistController {
    private WishlistService wishlistService = new WishlistService();
    AuthMiddleware authMiddleware = new AuthMiddleware();
    private String apiVersionV1 = "/api/v1";

    public void registerRoutes(Javalin app) {

        // addToWishlist --- "http://localhost:8000/api/v1/wishlist/add" ---
        app.post(apiVersionV1 + "/wishlist/add", ctx -> {
            authMiddleware.handle(ctx);
            if (ctx.status().getCode() == 401) {
                return;
            }

            Wishlist wishlist2create = ctx.bodyAsClass(Wishlist.class);

            try {
                Wishlist wishlistCreated = wishlistService.addToWishlist(wishlist2create);
                ctx.status(HttpStatus.CREATED).json(wishlistCreated);
            } catch (RuntimeException e) {
                if (e.getMessage().contains("already in your Pokedex")) {
                    ctx.status(HttpStatus.BAD_REQUEST).result("This Pokémon is already in your Pokedex.");
                } else if (e.getMessage().contains("already exists")) {
                    ctx.status(HttpStatus.BAD_REQUEST).result("Wishlist entry already exists for this user and national number.");
                } else {
                    ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).result("Failed to add Wishlist entry.");
                }
            }
        });


        // getWishlistWithDetailsByUserId --- "http://localhost:8000/api/v1/wishlist" ---
        app.get(apiVersionV1 + "/wishlist", ctx -> {
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
            List<Map<String, Object>> wishlistWithDetails = wishlistService.getWishlistWithDetailsByUserId(Integer.parseInt(loggedUserId));
            ctx.json(wishlistWithDetails);
        });

        // deleteFromWishlist --- "http://localhost:8000/api/v1/wishlist/delete/" + id ---
        app.delete(apiVersionV1 + "/wishlist/delete/{id}", ctx -> {
            authMiddleware.handle(ctx);
            if (ctx.status().getCode() == 401) {
                return;
            }

            int idWishlist = Integer.parseInt(ctx.pathParam("id"));

            try {
                wishlistService.deleteFromWishlist(idWishlist);
                ctx.status(HttpStatus.OK).result("Wishlist entry deleted successfully.");
            } catch (RuntimeException e) {
                ctx.status(HttpStatus.INTERNAL_SERVER_ERROR).result("Failed to delete Wishlist entry.");
            }
        });
    }
}
