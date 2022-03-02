package es.joseluisgs.springdam.controllers;

import es.joseluisgs.springdam.controllers.productos.ProductosRestController;
import es.joseluisgs.springdam.dto.productos.CreateProductoDTO;
import es.joseluisgs.springdam.dto.productos.ProductoDTO;
import es.joseluisgs.springdam.errors.productos.ProductoBadRequestException;
import es.joseluisgs.springdam.errors.productos.ProductoNotFoundException;
import es.joseluisgs.springdam.mappers.ProductoMapper;
import es.joseluisgs.springdam.models.Producto;
import es.joseluisgs.springdam.repositories.productos.ProductosRepository;
import es.joseluisgs.springdam.services.uploads.StorageService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


// Ejecutar uno a uno
// Vamos a moquear todo!!!
@SpringBootTest
// @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PoructosControllerMockTest {
    // Mis mocks
    @MockBean
    private final ProductosRepository productosRepository;
    @MockBean
    private final StorageService storageService;
    @MockBean
    private final ProductoMapper productoMapper;
    private final Producto producto = Producto.builder()
            .nombre("Producto de prueba")
            .id(1L)
            .precio(10.0)
            .stock(10)
            .build();
    // SUT: System Under Test
    @InjectMocks
    private ProductosRestController productosController;

    // Debemos decir como va a ser la inyección!!!!
    @Autowired
    public PoructosControllerMockTest(ProductosRepository productosRepository, StorageService storageService, ProductoMapper productoMapper) {
        this.productosRepository = productosRepository;
        this.storageService = storageService;
        this.productoMapper = productoMapper;
    }

    @Test
    void getAllTestMock() {
        var dto = ProductoDTO.builder()
                .nombre(producto.getNombre())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .build();

        Mockito.when(productosRepository.findAll())
                .thenReturn(List.of(producto));

        Mockito.when(productoMapper.toDTO(List.of(producto))).thenReturn(List.of(dto));

        var response = productosController.findAll(
                java.util.Optional.empty(), java.util.Optional.empty()
        );
        var res = response.getBody();

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode().value()),
                () -> assertEquals(res.get(0).getNombre(), producto.getNombre()),
                () -> assertEquals(res.get(0).getPrecio(), producto.getPrecio()),
                () -> assertEquals(res.get(0).getStock(), producto.getStock())
        );
    }

    @Test
    void getByIdTestMock() {
        var dto = ProductoDTO.builder()
                .nombre(producto.getNombre())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .build();

        Mockito.when(productosRepository.findById(1L))
                .thenReturn(Optional.of(producto));

        Mockito.when(productoMapper.toDTO(producto)).thenReturn(dto);

        var response = productosController.findById(1L);
        var res = response.getBody();

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode().value()),
                () -> assertEquals(res.getNombre(), producto.getNombre()),
                () -> assertEquals(res.getPrecio(), producto.getPrecio()),
                () -> assertEquals(res.getStock(), producto.getStock())
        );

        Mockito.verify(productosRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(productoMapper, Mockito.times(1)).toDTO(producto);
    }

    @Test
    void findByIdException() {
        Mockito.when(productosRepository.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(ProductoNotFoundException.class, () -> {
            productosController.findById(1L);
        });

        assertTrue(ex.getMessage().contains("producto"));

        Mockito.verify(productosRepository, Mockito.times(1))
                .findById(1L);
    }

    @Test
    @Order(3)
    void saveTestMock() {
        var createDto = CreateProductoDTO.builder()
                .nombre(producto.getNombre())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .build();

        var dto = ProductoDTO.builder()
                .nombre(producto.getNombre())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .build();

        Mockito.when(productosRepository.save(producto))
                .thenReturn(producto);

        Mockito.when(productoMapper.fromDTO(createDto))
                .thenReturn(producto);

        Mockito.when(productoMapper.toDTO(producto)).thenReturn(dto);

        var response = productosController.save(createDto);
        var res = response.getBody();

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode().value()),
                () -> assertEquals(res.getNombre(), producto.getNombre()),
                () -> assertEquals(res.getPrecio(), producto.getPrecio()),
                () -> assertEquals(res.getStock(), producto.getStock())
        );

        Mockito.verify(productosRepository, Mockito.times(1))
                .save(producto);
        Mockito.verify(productoMapper, Mockito.times(1))
                .fromDTO(createDto);
        Mockito.verify(productoMapper, Mockito.times(1))
                .toDTO(producto);
    }

    @Test
    void checkProductoDataNameExceptionTest() {
        var createDto = CreateProductoDTO.builder()
                .nombre("")
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .build();

        var prod = Producto.builder()
                .nombre("")
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .build();

        Mockito.when(productoMapper.fromDTO(createDto)).thenReturn(prod);

        Exception ex = assertThrows(ProductoBadRequestException.class, () -> {
            productosController.save(createDto);
        });

        assertTrue(ex.getMessage().contains("Nombre"));

        Mockito.verify(productoMapper, Mockito.times(1))
                .fromDTO(createDto);
    }

    @Test
    void checkProductoDataPrecioExceptionTest() {
        var createDto = CreateProductoDTO.builder()
                .nombre(producto.getNombre())
                .precio(-19.9)
                .stock(producto.getStock())
                .build();

        var prod = Producto.builder()
                .nombre(producto.getNombre())
                .precio(-19.9)
                .stock(producto.getStock())
                .build();

        Mockito.when(productoMapper.fromDTO(createDto)).thenReturn(prod);

        Exception ex = assertThrows(ProductoBadRequestException.class, () -> {
            productosController.save(createDto);
        });

        assertTrue(ex.getMessage().contains("Precio"));

        Mockito.verify(productoMapper, Mockito.times(1))
                .fromDTO(createDto);
    }

    @Test
    void checkProductoDataStockExceptionTest() {
        var createDto = CreateProductoDTO.builder()
                .nombre(producto.getNombre())
                .precio(producto.getPrecio())
                .stock(-1)
                .build();

        var prod = Producto.builder()
                .nombre(producto.getNombre())
                .precio(producto.getPrecio())
                .stock(-1)
                .build();

        Mockito.when(productoMapper.fromDTO(createDto)).thenReturn(prod);

        Exception ex = assertThrows(ProductoBadRequestException.class, () -> {
            productosController.save(createDto);
        });

        assertTrue(ex.getMessage().contains("Stock"));

        Mockito.verify(productoMapper, Mockito.times(1))
                .fromDTO(createDto);
    }

    @Test
    @Order(4)
    void updateTestMock() {
        var dto = ProductoDTO.builder()
                .nombre(producto.getNombre())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .build();

        Mockito.when(productosRepository.findById(1L))
                .thenReturn(java.util.Optional.of(producto));

        Mockito.when(productosRepository.save(producto))
                .thenReturn(producto);

        Mockito.when(productoMapper.toDTO(producto)).thenReturn(dto);

        var response = productosController.update(1L, producto);
        var res = response.getBody();

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode().value()),
                () -> assertEquals(res.getNombre(), producto.getNombre()),
                () -> assertEquals(res.getPrecio(), producto.getPrecio()),
                () -> assertEquals(res.getStock(), producto.getStock())
        );

        Mockito.verify(productosRepository, Mockito.times(1))
                .findById(1L);
        Mockito.verify(productosRepository, Mockito.times(1))
                .save(producto);
        Mockito.verify(productoMapper, Mockito.times(1))
                .toDTO(producto);
    }

    @Test
    @Order(5)
    void deleteTestMock() {
        var dto = ProductoDTO.builder()
                .nombre(producto.getNombre())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .build();

        Mockito.when(productosRepository.findById(1L))
                .thenReturn(java.util.Optional.of(producto));

        Mockito.when(productoMapper.toDTO(producto)).thenReturn(dto);

        var response = productosController.delete(1L);
        var res = response.getBody();

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode().value()),
                () -> assertEquals(res.getNombre(), producto.getNombre()),
                () -> assertEquals(res.getPrecio(), producto.getPrecio()),
                () -> assertEquals(res.getStock(), producto.getStock())
        );

        Mockito.verify(productosRepository, Mockito.times(1))
                .findById(1L);
        Mockito.verify(productoMapper, Mockito.times(1))
                .toDTO(producto);
    }
}
