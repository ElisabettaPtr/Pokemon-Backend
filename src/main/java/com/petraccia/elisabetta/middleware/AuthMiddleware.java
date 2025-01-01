package com.petraccia.elisabetta.middleware;


import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;

public class AuthMiddleware implements Handler {

    private final String JWT_SECRET = "thisisaverysecureandlongsecretkey32char";

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

            ctx.attribute("userClaims", claims);
        } catch (Exception e) {
            ctx.status(401).json("Invalid token");
        }
    }
}