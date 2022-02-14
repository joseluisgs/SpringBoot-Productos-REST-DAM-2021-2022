package es.joseluisgs.springdam.dto.productos;

import es.joseluisgs.springdam.config.APIConfig;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
// Con el Builder me ahorro el mappers...
public class ListProductoPageDTO {
    private final String consulta = LocalDateTime.now().toString();
    private final String project = "SpringDam";
    private final String version = APIConfig.API_VERSION;
    private List<ProductoDTO> data;
    private int currentPage;
    private long totalElements;
    private int totalPages;
    private String sort;
}
