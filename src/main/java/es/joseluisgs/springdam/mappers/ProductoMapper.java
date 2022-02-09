package es.joseluisgs.springdam.mappers;

import es.joseluisgs.springdam.dto.productos.CreateProductoDTO;
import es.joseluisgs.springdam.dto.productos.ListProductosDTO;
import es.joseluisgs.springdam.dto.productos.ProductoDTO;
import es.joseluisgs.springdam.models.Producto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

// Otra forma de hacer la inyección de dependencias con Lombok
// Para ello usamos la anotacion @RequiredArgsConstructor y
// private final con lo que queramos inyectar
@Component
@RequiredArgsConstructor // Nos ahorramos el autowire
public class ProductoMapper {
    private final ModelMapper modelMapper;


    // Recibe un producto y lo trasforma en productoDTO
    public ProductoDTO toDTO(Producto producto) {
        return modelMapper.map(producto, ProductoDTO.class);

    }

    // Para convertir un prodtctoDTO en producto
    public Producto fromDTO(ProductoDTO productoDTO) {
        return modelMapper.map(productoDTO, Producto.class);
    }

    // Una lista de productos a productosDTO
    public List<ProductoDTO> toDTO(List<Producto> productos) {
        return productos.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Producto fromDTO(CreateProductoDTO productoDTO) {
        return modelMapper.map(productoDTO, Producto.class);
    }

    public ListProductosDTO toListDTO(List<Producto> productos) {
        ListProductosDTO listProductosDTO = new ListProductosDTO();
        listProductosDTO.setData(productos.stream().map(this::toDTO).collect(Collectors.toList()));
        return listProductosDTO;
    }

}
