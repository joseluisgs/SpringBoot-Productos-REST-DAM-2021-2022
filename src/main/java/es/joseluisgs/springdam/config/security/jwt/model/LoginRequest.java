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
    @NotBlank(message = "El campo username no puede estar vacío")
    private String username;

    @NotBlank(message = "El campo password no puede estar vacío")
    private String password;

}
