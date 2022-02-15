package es.joseluisgs.springdam.config.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    
    // Creamos el bean para el Mapper para usarlo en todo el proyecto (es global)
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}