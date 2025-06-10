package com.expectra.roombooking.dto;
// Ejemplo de DTO para la creación del Hotel con sus detalles
// package com.expectra.roombooking.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelCreationRequestDTO {
    // Campos del Hotel
    private Long hotelId;
    private String hotelCode;
    private String hotelName;
    private String hotelStatus; // 'A', 'P', 'I'
    private Long brandId;
    private String brandName;
    private String localPhone;
    private String disclaimer;
    private String hotelWebsiteUrl;
    private Long chainId;
    private String chainName;

    // Información del Contacto Principal (anidada)
    @Valid // Para que se validen las anotaciones dentro de ContactInfoDTO
    @NotNull(message = "Main contact information is required")
    private ContactInfoDTO mainContact;

    // Información de la Dirección Principal (anidada)
    @Valid
    @NotNull(message = "Main address information is required")
    private AddressInfoDTO mainAddress;


}

