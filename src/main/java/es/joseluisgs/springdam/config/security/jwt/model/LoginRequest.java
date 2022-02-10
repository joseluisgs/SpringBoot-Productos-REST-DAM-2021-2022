package es.joseluisgs.springdam.config.security.jwt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor

// Para el login
public class LoginRequest {
    @NotBlank // No envíen datos en blanco. Usamos la api de validación!!! Ideal para otros DTO de Entrada
    private String username;
    @NotBlank
    private String password;

}
