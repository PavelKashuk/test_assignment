package com.clear_solutions.test_assignment.unit.controller;

import com.clear_solutions.test_assignment.controller.UserController;
import com.clear_solutions.test_assignment.dto.UserDTO;
import com.clear_solutions.test_assignment.exception.ResourceNotFoundException;
import com.clear_solutions.test_assignment.mapper.UserMapper;
import com.clear_solutions.test_assignment.model.User;
import com.clear_solutions.test_assignment.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(value = UserController.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    private String url = "/api/users";

    private static final User TEST_USER;
    private static final UserDTO TEST_USER_DTO;
    private static final User TEST_USER_FOR_CREATE;
    private static final UserDTO TEST_USER_DTO_FOR_CREATE;
    private static final UserDTO WRONG_TEST_USER_DTO_FOR_CREATE;

    private static final Long FAKE_ID = (long) Integer.MAX_VALUE;

    static {

        TEST_USER = User.builder()
                .id(1L)
                .email("test1@gmail.com")
                .firstName("Bob")
                .lastName("Smith")
                .birthDate(LocalDate.of(1980, 11, 20))
                .address("New_York")
                .phoneNumber(12345678)
                .build();

        TEST_USER_DTO = UserDTO.builder()
                .id(1L)
                .email("test1@gmail.com")
                .firstName("Bob")
                .lastName("Smith")
                .birthDate(LocalDate.of(1980, 11, 20))
                .address("New_York")
                .phoneNumber(12345678)
                .build();

        TEST_USER_FOR_CREATE = User.builder()
                .email("test1@gmail.com")
                .firstName("Bob")
                .lastName("Smith")
                .birthDate(LocalDate.of(1980, 11, 20))
                .address("New_York")
                .phoneNumber(12345678)
                .build();

        TEST_USER_DTO_FOR_CREATE = UserDTO.builder()
                .email("test1@gmail.com")
                .firstName("Bob")
                .lastName("Smith")
                .birthDate(LocalDate.of(1980, 11, 20))
                .address("New_York")
                .phoneNumber(12345678)
                .build();

        WRONG_TEST_USER_DTO_FOR_CREATE = UserDTO.builder()
                .email("test1@gmail.com")
                .firstName("Bob")
                .lastName("Smith")
                .birthDate(LocalDate.of(2010, 11, 20))
                .address("New_York")
                .phoneNumber(12345678)
                .build();
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(userService, mapper);
    }

    @SneakyThrows
    @Test
    public void checkAllUsers_successFlow() {
        when(userService.getAllUsers()).thenReturn(List.of(TEST_USER));
        when(mapper.toDtoList(List.of(TEST_USER))).thenReturn(List.of(TEST_USER_DTO));

        mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(TEST_USER.getId()))
                .andExpect(jsonPath("$[0].firstName").value(TEST_USER.getFirstName()));

        verify(userService).getAllUsers();
        verify(mapper).toDtoList(List.of(TEST_USER));
    }

    @SneakyThrows
    @Test
    public void checkUserById_successFlow() {
        when(userService.getUserById(TEST_USER.getId())).thenReturn(TEST_USER);
        when(mapper.toDto(TEST_USER)).thenReturn(TEST_USER_DTO);

        mockMvc.perform(get(url + "/" + TEST_USER.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.id").value(TEST_USER.getId())))
                .andExpect(jsonPath("$.firstName").value(TEST_USER.getFirstName()));

        verify(userService).getUserById(TEST_USER.getId());
        verify(mapper).toDto(TEST_USER);
    }

    @SneakyThrows
    @Test
    public void checkUserById_errorFlow() {
        when(userService.getUserById(FAKE_ID)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get(url + "/" + FAKE_ID).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userService).getUserById(FAKE_ID);
    }

    @SneakyThrows
    @Test
    public void checkCreateUser_successFlow() {
        when(userService.isAdultUser(TEST_USER_FOR_CREATE.getBirthDate())).thenReturn(true);
        when(userService.saveUser(TEST_USER_FOR_CREATE)).thenReturn(TEST_USER_FOR_CREATE);
        when(mapper.toDto(TEST_USER_FOR_CREATE)).thenReturn(TEST_USER_DTO_FOR_CREATE);
        when(mapper.toEntity(TEST_USER_DTO_FOR_CREATE)).thenReturn(TEST_USER_FOR_CREATE);

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(TEST_USER_DTO_FOR_CREATE)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(TEST_USER_FOR_CREATE.getFirstName()));

        verify(userService).isAdultUser(TEST_USER_FOR_CREATE.getBirthDate());
        verify(userService).saveUser(TEST_USER_FOR_CREATE);
        verify(mapper).toDto(TEST_USER_FOR_CREATE);
        verify(mapper).toEntity(TEST_USER_DTO_FOR_CREATE);
    }

    @SneakyThrows
    @Test
    public void checkCreateUser_errorFlow() {
        when(userService.isAdultUser(WRONG_TEST_USER_DTO_FOR_CREATE.getBirthDate())).thenReturn(false);

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(WRONG_TEST_USER_DTO_FOR_CREATE)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid user age"));

        verify(userService).isAdultUser(WRONG_TEST_USER_DTO_FOR_CREATE.getBirthDate());
    }

    @SneakyThrows
    @Test
    public void checkUpdateUser_successFlow() {
        when(userService.updateUser(TEST_USER, TEST_USER.getId())).thenReturn(TEST_USER);
        when(mapper.toDto(TEST_USER)).thenReturn(TEST_USER_DTO);
        when(mapper.toEntity(TEST_USER_DTO)).thenReturn(TEST_USER);

        mockMvc.perform(put(url + "/" + TEST_USER.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(TEST_USER_DTO)))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.id").value(TEST_USER.getId())))
                .andExpect(jsonPath("$.firstName").value(TEST_USER.getFirstName()));

        verify(userService).updateUser(TEST_USER, TEST_USER.getId());
        verify(mapper).toDto(TEST_USER);
        verify(mapper).toEntity(TEST_USER_DTO);
    }

    @SneakyThrows
    @Test
    public void checkUpdateUser_errorFlow() {
        when(mapper.toEntity(TEST_USER_DTO_FOR_CREATE)).thenReturn(TEST_USER_FOR_CREATE);
        when(userService.updateUser(TEST_USER_FOR_CREATE, FAKE_ID)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(put(url + "/" + FAKE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(TEST_USER_FOR_CREATE)))
                .andExpect(status().isNotFound());

        verify(mapper).toEntity(TEST_USER_DTO_FOR_CREATE);
        verify(userService).updateUser(TEST_USER_FOR_CREATE, FAKE_ID);
    }

    @SneakyThrows
    @Test
    public void checkDeleteUser_successFlow() {
        doNothing().when(userService).deleteUser(TEST_USER.getId());

        mockMvc.perform(delete(url + "/" + TEST_USER.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("User has been deleted"));

        verify(userService).deleteUser(TEST_USER.getId());
    }

    @SneakyThrows
    @Test
    public void checkDeleteUser_errorFlow() {
        doThrow(new ResourceNotFoundException("User", "Id", FAKE_ID)).when(userService).deleteUser(FAKE_ID);

        mockMvc.perform(delete(url + "/" + FAKE_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format("User not found with Id : '%s'", FAKE_ID)));

        verify(userService).deleteUser(FAKE_ID);
    }

    private String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        return objectMapper.writeValueAsString(obj);
    }
}
