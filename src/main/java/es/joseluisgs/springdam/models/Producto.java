package es.joseluisgs.springdam.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Nombre no puede ser vacío")
    private String nombre;

    @NotNull(message = "Precio no puede ser nulo")
    @Min(value = 0, message = "El precio no puede ser negativo")
    private Double precio;

    @NotNull(message = "Stock no puede ser nulo")
    @Min(value = 0, message = "El precio no puede ser negativo")
    private Integer stock;

    private String imagen;
    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

}

