package com.provider.security;

import com.provider.entity.User;
import com.provider.exception.AccessDeniedException;
import io.jsonwebtoken.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

import static org.springframework.util.StringUtils.hasText;

@Log
@Component
public class JwtProvider {
    @Value("${jwt.secret})")
    private String jwtSecret;

    @Value("${jwt.expired}")
    private long expired;

    public String generateToken(User user) {
        Date now = new Date();
        return Jwts.builder()
                .setId(user.getId().toString())
                .setSubject(user.getLogin())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expired))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.severe("Token expired");
        } catch (UnsupportedJwtException unsEx) {
            log.severe("Unsupported jwt");
        } catch (MalformedJwtException mjEx) {
            log.severe("Malformed jwt");
        } catch (SignatureException sEx) {
            log.severe("Invalid signature");
        } catch (Exception e) {
            log.severe("invalid token");
        }
        return false;
    }

    public String getLoginFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public Long getUserIdFromToken(String bearer) {
        if (hasText(bearer) && bearer.startsWith("Bearer ")) {
            String token = bearer.substring(7);
            Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
            return Long.valueOf(claims.getId());
        } else {
            throw new AccessDeniedException();
        }
    }
}
