package com.expectra.roombooking.config;

import com.expectra.roombooking.dto.RoomDTO;
import com.expectra.roombooking.model.Room;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        // Configuración personalizada (si es necesario)
        mapper.createTypeMap(Room.class, RoomDTO.class); // Mapeo entre Room y RoomDTO
        return mapper;
    }
}
