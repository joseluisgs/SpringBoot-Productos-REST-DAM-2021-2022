package es.joseluisgs.springdam.config.security.jwt.model;

import es.joseluisgs.springdam.dto.usuarios.GetUsuarioDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

// Respuesta al loguearte con los datos del usuario, es el JSON...
@Getter
@Setter
@NoArgsConstructor
public class JwtUserResponse extends GetUsuarioDTO {

    private String token;

    @Builder(builderMethodName = "jwtUserResponseBuilder") // Lo llamos así por tener dos builder en dos clases.
    // Le añadimos el token
    public JwtUserResponse(String username, String avatar, String fullName, String email, Set<String> roles, String token) {
        super(username, avatar, fullName, email, roles);
        this.token = token;
    }
}