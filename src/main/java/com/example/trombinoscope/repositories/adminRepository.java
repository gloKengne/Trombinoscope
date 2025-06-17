package com.example.trombinoscope.repositories;

import com.example.trombinoscope.entities.admin;
import com.example.trombinoscope.entities.etudiant;
import com.example.trombinoscope.entities.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface adminRepository extends JpaRepository<admin,Long> {
    List<user> findByRole(user.role role);
    List<etudiant> findByName(String nom);
    Optional<etudiant> findById(String matricule);
    Optional<etudiant> findByPrenom(String prenom);

}
