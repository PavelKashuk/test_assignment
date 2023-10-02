package com.clear_solutions.test_assignment.service;

import com.clear_solutions.test_assignment.model.User;

import java.time.LocalDate;
import java.util.List;

public interface UserService {

    User saveUser(User user);

    List<User> getAllUsers();

    User getUserById(long id);

    User updateUser(User user, long id);

    void deleteUser(long id);

    boolean isAdultUser(LocalDate userBirthDate);

    List<User> usersByBirthDateRange(LocalDate fromDate, LocalDate toDate);

}
