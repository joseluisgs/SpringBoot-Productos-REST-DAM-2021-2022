package es.joseluisgs.springdam;

import es.joseluisgs.springdam.models.Producto;
import es.joseluisgs.springdam.repositories.ProductosRepository;
import es.joseluisgs.springdam.services.uploads.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class SpringDamApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDamApplication.class, args);
    }

    // Inicializamos el sistema de ficheros
    @Bean
    public CommandLineRunner init(StorageService storageService) {
        return args -> {
            // Inicializamos el servicio de ficheros
            storageService.deleteAll(); // Borramos todos (esto deberíamos quitarlo)
            storageService.init(); // inicializamos

        };

    }

    // Iniciamos la base de datos
    // OJO!!! Tambien podemos iniciar los datos cargándolos desed aqui
    @Bean
    public CommandLineRunner initProductos(ProductosRepository productosRepository) {
        return (args) -> {
            productosRepository.save(
                    Producto.builder().nombre("Init").precio(1.5).stock(5).createdAt(LocalDateTime.now()).imagen("https://api.lorem.space/image?w=150&h=180")
                            .build()
            );
            productosRepository.save(
                    Producto.builder().nombre("Init2").precio(5.55).stock(25).createdAt(LocalDateTime.now()).imagen("https://api.lorem.space/image?w=150&h=180")
                            .build()
            );
        };
    }

}
