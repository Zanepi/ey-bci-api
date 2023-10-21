package com.github.zanepi.eybciapi.utils.auth;

import com.github.zanepi.eybciapi.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.naming.AuthenticationException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {
    private final SecretKey secret_key =  Jwts.SIG.HS256.key().build();
    private final JwtParser jwtParser;

    public JwtUtil(){

        this.jwtParser = Jwts.parser().verifyWith(secret_key).build();
    }

    public String generateToken(User user){
        Claims claims = Jwts.claims()
                .subject(user.getEmail())
                .add("email",user.getEmail())
                .add("name",user.getName()).build();
        long tokenExpirationTime = 1000 * 60 * 60;
        Date tokenExpiration = new Date(new Date().getTime() + tokenExpirationTime);

        return Jwts.builder()
                .claims(claims)
                .expiration(tokenExpiration)
                .signWith(secret_key)
                .compact();
    }

    private Claims parseJwtClaims(String token){
        return jwtParser.parseSignedClaims(token).getPayload();
    }

    public Claims resolveClaims(HttpServletRequest req){
        try {
            String token = resolveToken(req);
            if(token == null){
                return null;
            }
            return parseJwtClaims(token);
        }catch (ExpiredJwtException ex){
            req.setAttribute("expired",ex.getMessage());
            throw ex;
        }catch (Exception ex){
            req.setAttribute("invalid",ex.getMessage());
            throw ex;
        }
    }
    public String resolveToken(HttpServletRequest request) {

        String TOKEN_HEADER = "Authorization";
        String bearerToken = request.getHeader(TOKEN_HEADER);
        String TOKEN_PREFIX = "Bearer ";
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException{
        return claims.getExpiration().after(new Date());
    }

    private Key getSigninKey(){
        String string_key = "EYBciApiSecretKey";
        byte[] keyBytes = string_key.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyBytes, Jwts.SIG.HS256.getId());
    }

}
