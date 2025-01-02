package com.petraccia.elisabetta.middleware;

import io.github.cdimascio.dotenv.Dotenv;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;

public class AuthMiddleware implements Handler {

    Dotenv dotenv = Dotenv.load();

    private final String JWT_SECRET = dotenv.get("JWT_SECRET");

    @Override
    public void handle(Context ctx) throws Exception {
        String token = ctx.header("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            ctx.status(401).json("Unauthorized");
            return;
        }

        try {
            token = token.replace("Bearer ", "");

            JwtParser jwtParser = Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8)))
                    .build();

            Claims claims = jwtParser.parseClaimsJws(token).getBody();

            // Aggiungi i claims al contesto per renderli disponibili alle rotte successive
            ctx.attribute("userClaims", claims);
        } catch (Exception e) {
            ctx.status(401).json("Invalid token");
            System.err.println("Error parsing token: " + e.getMessage());
        }
    }

    public String getUserIdFromToken(Context ctx) {
        Claims claims = ctx.attribute("userClaims");
        if (claims != null) {
            return claims.getSubject();  // Assuming the user ID is stored as the subject in the JWT
        }
        return null;  // Return null if no valid claims are present
    }
}