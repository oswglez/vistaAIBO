package com.expectra.roombooking.config;

import com.expectra.roombooking.dto.*;
import com.expectra.roombooking.model.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        final ModelMapper mapper = new ModelMapper();
        // Configuración personalizada (si es necesario)
        mapper.createTypeMap(Brand.class, BrandDTO.class);
        mapper.createTypeMap(Hotel.class, HotelOnlyDTO.class);
        mapper.createTypeMap(Room.class, RoomOnlyDTO.class);
        mapper.createTypeMap(Room.class, RoomDTO.class);
        mapper.createTypeMap(Media.class, MediaDTO.class);
        mapper.createTypeMap(Amenity.class, AmenityDTO.class);
        return mapper;
    }

    public ModelMapperConfig() {
    }
}
