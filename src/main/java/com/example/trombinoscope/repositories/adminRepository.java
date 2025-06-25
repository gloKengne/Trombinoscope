package com.example.trombinoscope.repositories;

import com.example.trombinoscope.entities.admin;
import com.example.trombinoscope.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface adminRepository extends JpaRepository<admin,Long> {
    List<User> findByRole(User.Role role);
    List<admin> findByUsername(String username);
    Optional<admin> findById(Long id);

}
