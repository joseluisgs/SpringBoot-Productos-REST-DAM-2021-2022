package es.joseluisgs.springdam.dto.productos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// Los DTO nos sirven entre otras cosas para filtrar información de una o varias clases, podría ser similar a las vistas
@Getter
@Setter

public class ProductoDTO {
    private Long id;
    private String nombre;
    private double precio;
    private int stock;
    private String imagen;
    private LocalDateTime createdAt;
    // Por ejemplo sin en modelo de prodyuto este tuviese una categoria, y solo queremos devolver su nombre
    // Lo haríamos aquí, y luego en e mapper es donde cogeríamos el nombre. Ver...
    //private String categoria;
}
