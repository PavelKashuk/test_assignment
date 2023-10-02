package com.clear_solutions.test_assignment.repository;

import com.clear_solutions.test_assignment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
