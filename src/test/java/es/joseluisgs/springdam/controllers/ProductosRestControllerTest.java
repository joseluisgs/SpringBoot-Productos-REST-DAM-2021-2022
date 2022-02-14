package es.joseluisgs.springdam.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.joseluisgs.springdam.controllers.productos.ProductosRestController;
import es.joseluisgs.springdam.dto.productos.CreateProductoDTO;
import es.joseluisgs.springdam.dto.productos.ProductoDTO;
import es.joseluisgs.springdam.mappers.ProductoMapper;
import es.joseluisgs.springdam.models.Producto;
import es.joseluisgs.springdam.repositories.productos.ProductosRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

// https://www.baeldung.com/integration-testing-in-spring
// https://lankydan.dev/2017/03/26/testing-data-transfer-objects-and-rest-controllers-in-spring-boot
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@Import(ProductosRestController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductosRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    private ProductosRepository productosRepository;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private JacksonTester<CreateProductoDTO> jsonCreateProductoDTO;

    @Autowired
    private JacksonTester<ProductoDTO> jsonProductoDTO;

    @Autowired
    private ProductoMapper productoMapper;


    @Test
    @Order(1)
    public void findAll() throws Exception {

        MockHttpServletResponse response = mockMvc.perform(
                        get("/rest/productos/")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertTrue(response.getContentAsString().contains("\"id\":1"));
        ObjectMapper mapper = new ObjectMapper();
        List<ProductoDTO> myObjects = Arrays.asList(mapper.readValue(response.getContentAsString(), ProductoDTO[].class));
        assertTrue(myObjects.size() > 0);
        assertEquals(myObjects.get(0).getId(), 1);
        assertTrue(myObjects.get(0).getNombre().contains("Zumo de Naranja"));


    }

    @Test
    @Order(2)
    public void findById() throws Exception {
        Producto producto = Producto.builder()
                .nombre("Zumo de Naranja")
                .precio(9.5)
                .stock(25)
                .build();

        // when
        // Mockito.when(productosRepository.findById(1L)).thenReturn(Optional.of(producto));

        // do
        MockHttpServletResponse response = mockMvc.perform(
                        get("/rest/productos/1")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        ProductoDTO res = jsonProductoDTO.parseObject(response.getContentAsString());
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(res.getNombre(), producto.getNombre());
        assertEquals(res.getPrecio(), producto.getPrecio());
        assertEquals(res.getStock(), producto.getStock());

        // Mockito.verify(productosRepository, times(1)).findById(1L);
    }

    @Test
    @Order(3)
    public void save() throws Exception {
        CreateProductoDTO createProductoDTO = new CreateProductoDTO();
        createProductoDTO.setNombre("POST");
        createProductoDTO.setPrecio(10.0);
        createProductoDTO.setStock(10);

        // Producto producto = productoMapper.fromDTO(createProductoDTO);

        // when
        // Mockito.when(productosRepository.findById(1L)).thenReturn(Optional.of(producto));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/rest/productos/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCreateProductoDTO.write(createProductoDTO).getJson())
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        ProductoDTO res = jsonProductoDTO.parseObject(response.getContentAsString());
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(res.getNombre(), createProductoDTO.getNombre());
        assertEquals(res.getPrecio(), createProductoDTO.getPrecio());
        assertEquals(res.getStock(), createProductoDTO.getStock());

        // Mockito.verify(productosRepository, times(1)).save(producto);

    }

    @Test
    @Order(4)
    public void update() throws Exception {
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setNombre("PUT");
        productoDTO.setPrecio(10.0);
        productoDTO.setStock(10);

        // Producto producto = productoMapper.fromDTO(productoDTO);

        // when
        // Mockito.when(productosRepository.findById(1L)).thenReturn(Optional.of(producto));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.put("/rest/productos/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonProductoDTO.write(productoDTO).getJson())
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        ProductoDTO res = jsonProductoDTO.parseObject(response.getContentAsString());
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(res.getNombre(), productoDTO.getNombre());
        assertEquals(res.getPrecio(), productoDTO.getPrecio());
        assertEquals(res.getStock(), productoDTO.getStock());

        // Mockito.verify(productosRepository, times(1)).save(producto);
    }

    @Test
    @Order(5)
    public void delete() throws Exception {
        Producto producto = Producto.builder()
                .nombre("Zumo de Naranja")
                .precio(9.5)
                .stock(25)
                .build();

        // Producto producto = productoMapper.fromDTO(productoDTO);

        // when
        // Mockito.when(productosRepository.findById(1L)).thenReturn(Optional.of(producto));

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete("/rest/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        ProductoDTO res = jsonProductoDTO.parseObject(response.getContentAsString());
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(res.getNombre(), producto.getNombre());
        assertEquals(res.getPrecio(), producto.getPrecio());
        assertEquals(res.getStock(), producto.getStock());

        // Mockito.verify(productosRepository, times(1)).save(producto);
    }


}
