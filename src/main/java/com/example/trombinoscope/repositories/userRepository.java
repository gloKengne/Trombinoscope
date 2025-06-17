package com.example.trombinoscope.repositories;

import com.example.trombinoscope.entities.etudiant;
import com.example.trombinoscope.entities.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface userRepository extends JpaRepository<user,Long> {
    List<etudiant> findByName(String nom);
    Optional<etudiant> findByPrenom(String prenom);
    List<etudiant> findById(String matricule);
}
