package es.joseluisgs.springdam.dto.usuarios;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUsuarioDTO {

    private String username;
    private String avatar;
    private String fullname;
    private String email;
    private String password;
    private String password2;

}
