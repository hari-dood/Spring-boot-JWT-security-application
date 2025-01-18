package com.hariSolution.Repository;

import com.hariSolution.entitiy.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface userRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);
}
