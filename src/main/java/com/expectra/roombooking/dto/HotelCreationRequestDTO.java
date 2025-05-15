package com.expectra.roombooking.dto;
// Ejemplo de DTO para la creación del Hotel con sus detalles
// package com.expectra.roombooking.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelCreationRequestDTO {

    // Campos del Hotel
    private String hotelCode;
    @NotBlank(message = "Hotel name cannot be blank")
    private String hotelName;
    private String hotelStatus; // 'A', 'P', 'I'
    private Long brandId; // ID de la marca seleccionada

    private String localPhone;
    private String celularPhone; // Podrías tener solo uno principal
    private String pmsVendor;    // Nombre del proveedor PMS
    private Long pmsHotelId;
    private String pmsToken;
    private String crsVendor;    // Nombre del proveedor CRS
    private Long crsHotelId;
    private String crsToken;
    private String disclaimer;
    private Integer totalFloors;
    private Integer totalRooms;
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

