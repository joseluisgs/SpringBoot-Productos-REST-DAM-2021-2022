package es.joseluisgs.springdam.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.joseluisgs.springdam.dto.productos.CreateProductoDTO;
import es.joseluisgs.springdam.dto.productos.ProductoDTO;
import es.joseluisgs.springdam.models.Producto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// https://www.baeldung.com/integration-testing-in-spring
// https://medium.com/@tbrouwer/testing-a-spring-boot-restful-service-c61b981cac61
// https://lankydan.dev/2017/03/26/testing-data-transfer-objects-and-rest-controllers-in-spring-boot
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductosRestControllerTest {

    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private JacksonTester<CreateProductoDTO> jsonCreateProductoDTO;
    @Autowired
    private JacksonTester<ProductoDTO> jsonProductoDTO;

    @Test
    @Order(1)
    public void findAllTest() throws Exception {

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
    public void findByIdTest() throws Exception {
        Producto producto = Producto.builder()
                .nombre("Zumo de Naranja")
                .precio(9.5)
                .stock(25)
                .build();

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

    }

    @Test
    @Order(3)
    public void saveTest() throws Exception {
        CreateProductoDTO createProductoDTO = new CreateProductoDTO();
        createProductoDTO.setNombre("POST");
        createProductoDTO.setPrecio(10.0);
        createProductoDTO.setStock(10);


        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/rest/productos/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCreateProductoDTO.write(createProductoDTO).getJson())
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        ProductoDTO res = jsonProductoDTO.parseObject(response.getContentAsString());
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(res.getNombre(), createProductoDTO.getNombre());
        assertEquals(res.getPrecio(), createProductoDTO.getPrecio());
        assertEquals(res.getStock(), createProductoDTO.getStock());

    }

    @Test
    @Order(4)
    public void updateTest() throws Exception {
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setNombre("PUT");
        productoDTO.setPrecio(10.0);
        productoDTO.setStock(10);

        MockHttpServletResponse response = mockMvc.perform(put("/rest/productos/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonProductoDTO.write(productoDTO).getJson())
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        ProductoDTO res = jsonProductoDTO.parseObject(response.getContentAsString());
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(res.getNombre(), productoDTO.getNombre());
        assertEquals(res.getPrecio(), productoDTO.getPrecio());
        assertEquals(res.getStock(), productoDTO.getStock());

    }

    @Test
    @Order(5)
    public void deleteTest() throws Exception {
        Producto producto = Producto.builder()
                .nombre("Zumo de Naranja")
                .precio(9.5)
                .stock(25)
                .build();

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.delete("/rest/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        ProductoDTO res = jsonProductoDTO.parseObject(response.getContentAsString());
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        assertEquals(res.getNombre(), producto.getNombre());
        assertEquals(res.getPrecio(), producto.getPrecio());
        assertEquals(res.getStock(), producto.getStock());

    }

    @Test
    @Order(6)
    public void findAllAlternativeTest() throws Exception {
        MvcResult result = mockMvc.perform(get("/rest/productos/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre", is("PUT")))
                .andExpect(jsonPath("$[0].precio", is(10.0)))
                .andReturn();
    }

    @Test
    @Order(7)
    public void findByIdlternativeTest() throws Exception {
        MvcResult result = mockMvc.perform(get("/rest/productos/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("PUT")))
                .andExpect(jsonPath("$.id", is(2)))
                .andReturn();
    }

    @Test
    @Order(8)
    public void postAlternativeTest() throws Exception {
        CreateProductoDTO createProductoDTO = new CreateProductoDTO();
        createProductoDTO.setNombre("POST");
        createProductoDTO.setPrecio(10.0);
        createProductoDTO.setStock(10);

        byte[] json = jsonCreateProductoDTO.write(createProductoDTO).getJson().getBytes();

        MvcResult result = mockMvc.perform(post("/rest/productos/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("POST")))
                .andExpect(jsonPath("$.precio", is(10.0)))
                .andExpect(jsonPath("$.stock", is(10)))
                .andReturn();
    }

    @Test
    @Order(9)
    public void putAlternativeTest() throws Exception {
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setNombre("PUT");
        productoDTO.setPrecio(10.0);
        productoDTO.setStock(10);

        byte[] json = jsonProductoDTO.write(productoDTO).getJson().getBytes();

        MvcResult result = mockMvc.perform(put("/rest/productos/2")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("PUT")))
                .andExpect(jsonPath("$.precio", is(10.0)))
                .andExpect(jsonPath("$.stock", is(10)))
                .andReturn();
    }

    @Test
    @Order(10)
    public void deleteAlternativeTest() throws Exception {
        mockMvc.perform(delete("/rest/productos/2"))
                .andExpect(status().isOk())
                .andReturn();
    }


}
