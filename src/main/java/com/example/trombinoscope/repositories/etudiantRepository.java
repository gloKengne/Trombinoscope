package com.example.trombinoscope.repositories;

import com.example.trombinoscope.entities.etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface etudiantRepository extends JpaRepository<etudiant,String> {
    Optional<etudiant> findById(String matricule);
    List<etudiant> findByName(String nom);
    Optional<etudiant> findByPrenom(String prenom);


}
