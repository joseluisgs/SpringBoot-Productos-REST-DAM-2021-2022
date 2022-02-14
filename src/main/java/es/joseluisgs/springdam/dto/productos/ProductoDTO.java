package es.joseluisgs.springdam.dto.productos;

import lombok.*;

// Los DTO nos sirven entre otras cosas para filtrar información de una o varias clases, podría ser similar a las vistas
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDTO {
    private Long id;
    private String nombre;
    private double precio;
    private int stock;
    private String imagen;
    private String createdAt;
    // Por ejemplo sin en modelo de prodyuto este tuviese una categoria, y solo queremos devolver su nombre
    // Lo haríamos aquí, y luego en e mappers es donde cogeríamos el nombre. Ver...
    //private String categoria;
}
