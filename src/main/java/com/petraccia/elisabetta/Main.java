package com.petraccia.elisabetta;

import com.petraccia.elisabetta.controller.PokemonController;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(it -> {
                    it.anyHost();
                });
            });
        }).start(8000);

        PokemonController pokemonController = new PokemonController();
        pokemonController.registerRoutes(app);

        System.out.println("Server is running on port 8000");
    }
}
