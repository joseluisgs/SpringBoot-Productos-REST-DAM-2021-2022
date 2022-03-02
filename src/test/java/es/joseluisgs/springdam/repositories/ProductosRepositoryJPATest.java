package es.joseluisgs.springdam.repositories;

import es.joseluisgs.springdam.models.Producto;
import es.joseluisgs.springdam.repositories.productos.ProductosRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

// Una manera de porbar nuestro repositorio si es JPA
// Con los metodos nuevos que hayamos añadido

@SpringBootTest
@Transactional
@TypeExcludeFilters(value= DataJpaTypeExcludeFilter.class)
@AutoConfigureCache
@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@ImportAutoConfiguration
// Todo eso es @DataJpaTest
public class ProductosRepositoryJPATest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductosRepository productosRepository;

    private final  Producto producto = Producto.builder()
            .nombre("Producto Test")
            .precio(10.0)
            .stock(10)
            .build();

    @Test
    public void getAllTest() {
        entityManager.persist(producto);
        entityManager.flush();

        assertTrue(productosRepository.findAll().size() > 0);

    }

    @Test
    public void getByIdTest() {
        entityManager.persist(producto);
        entityManager.flush();

        Producto found = productosRepository.findById(producto.getId()).get();
        assertAll(
                () -> assertEquals(producto.getNombre(), found.getNombre()),
                () -> assertEquals(producto.getPrecio(), found.getPrecio())
        );
    }

    @Test
    public void testFindByNombre() {
        entityManager.persist(producto);
        entityManager.flush();

        Producto found = productosRepository.findByNombre(producto.getNombre()).get(0);
        assertAll(
                () -> assertEquals(producto.getNombre(), found.getNombre()),
                () -> assertEquals(producto.getPrecio(), found.getPrecio())
        );
    }

    @Test
    public void findByNombreContainsIgnoreCase() {
        entityManager.persist(producto);
        entityManager.flush();

        Producto found = productosRepository.findByNombreContainsIgnoreCase(producto.getNombre()).get(0);
        assertAll(
                () -> assertEquals(producto.getNombre(), found.getNombre()),
                () -> assertEquals(producto.getPrecio(), found.getPrecio())
        );
    }

    @Test
    public void save() {
        Producto saved = productosRepository.save(producto);
        assertAll(
                () -> assertEquals(producto.getNombre(), saved.getNombre()),
                () -> assertEquals(producto.getPrecio(), saved.getPrecio())
        );
    }

    @Test
    public void update() {
        entityManager.persist(producto);
        entityManager.flush();

        Producto found = productosRepository.findById(producto.getId()).get();
        found.setNombre("Producto Test Updated");
        found.setPrecio(20.0);
        Producto updated = productosRepository.save(found);
        assertAll(
                () -> assertEquals(producto.getNombre(), updated.getNombre()),
                () -> assertEquals(producto.getPrecio(), updated.getPrecio())
        );
    }

    @Test
    public void delete() {
        entityManager.persist(producto);
        entityManager.flush();
        var  res = productosRepository.findById(producto.getId()).get();
        productosRepository.delete(producto);
        res = productosRepository.findById(producto.getId()).orElse(null);
        assertNull(res);
    }
}
