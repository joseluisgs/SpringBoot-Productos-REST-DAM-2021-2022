package es.joseluisgs.springdam.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
// Con el Builder me ahorro el mapper...
public class ListProductoPageDTO {
    private final LocalDateTime consulta = LocalDateTime.now();
    private final String project = "SpringDam";
    private final String version = "1.0";
    private List<ProductoDTO> data;
    private int currentPage;
    private long totalElements;
    private int totalPages;
}
