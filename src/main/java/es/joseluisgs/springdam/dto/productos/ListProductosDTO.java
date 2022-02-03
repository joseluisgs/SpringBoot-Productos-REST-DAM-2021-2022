package es.joseluisgs.springdam.dto.productos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ListProductosDTO {
    LocalDateTime consulta = LocalDateTime.now();
    String project = "SpringDam";
    String version = "1.0";
    List<ProductoDTO> data;
}