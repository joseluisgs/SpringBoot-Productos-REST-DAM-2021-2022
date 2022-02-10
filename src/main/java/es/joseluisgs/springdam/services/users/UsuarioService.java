package es.joseluisgs.springdam.services.users;

import es.joseluisgs.springdam.dto.usuarios.CreateUsuarioDTO;
import es.joseluisgs.springdam.errors.usuarios.NewUserWithDifferentPasswordsException;
import es.joseluisgs.springdam.models.Usuario;
import es.joseluisgs.springdam.models.UsuarioRol;
import es.joseluisgs.springdam.repositories.usuarios.UsuariosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
// OJO la inyeccion de dependencias es a modo de constructor al poner @RequiredArgsConstructor
public class UsuarioService {
    private final UsuariosRepository usuariosRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Nos permite buscar un usuario por su nombre de usuario
     */
    public Optional<Usuario> findUserByUsername(String username) {
        return usuariosRepository.findByUsername(username);
    }

    public Optional<Usuario> findUserById(Long userId) {
        return usuariosRepository.findById(userId);
    }

    /**
     * Nos permite crear un nuevo Usuario con rol USER
     */
    public Usuario nuevoUsuario(CreateUsuarioDTO newUser) {
        System.out.println(passwordEncoder.encode(newUser.getPassword()));
        if (newUser.getPassword().contentEquals(newUser.getPassword2())) {
            Usuario usuario = Usuario.builder()
                    .username(newUser.getUsername())
                    .password(passwordEncoder.encode(newUser.getPassword()))
                    .avatar(newUser.getAvatar())
                    .fullName(newUser.getFullname()).email(newUser.getEmail())
                    .roles(Stream.of(UsuarioRol.USER).collect(Collectors.toSet()))
                    .build();
            try {
                return usuariosRepository.save(usuario);
            } catch (DataIntegrityViolationException ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de usuario ya existe");
            }
        } else {
            throw new NewUserWithDifferentPasswordsException();
        }

    }

}
