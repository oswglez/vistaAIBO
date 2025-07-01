package com.expectra.roombooking.service;

import com.expectra.roombooking.dto.UserRequestDTO;
import com.expectra.roombooking.dto.UserResponseDTO;
import com.expectra.roombooking.dto.UserPageResponseDTO;

public interface UserService {
    UserPageResponseDTO getUsers(int page, int size, String sortBy, String direction);
    UserResponseDTO getUserById(Long id);
    UserResponseDTO createUser(UserRequestDTO userRequestDTO);
    UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO);
    void deleteUser(Long id);
} 