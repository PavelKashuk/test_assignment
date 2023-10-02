package com.clear_solutions.test_assignment.controller;

import com.clear_solutions.test_assignment.dto.UserDTO;
import com.clear_solutions.test_assignment.exception.UserInvalidAgeException;
import com.clear_solutions.test_assignment.mapper.UserMapper;
import com.clear_solutions.test_assignment.model.User;
import com.clear_solutions.test_assignment.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public ResponseEntity<UserDTO> saveUser(@RequestBody @Valid UserDTO userDTO) {
        if (userService.isAdultUser(userDTO.getBirthDate())) {
            User user = userService.saveUser(userMapper.toEntity(userDTO));
            return new ResponseEntity<>(userMapper.toDto(user), HttpStatus.CREATED);
        }
        throw new UserInvalidAgeException("Invalid user age");
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userMapper.toDtoList(userService.getAllUsers());
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") long userId) {
        User user = userService.getUserById(userId);
        return new ResponseEntity<>(userMapper.toDto(user), HttpStatus.OK);
    }

    @GetMapping("{fromDate}/{toDate}")
    public List<UserDTO> getAllUsersByDateRange(@PathVariable("fromDate") LocalDate from,
                                                @PathVariable("toDate") LocalDate to) {
        return userMapper.toDtoList(userService.usersByBirthDateRange(from, to));
    }

    @PutMapping("{id}")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO,
                                              @PathVariable("id") long userId) {
        User user = userService.updateUser(userMapper.toEntity(userDTO), userId);
        return new ResponseEntity<>(userMapper.toDto(user), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>("User has been deleted", HttpStatus.OK);
    }
}
