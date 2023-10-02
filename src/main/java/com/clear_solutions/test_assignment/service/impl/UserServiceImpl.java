package com.clear_solutions.test_assignment.service.impl;

import com.clear_solutions.test_assignment.exception.RangeDateException;
import com.clear_solutions.test_assignment.exception.ResourceNotFoundException;
import com.clear_solutions.test_assignment.model.User;
import com.clear_solutions.test_assignment.repository.UserRepository;
import com.clear_solutions.test_assignment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Value("${user.age}")
    private String adult_user_age;

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User", "Id", id));
    }

    @Override
    public User updateUser(User user, long id) {
        User existingUser = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User", "Id", id));
        existingUser.setEmail(user.getEmail());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setBirthDate(user.getBirthDate());
        existingUser.setAddress(user.getAddress());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        userRepository.save(existingUser);
        return existingUser;
    }

    @Override
    public void deleteUser(long id) {
        userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User", "Id", id));
        userRepository.deleteById(id);
    }

    @Override
    public boolean isAdultUser(LocalDate userBirthDate) {
        int validUserAge = Integer.parseInt(adult_user_age);
        LocalDate currentDate = LocalDate.now();
        return (Period.between(userBirthDate, currentDate).getYears()) >= validUserAge;
    }

    @Override
    public List<User> usersByBirthDateRange(LocalDate fromDate, LocalDate toDate) {
        if (fromDate.isAfter(toDate)) {
            throw new RangeDateException("Argument fromDate should be less then toDate");
        }
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream()
                .filter(user -> user.getBirthDate().isAfter(fromDate)
                        && user.getBirthDate().isBefore(toDate))
                .collect(Collectors.toList());
    }
}
