package com.clear_solutions.test_assignment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private long id;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email is not valid",
            regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    @NotBlank(message = "First Name cannot be empty")
    @Pattern(regexp = "^[A-Za-z]*$", message = "Invalid Input")
    @Length(min = 3, max = 20, message = "Invalid name")
    private String firstName;

    @NotBlank(message = "Last Name cannot be empty")
    @Pattern(regexp = "^[A-Za-z]*$", message = "Invalid Input")
    private String lastName;

    @NotNull(message = "Date cannot be null")
    @Past(message = "Invalid birth date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private String address;

    private int phoneNumber;
}
