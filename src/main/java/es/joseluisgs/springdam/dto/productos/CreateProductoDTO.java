package es.joseluisgs.springdam.dto.productos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter

// Getter & setter
public class CreateProductoDTO {
    @NotBlank
    private String nombre;
    private double precio;
    private int stock;
}
