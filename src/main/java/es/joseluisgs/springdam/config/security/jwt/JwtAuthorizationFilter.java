package es.joseluisgs.springdam.config.security.jwt;

import es.joseluisgs.springdam.models.Usuario;
import es.joseluisgs.springdam.services.users.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;

    // Comprueba la autorización a través del token
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // Sacamos el token
            String token = getJwtFromRequest(request);
            // Si el token existe y es válido
            if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
                // Obtenemos su ID
                Long userId = tokenProvider.getUserIdFromJWT(token);
                // Lo buscamos
                Usuario user = (Usuario) userDetailsService.loadUserById(userId);
                // Obtenemos la auteticación encapsulada del token: usuario, roles, y las autorizaciones.
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
                        user.getRoles(), user.getAuthorities());
                // le vamos a pasar información detro del contexto: dirección remota, session ID, etc.
                authentication.setDetails(new WebAuthenticationDetails(request));
                // Guardamos este objeto autetificación en elcontexto de seguridad.
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        } catch (Exception ex) {
            log.info("No se ha podido establecer la autenticación de usuario en el contexto de seguridad");
        }

        filterChain.doFilter(request, response);

    }

    // Procesamos el Token del Request
    private String getJwtFromRequest(HttpServletRequest request) {
        // Tomamos la cabecera
        String bearerToken = request.getHeader(JwtTokenProvider.TOKEN_HEADER);
        // Si tiene el prefijo y es de la logitud indicada
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtTokenProvider.TOKEN_PREFIX)) {
            return bearerToken.substring(JwtTokenProvider.TOKEN_PREFIX.length());
        }
        return null;
    }

}
