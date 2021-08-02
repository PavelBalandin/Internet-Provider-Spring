package com.provider.security;

import com.provider.entity.User;
import com.provider.exception.AccessDeniedException;
import io.jsonwebtoken.*;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

import static org.springframework.util.StringUtils.hasText;

@Log4j2
@Component
public class JwtProvider {

    @Value("${jwt.secret})")
    private String jwtSecret;

    @Value("${jwt.expired}")
    private long expired;

    public String generateToken(User user) {
        Date now = new Date();
        log.trace("Generate token");
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
            log.trace("Token expired");
        } catch (UnsupportedJwtException unsEx) {
            log.trace("Unsupported jwt");
        } catch (MalformedJwtException mjEx) {
            log.trace("Malformed jwt");
        } catch (SignatureException sEx) {
            log.trace("Invalid signature");
        } catch (Exception e) {
            log.trace("invalid token");
        }
        return false;
    }

    public String getLoginFromToken(String token) {
        log.trace("Get login from token");
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public Long getUserIdFromToken(String bearer) {
        log.trace("Get user id from token");
        if (hasText(bearer) && bearer.startsWith("Bearer ")) {
            String token = bearer.substring(7);
            Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
            log.trace("Token is valid");
            return Long.valueOf(claims.getId());
        } else {
            log.trace("Access denied");
            throw new AccessDeniedException();
        }
    }
}
