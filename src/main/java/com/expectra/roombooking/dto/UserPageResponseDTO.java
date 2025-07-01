package com.expectra.roombooking.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class UserPageResponseDTO implements Serializable {
    private List<UserListDTO> content;
    private int totalPages;
    private long totalElements;
    private int size;
    private int number;
    private boolean first;
    private boolean last;
    private int numberOfElements;
    private boolean empty;
    // Puedes agregar más campos si el FE los necesita (ej: sort)
} 