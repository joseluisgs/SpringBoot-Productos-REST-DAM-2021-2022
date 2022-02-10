package es.joseluisgs.springdam.config.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // Para mapear.
    private final ObjectMapper mapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // Estado de la respuesta a no autorizado, pero como JSON
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        // Construimos nuestro error con el mensaje de la excepción
        Map<HttpStatus, String> error = Map.of(HttpStatus.UNAUTHORIZED, authException.getMessage());
        String strApiError = mapper.writeValueAsString(error);

        // Lo devolvemos
        PrintWriter writer = response.getWriter();
        writer.println(strApiError);

    }

}
