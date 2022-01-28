package es.joseluisgs.springdam.repositories;

import es.joseluisgs.springdam.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Creamos el repositorio extendéndolo de JPA, siguiendo DAO
// Con ello ya tenemos las operaciones básicas de CRUD y Paginación y Queries
public interface ProductosRepository extends JpaRepository<Producto, Long> {

    // Buscamos los productos por su nombre
    List<Producto> findByNombre(String nombre);

    List<Producto> findByNombreContainsIgnoreCase(String nombre);


}
