package es.joseluisgs.springdam.controllers;

import es.joseluisgs.springdam.controllers.productos.ProductosRestController;
import es.joseluisgs.springdam.dto.productos.CreateProductoDTO;
import es.joseluisgs.springdam.dto.productos.ProductoDTO;
import es.joseluisgs.springdam.mappers.ProductoMapper;
import es.joseluisgs.springdam.models.Producto;
import es.joseluisgs.springdam.repositories.productos.ProductosRepository;
import es.joseluisgs.springdam.services.uploads.StorageService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


// Ejecutar uno a uno
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PoructosControllerMockTest {
    @Mock
    private static ProductosRepository productosRepository;
    // SUT: System Under Test
    @InjectMocks
    private static ProductosRestController productosController;
    private final Producto producto = Producto.builder()
            .nombre("Producto de prueba")
            .id(1L)
            .precio(10.0)
            .stock(10)
            .build();
    @Mock
    private ProductoMapper productoMapper;
    @Mock
    private StorageService storageService;

    @BeforeAll
    static void setUp() {

        //Creamos el mock... Sintáxis mock con Mockito
        //productosRepository = Mockito.mock(ProductosRepository.class);
        //var fileStorage = Mockito.mock(StorageService.class);
        //var mapper = Mockito.mock(ProductoMapper.class);
        // Creamos el SUT con su mock
        //productosController = new ProductosRestController(productosRepository, fileStorage, mapper);
    }

    @Test
    @Order(1)
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
    @Order(2)
    void getByIdTestMock() {
        var dto = ProductoDTO.builder()
                .nombre(producto.getNombre())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .build();

        Mockito.when(productosRepository.findById(1L))
                .thenReturn(java.util.Optional.of(producto));

        Mockito.when(productoMapper.toDTO(producto)).thenReturn(dto);

        var response = productosController.findById(1L);
        var res = response.getBody();

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode().value()),
                () -> assertEquals(res.getNombre(), producto.getNombre()),
                () -> assertEquals(res.getPrecio(), producto.getPrecio()),
                () -> assertEquals(res.getStock(), producto.getStock())
        );
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
    }
}
