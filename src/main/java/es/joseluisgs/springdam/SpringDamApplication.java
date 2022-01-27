package es.joseluisgs.springdam;

import es.joseluisgs.springdam.models.Producto;
import es.joseluisgs.springdam.repositories.ProductosRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@SpringBootApplication
public class SpringDamApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDamApplication.class, args);
    }

        // OJO!!! Tambien podemos iniciar los datos cargándolos desed aqui
        @Bean
        public CommandLineRunner initProductos(ProductosRepository productosRepository){
            return (args) -> {
                productosRepository.save(Producto.builder().nombre("Init").precio(1.5).stock(5).createdAt(LocalDateTime.now()).build());
            };
        }

}
