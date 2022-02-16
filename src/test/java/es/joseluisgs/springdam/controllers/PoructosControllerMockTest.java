package es.joseluisgs.springdam.controllers;

import es.joseluisgs.springdam.controllers.productos.ProductosRestController;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PoructosControllerMockTest {
    @Mock
    private static ProductosRepository productosRepository;
    // SUT: System Under Test
    @InjectMocks
    private static ProductosRestController productosController;
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
        var lista = List.of(
                Producto.builder()
                        .id(1L)
                        .nombre("Producto 1")
                        .precio(10.0)
                        .stock(10)
                        .build()
        );

        Mockito.when(productosRepository.findAll()).thenReturn(lista);

        var result = productosController.findAll(
                java.util.Optional.empty(), java.util.Optional.empty()
        );

        assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
        // System.out.println(result.getBody());

        Mockito.verify(productosRepository, Mockito.times(1)).findAll();
    }
}
