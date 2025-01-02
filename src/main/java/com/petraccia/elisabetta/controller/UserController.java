package com.petraccia.elisabetta.controller;

import com.petraccia.elisabetta.middleware.AuthMiddleware;
import com.petraccia.elisabetta.model.LoginRequest;
import com.petraccia.elisabetta.model.User;
import com.petraccia.elisabetta.service.UserService;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import lombok.Data;

import java.util.List;

public class UserController {
    private UserService userService = new UserService();
    AuthMiddleware authMiddleware = new AuthMiddleware();
    private String apiVersionV1 = "/api/v1";

    public void registerRoutes(Javalin app) {

        // login --- "http://localhost:8000/api/v1/login" ---
        app.post(apiVersionV1 + "/login", ctx -> {
            LoginRequest loginRequest = ctx.bodyAsClass(LoginRequest.class);

            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();

            if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
                ctx.status(HttpStatus.BAD_REQUEST).json("Username and password are required");
                return;
            }

            try {
                // Chiama il service per eseguire il login e ottenere il token
                String token = userService.login(username, password);

                // Restituisce il token come risposta
                ctx.status(HttpStatus.OK).json(new LoginResponse(token));
            } catch (RuntimeException e) {
                ctx.status(HttpStatus.UNAUTHORIZED).json("Invalid username or password");
            }
        });

        // getAllUsers --- "http://localhost:8000/api/v1/users" ---
        app.get(apiVersionV1 + "/users", ctx -> {
            authMiddleware.handle(ctx);
            if (ctx.status().getCode() == 401) {
                return;
            }

            List<User> users = userService.getAllUsers();
            ctx.json(users);
        });

        // getUserById --- "http://localhost:8000/api/v1/users" + id ---
        app.get(apiVersionV1 + "/users/{id}", ctx -> {
            authMiddleware.handle(ctx);
            if (ctx.status().getCode() == 401) {
                return;
            }

            String userId = ctx.pathParam("id");
            User user = userService.getUserById(Integer.parseInt(userId));
            ctx.json(user);
        });

        // createUser --- "http://localhost:8000/api/v1/registration" ---
        app.post(apiVersionV1 + "/registration", ctx -> {
            User user2create = ctx.bodyAsClass(User.class);

            if (!userService.isEmailUnique(user2create.getEmail())) {
                ctx.status(HttpStatus.BAD_REQUEST).json(new ErrorResponse("Email already in use"));
                return;
            }

            if (!userService.isUsernameUnique(user2create.getUsername())) {
                ctx.status(HttpStatus.BAD_REQUEST).json(new ErrorResponse("Username already in use"));
                return;
            }

            User createdUser = userService.createUser(user2create);
            createdUser.setPassword(null);
            ctx.status(HttpStatus.CREATED).json(createdUser);
        });

        // deleteUserById --- "http://localhost:8000/api/v1/users" + id ---
        app.delete(apiVersionV1 + "/users/{id}", ctx -> {
            authMiddleware.handle(ctx);
            if (ctx.status().getCode() == 401) {
                return;
            }

            String userId = ctx.pathParam("id");
            boolean isDeleted = userService.deleteUserById(Integer.parseInt(userId));

            if (isDeleted) {
                ctx.status(HttpStatus.OK).json("User deleted successfully");
            } else {
                ctx.status(HttpStatus.NOT_FOUND).json("User not found");
            }
        });
    }
    // Classe per restituire la risposta del login (contiene solo il token)
    @Data
    public static class LoginResponse {
        private String token;

        public LoginResponse(String token) {
            this.token = token;
        }
    }

    @Data
    class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }
    }

}
