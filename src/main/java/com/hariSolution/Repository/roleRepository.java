package com.hariSolution.Repository;

import com.hariSolution.entitiy.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface roleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByName(String name);
}
