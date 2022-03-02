package es.joseluisgs.springdam.repositories.productos;

import es.joseluisgs.springdam.models.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// Creamos el repositorio extendéndolo de JPA, siguiendo DAO
// Con ello ya tenemos las operaciones básicas de CRUD y Paginación y Queries
public interface ProductosRepository extends JpaRepository<Producto, Long> {

    // Buscamos los productos por su nombre
    List<Producto> findByNombre(String nombre);

    // Por nombre y En Páginas
    List<Producto> findByNombreContainsIgnoreCase(String nombre);

    Page<Producto> findByNombreContainsIgnoreCase(String nombre, Pageable pageable);

    Page<Producto> findByPrecioGreaterThanEqualOrderByNombreAsc(double precio, Pageable pageable);

    Page<Producto> findByNombreContainsIgnoreCaseAndPrecioGreaterThanEqualOrderByNombreAsc(String nombre, double precio, Pageable pageable);

}
