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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// https://www.baeldung.com/integration-testing-in-spring
// https://medium.com/@tbrouwer/testing-a-spring-boot-restful-service-c61b981cac61
// https://lankydan.dev/2017/03/26/testing-data-transfer-objects-and-rest-controllers-in-spring-boot
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
// Levanto la BBDD en cada test
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductosRestControllerTest {

    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    Producto producto = Producto.builder()
            .id(1L)  // El id de la BBDD
            .nombre("Zumo de Naranja")
            .precio(9.5)
            .stock(25)
            .build();
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

        ObjectMapper mapper = new ObjectMapper();
        List<ProductoDTO> myObjects = Arrays.asList(mapper.readValue(response.getContentAsString(), ProductoDTO[].class));

        assertAll(
                () -> assertEquals(response.getStatus(), HttpStatus.OK.value()),
                () -> assertTrue(response.getContentAsString().contains("\"id\":" + producto.getId())),
                () -> assertTrue(myObjects.size() > 0),
                () -> assertEquals(myObjects.get(0).getId(), producto.getId()),
                () -> assertEquals(myObjects.get(0).getNombre(), producto.getNombre()),
                () -> assertEquals(myObjects.get(0).getPrecio(), producto.getPrecio()),
                () -> assertEquals(myObjects.get(0).getStock(), producto.getStock())
        );
    }

    @Test
    @Order(2)
    public void findByIdTest() throws Exception {

        var response = mockMvc.perform(
                        get("/rest/productos/" + producto.getId())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        var res = jsonProductoDTO.parseObject(response.getContentAsString());

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(res.getNombre(), producto.getNombre()),
                () -> assertEquals(res.getPrecio(), producto.getPrecio()),
                () -> assertEquals(res.getStock(), producto.getStock())
        );
    }

    @Test
    @Order(3)
    public void saveTest() throws Exception {
        var createProductoDTO = CreateProductoDTO.builder()
                .nombre(producto.getNombre())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .build();


        var response = mockMvc.perform(MockMvcRequestBuilders.post("/rest/productos/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCreateProductoDTO.write(createProductoDTO).getJson())
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        var res = jsonProductoDTO.parseObject(response.getContentAsString());

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(res.getNombre(), producto.getNombre()),
                () -> assertEquals(res.getPrecio(), producto.getPrecio()),
                () -> assertEquals(res.getStock(), producto.getStock()),
                () -> assertEquals(res.getNombre(), createProductoDTO.getNombre()),
                () -> assertEquals(res.getPrecio(), createProductoDTO.getPrecio()),
                () -> assertEquals(res.getStock(), createProductoDTO.getStock())
        );
    }

    @Test
    @Order(4)
    public void updateTest() throws Exception {
        var productoDTO = ProductoDTO.builder()
                .nombre(producto.getNombre())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .build();

        var response = mockMvc.perform(put("/rest/productos/" + producto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonProductoDTO.write(productoDTO).getJson())
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        var res = jsonProductoDTO.parseObject(response.getContentAsString());

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(res.getNombre(), producto.getNombre()),
                () -> assertEquals(res.getPrecio(), producto.getPrecio()),
                () -> assertEquals(res.getStock(), producto.getStock()),
                () -> assertEquals(res.getNombre(), productoDTO.getNombre()),
                () -> assertEquals(res.getPrecio(), productoDTO.getPrecio()),
                () -> assertEquals(res.getStock(), productoDTO.getStock())
        );
    }

    @Test
    @Order(5)
    public void deleteTest() throws Exception {

        var response = mockMvc.perform(MockMvcRequestBuilders.delete("/rest/productos/" + producto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        var res = jsonProductoDTO.parseObject(response.getContentAsString());

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(res.getNombre(), producto.getNombre()),
                () -> assertEquals(res.getPrecio(), producto.getPrecio()),
                () -> assertEquals(res.getStock(), producto.getStock())
        );
    }

    @Test
    @Order(6)
    public void findAllAlternativeTest() throws Exception {
        mockMvc.perform(get("/rest/productos/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre", is(producto.getNombre())))
                .andExpect(jsonPath("$[0].precio", is(producto.getPrecio())))
                .andExpect(jsonPath("$[0].stock", is(producto.getStock())))
                .andReturn();
    }

    @Test
    @Order(7)
    public void findByIdlternativeTest() throws Exception {
        mockMvc.perform(get("/rest/productos/" + producto.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is(producto.getNombre())))
                .andExpect(jsonPath("$.precio", is(producto.getPrecio())))
                .andExpect(jsonPath("$.stock", is(producto.getStock())))
                .andReturn();
    }

    @Test
    @Order(8)
    public void postAlternativeTest() throws Exception {
        var createProductoDTO = CreateProductoDTO.builder()
                .nombre(producto.getNombre())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .build();


        var json = jsonCreateProductoDTO.write(createProductoDTO).getJson();

        mockMvc.perform(post("/rest/productos/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is(producto.getNombre())))
                .andExpect(jsonPath("$.precio", is(producto.getPrecio())))
                .andExpect(jsonPath("$.stock", is(producto.getStock())))
                .andReturn();
    }

    @Test
    @Order(9)
    public void updateAlternativeTest() throws Exception {
        var productoDTO = ProductoDTO.builder()
                .nombre(producto.getNombre())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .build();

        var json = jsonProductoDTO.write(productoDTO).getJson();

        mockMvc.perform(put("/rest/productos/" + producto.getId())
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is(producto.getNombre())))
                .andExpect(jsonPath("$.precio", is(producto.getPrecio())))
                .andExpect(jsonPath("$.stock", is(producto.getStock())))
                .andReturn();
    }

    @Test
    @Order(10)
    public void deleteAlternativeTest() throws Exception {
        mockMvc.perform(delete("/rest/productos/" + producto.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is(producto.getNombre())))
                .andExpect(jsonPath("$.precio", is(producto.getPrecio())))
                .andExpect(jsonPath("$.stock", is(producto.getStock())))
                .andReturn();
    }
}
