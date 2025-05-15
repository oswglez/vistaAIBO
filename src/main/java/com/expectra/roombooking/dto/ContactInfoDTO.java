package com.expectra.roombooking.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactInfoDTO {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String contactTitle;
    @NotBlank
    private String contactEmail;
    private String contactLocalNumber;
    private String contactMobileNumber;
    private String contactFaxNumber;
    @NotBlank
    private String contactType; // e.g., "MAIN", "BILLING"
}
