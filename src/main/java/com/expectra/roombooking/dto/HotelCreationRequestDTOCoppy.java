package com.expectra.roombooking.dto;
// Ejemplo de DTO para la creación del Hotel con sus detalles
// package com.expectra.roombooking.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelCreationRequestDTOCoppy {

    // Campos del Hotel
    private String hotelCode;
    @NotBlank(message = "Hotel name cannot be blank")
    private String hotelName;
    private String hotelStatus; // 'A', 'P', 'I'
    private Long brandId; // ID de la marca seleccionada

    private String localPhone;
    private String disclaimer;
    private String hotelWebsiteUrl;

    // Información del Contacto Principal (anidada)
    @Valid // Para que se validen las anotaciones dentro de ContactInfoDTO
    @NotNull(message = "Main contact information is required")
    private ContactInfoDTO mainContact;

    // Información de la Dirección Principal (anidada)
    @Valid
    @NotNull(message = "Main address information is required")
    private AddressInfoDTO mainAddress;


}

