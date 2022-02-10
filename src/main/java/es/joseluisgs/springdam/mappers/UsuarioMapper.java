package es.joseluisgs.springdam.mappers;

import es.joseluisgs.springdam.dto.usuarios.GetUsuarioDTO;
import es.joseluisgs.springdam.models.Usuario;
import es.joseluisgs.springdam.models.UsuarioRol;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
// esta ez lo voy a hacer sin usar Mapper y usando Builder
public class UsuarioMapper {
    public GetUsuarioDTO toDTO(Usuario user) {
        return GetUsuarioDTO.builder()
                .username(user.getUsername())
                .avatar(user.getAvatar())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(UsuarioRol::name)
                        .collect(Collectors.toSet())
                ).build();
    }
}
