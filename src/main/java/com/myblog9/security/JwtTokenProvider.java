package com.myblog9.security;
import com.myblog9.payload.BlogAPIException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class JwtTokenProvider {
    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app.jwt-expiration-milliseconds}")
    private int jwtExpirationInMs;
    // generate token
    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationInMs);
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
        return token;
    }
    // get username from the token
    public String getUsernameFromJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
    // validate JWT token
    public boolean validateToken(String token) {
        try{
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException ex) {
            try {
                throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
            } catch (BlogAPIException e) {
                throw new RuntimeException(e);
            }
        } catch (ExpiredJwtException ex) {
            try {
                throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Expired JWT token");
            } catch (BlogAPIException e) {
                throw new RuntimeException(e);
            }
        } catch (UnsupportedJwtException ex) {
            try {
                throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
            } catch (BlogAPIException e) {
                throw new RuntimeException(e);
            }
        } catch (IllegalArgumentException ex) {
            try {
                throw new BlogAPIException(HttpStatus.BAD_REQUEST, "JWT claims string is empty.");
            } catch (BlogAPIException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
