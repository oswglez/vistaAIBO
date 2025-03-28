package com.expectra.roombooking.config;

import com.expectra.roombooking.dto.AmenityDTO;
import com.expectra.roombooking.dto.MediaDTO;
import com.expectra.roombooking.dto.RoomDTO;
import com.expectra.roombooking.dto.RoomOnlyDTO;
import com.expectra.roombooking.model.Amenity;
import com.expectra.roombooking.model.Media;
import com.expectra.roombooking.model.Room;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        final ModelMapper mapper = new ModelMapper();
        // Configuración personalizada (si es necesario)
        mapper.createTypeMap(Room.class, RoomOnlyDTO.class);
        mapper.createTypeMap(Room.class, RoomDTO.class);
        mapper.createTypeMap(Media.class, MediaDTO.class);
        mapper.createTypeMap(Amenity.class, AmenityDTO.class);
        return mapper;
    }

    public ModelMapperConfig() {
    }
}
