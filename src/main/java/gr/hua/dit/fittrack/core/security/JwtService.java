package gr.hua.dit.fittrack.core.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${app.jwt.secret:change-me-secret}")
    private String secretKeyString;

    @Value("${app.jwt.validity-ms:86400000}") // 24 ώρες
    private long validityInMs;

    // ----------------- ΒΑΣΙΚΟ: Δημιουργία token -----------------
    public String generateToken(String email, String role) {

        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMs);

        return Jwts.builder()
                .setSubject(email)            // ποιος είναι ο χρήστης
                .claim("role", role)          // ρόλος στο σύστημά σου
                .setIssuedAt(now)             // πότε εκδόθηκε
                .setExpiration(expiry)        // πότε λήγει
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ----------------- Χρήσιμες μέθοδοι ανάγνωσης -----------------

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // ----------------- Εσωτερικά helpers -----------------

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        // Χρησιμοποιεί το ίδιο secret που είχες πριν
        return Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }
}
