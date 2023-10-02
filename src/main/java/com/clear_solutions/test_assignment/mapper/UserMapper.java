package com.clear_solutions.test_assignment.mapper;

import com.clear_solutions.test_assignment.dto.UserDTO;
import com.clear_solutions.test_assignment.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements EntityDtoMapper<User, UserDTO> {

    public UserDTO toDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    public User toEntity(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .email(userDTO.getEmail())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .birthDate(userDTO.getBirthDate())
                .address(userDTO.getAddress())
                .phoneNumber(userDTO.getPhoneNumber())
                .build();
    }
}
