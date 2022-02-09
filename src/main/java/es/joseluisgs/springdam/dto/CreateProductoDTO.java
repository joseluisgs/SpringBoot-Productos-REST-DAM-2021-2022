package es.joseluisgs.springdam.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

// Getter & setter
public class CreateProductoDTO {
    private String nombre;
    private double precio;
    private int stock;
}
