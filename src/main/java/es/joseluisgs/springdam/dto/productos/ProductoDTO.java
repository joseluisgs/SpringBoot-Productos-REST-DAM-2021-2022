package es.joseluisgs.springdam.dto.productos;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

// Los DTO nos sirven entre otras cosas para filtrar información de una o varias clases, podría ser similar a las vistas
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDTO {
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @Min(message = "El precio no puede ser negativo", value = 0)
    private Double precio;

    @Min(message = "El Stock no puede ser negativo", value = 0)
    private Integer stock;

    private String imagen;
    private String createdAt;
    // Por ejemplo sin en modelo de prodyuto este tuviese una categoria, y solo queremos devolver su nombre
    // Lo haríamos aquí, y luego en e mappers es donde cogeríamos el nombre. Ver...
    //private String categoria;
}
