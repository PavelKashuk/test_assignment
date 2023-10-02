package com.clear_solutions.test_assignment.unit.service;

import com.clear_solutions.test_assignment.exception.RangeDateException;
import com.clear_solutions.test_assignment.exception.ResourceNotFoundException;
import com.clear_solutions.test_assignment.model.User;
import com.clear_solutions.test_assignment.repository.UserRepository;
import com.clear_solutions.test_assignment.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WebMvcTest(value = UserService.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void beforeEach() {
        //Create test data
        user1 = User.builder()
                .id(1L)
                .email("test1@gmail.com")
                .firstName("Bob")
                .lastName("Smith")
                .birthDate(LocalDate.of(1980, 11, 20))
                .address("New_York")
                .phoneNumber(12345678)
                .build();

        user2 = User.builder()
                .id(2L)
                .email("test2@gmail.com")
                .firstName("Den")
                .lastName("Mitchel")
                .birthDate(LocalDate.of(1995, 6, 9))
                .address("Chicago")
                .phoneNumber(9876543)
                .build();

        user3 = User.builder()
                .id(3L)
                .email("test3@gmail.com")
                .firstName("Mike")
                .lastName("Taylor")
                .birthDate(LocalDate.of(2000, 1, 5))
                .address("New_York")
                .phoneNumber(52345671)
                .build();
    }

    @AfterEach
    public void afterEach() {
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void checkAllUsers_successFlow() {
        List<User> expected = Arrays.asList(user1, user2, user3);
        when(userRepository.findAll()).thenReturn(expected);
        List<User> actual = userService.getAllUsers();
        assertEquals(expected, actual);
        verify(userRepository).findAll();
    }

    @Test
    public void checkUserById_successFlow() {
        when(userRepository.findById(user1.getId())).thenReturn(java.util.Optional.of(user1));
        User actual = userService.getUserById(user1.getId());
        assertEquals(user1, actual);
        verify(userRepository).findById(user1.getId());
    }

    @Test
    public void checkSaveUser_successFlow() {
        User expected = user1;
        when(userRepository.save(user1)).thenReturn(expected);
        User actual = userService.saveUser(user1);
        assertEquals(expected, actual);
        verify(userRepository).save(user1);
    }

    @Test
    public void checkUserUpdate_successFlow() {
        User expected = user1;
        when(userRepository.save(user1)).thenReturn(expected);
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        User actual = userService.updateUser(user1, user1.getId());
        assertEquals(expected, actual);
        verify(userRepository).save(user1);
        verify(userRepository).findById(user1.getId());
    }

    @Test
    public void checkUserUpdate_exceptionFlow() {
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateUser(user1, 4L);
        });
        String expectedMessage = "User not found with Id : '4'";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        verify(userRepository).findById(4L);
    }

    @Test
    public void checkDeleteUserById_successFlow() {
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        userService.deleteUser(user1.getId());
        verify(userRepository).findById(user1.getId());
        verify(userRepository).deleteById(user1.getId());
    }

    @Test
    public void checkDeleteUserById_exceptionFlow() {
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteUser(4L);
        });
        String expectedMessage = "User not found with Id : '4'";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        verify(userRepository).findById(4L);
    }

    @Test
    public void checkAdultUser_successFLow() {
        LocalDate localDate = LocalDate.of(2023, 9, 30);
        user1.setBirthDate(localDate.minusYears(20));
        user2.setBirthDate(localDate.minusYears(10));
        assertFalse(userService.isAdultUser(user2.getBirthDate()));
        assertTrue(userService.isAdultUser(user1.getBirthDate()));
    }

    @Test
    public void checkUserByBirthDateRange_successFlow() {
        LocalDate fromDate = LocalDate.of(1993, 9, 30);
        LocalDate toDate = LocalDate.of(2001, 9, 30);
        List<User> allUsers = Arrays.asList(user1, user2, user3);
        when(userRepository.findAll()).thenReturn(allUsers);
        List<User> expected = Arrays.asList(user2, user3);
        List<User> actual = userService.usersByBirthDateRange(fromDate, toDate);
        assertEquals(expected, actual);
        verify(userRepository).findAll();
    }

    @Test
    public void checkUserByBirthDateRange_exceptionFlow() {
        LocalDate fromDate = LocalDate.of(2001, 9, 30);
        LocalDate toDate = LocalDate.of(1995, 9, 30);
        Exception exception = assertThrows(RangeDateException.class, () -> {
            userService.usersByBirthDateRange(fromDate, toDate);
        });
        String expectedMessage = "Argument fromDate should be less then toDate";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
