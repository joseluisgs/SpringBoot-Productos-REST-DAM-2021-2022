package es.joseluisgs.springdam.config.security.jwt;

import es.joseluisgs.springdam.models.Usuario;
import es.joseluisgs.springdam.models.UsuarioRol;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Log
@Component
public class JwtTokenProvider {
    // Naturaleza del Token!!!
    public static final String TOKEN_HEADER = "Authorization"; // Encabezado
    public static final String TOKEN_PREFIX = "Bearer "; // Prefijo, importante este espacio
    public static final String TOKEN_TYPE = "JWT"; // Tipo de Token

    @Value("${jwt.secret:EnUnLugarDeLaManchaDeCuyoNombreNoQuieroAcordarmeNoHaMuchoTiempoQueViviaUnHidalgo}")
    private String jwtSecreto; // Secreto, lo cargamos de properties y si no le asignamos un valor por defecto

    @Value("${jwt.token-expiration:86400}")
    private int jwtDuracionTokenEnSegundos; // Tiempo de expiración, idem a secreto


    // Genera el Token
    public String generateToken(Authentication authentication) {

        // Obtenemos el usuario
        Usuario user = (Usuario) authentication.getPrincipal();

        // Creamos el timepo de vida del token, fecha en milisegunods (*1000) Fecha del sistema
        // Mas duración del token
        Date tokenExpirationDate = new Date(System.currentTimeMillis() + (jwtDuracionTokenEnSegundos * 1000));

        // Construimos el token con sus datos y payload
        return Jwts.builder()
                // Lo firmamos con nuestro secreto HS512
                .signWith(Keys.hmacShaKeyFor(jwtSecreto.getBytes()), SignatureAlgorithm.HS512)
                // Tipo de token
                .setHeaderParam("typ", TOKEN_TYPE)
                // Como Subject el ID del usuario
                .setSubject(Long.toString(user.getId()))
                // Fecha actual
                .setIssuedAt(new Date())
                // Fecha de expiración
                .setExpiration(tokenExpirationDate)
                // Payload o datos extra del token son claims
                // Nombre completo del usuario
                .claim("fullname", user.getFullName())
                // Le añadimos los roles o lo que queramos como payload: claims
                .claim("roles", user.getRoles().stream()
                        .map(UsuarioRol::name)
                        .collect(Collectors.joining(", "))
                )
                .compact();

    }

    // A partir de un token obetner el ID de usuario
    public Long getUserIdFromJWT(String token) {
        // Obtenemos los claims del token
        /*Claims claims = Jwts.parser()
                // Obtenemos la firma
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecreto.getBytes()))
                // Obtenemlos el cuerpo de los claims
                .parseClaimsJws(token)
                .getBody();
        // Devolvemos el ID
        return Long.parseLong(claims.getSubject());*/
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecreto.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
        // Devolvemos el ID
        return Long.parseLong(claims.getSubject());

    }

    // Nos idica como validar el Token
    public boolean validateToken(String authToken) {

        try {
            // Jwts.parser().setSigningKey(jwtSecreto.getBytes()).parseClaimsJws(authToken);
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecreto.getBytes()))
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.info("Error en la firma del token JWT: " + ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.info("Token malformado: " + ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.info("El token ha expirado: " + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.info("Token JWT no soportado: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.info("JWT claims vacío");
        }
        return false;
    }
}