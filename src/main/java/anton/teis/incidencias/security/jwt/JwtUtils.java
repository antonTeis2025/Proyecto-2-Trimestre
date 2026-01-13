package anton.teis.incidencias.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    // todo: cambiar el secret a un fichero de propiedades
    private static final String SECRET = "TeisTeisTeisTeisTeisTeisTeisTeisTeisTeisTeisTeis";
    private static final long EXPIRATION_TIME = 864_000_000; // expiracion de 10 días

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    /**
     * Genera un token para un usuario
     * @param username
     * @return
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Obtiene el usuario a partir del token
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * valida el token
     * @param token
     * @return
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}