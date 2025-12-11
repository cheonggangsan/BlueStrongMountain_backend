package com.ssafy.BlueStrongMountain.auth;

import com.ssafy.BlueStrongMountain.domain.User;
import com.ssafy.BlueStrongMountain.domain.UserRole;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.io.Decoders;
import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {
    private static final String SECRET =
            "VGhpcy1pcy1yYW5kb20tc2VjcmV0LWtleS1mb3ItQlNULUFwcA==";

    // Access Token: 30분
    private static final long ACCESS_TOKEN_VALIDITY_MS = 30 * 60 * 1000;

    // Refresh Token: 14일
    private static final long REFRESH_TOKEN_VALIDITY_MS = 14L * 24 * 60 * 60 * 1000;

    private Key key;

    @PostConstruct
    public void init(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + ACCESS_TOKEN_VALIDITY_MS);

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .claim("email", user.getEmail())
                .claim("username", user.getUsername())
                .claim("role", UserRole.USER.name())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    public String generateRefreshToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + REFRESH_TOKEN_VALIDITY_MS);

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .claim("type", "refresh")
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String refreshToken){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(refreshToken);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public Long getUserIdFromToken(String refreshToken){
        return Long.parseLong(
                Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(refreshToken)
                        .getBody()
                        .getSubject()
        );
    }
}
