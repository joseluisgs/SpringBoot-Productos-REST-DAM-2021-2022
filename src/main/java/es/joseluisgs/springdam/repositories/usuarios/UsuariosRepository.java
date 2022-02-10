package es.joseluisgs.springdam.repositories.usuarios;

import es.joseluisgs.springdam.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuariosRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
}
