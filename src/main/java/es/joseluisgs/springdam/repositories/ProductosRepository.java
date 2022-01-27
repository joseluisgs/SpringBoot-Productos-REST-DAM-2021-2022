package es.joseluisgs.springdam.repositories;

import es.joseluisgs.springdam.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

// Creamos el repositorio extendéndolo de JPA, siguiendo DAO
// Con ello ya tenemos las operaciones básicas de CRUD y Paginación y Queries
public interface ProductosRepository extends JpaRepository<Producto, Long> {
}
