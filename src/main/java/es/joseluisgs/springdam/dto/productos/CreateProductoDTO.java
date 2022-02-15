package es.joseluisgs.springdam.dto.productos;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// Getter & setter
public class CreateProductoDTO {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @Min(message = "El precio no puede ser negativo", value = 0)
    private Double precio;

    @Min(message = "El stock no puede ser negativo", value = 0)
    private Integer stock;

}
