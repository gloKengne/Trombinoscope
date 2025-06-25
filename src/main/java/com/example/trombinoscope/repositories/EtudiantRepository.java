package com.example.trombinoscope.repositories;

import com.example.trombinoscope.entities.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant,String> {
    Optional<Etudiant> findById(String matricule);
    Optional<Etudiant> findByNom(String nom);
    Optional<Etudiant> findByPrenom(String prenom);


    List<Etudiant> nom(String nom);

    @Query("SELECT e FROM Etudiant e WHERE " +
            "(:name IS NULL OR LOWER(e.nom) LIKE LOWER(CONCAT('%', :nom, '%'))) AND " +
            "(:matricule IS NULL OR e.matricule = :matricule)")
    List<Etudiant> searchEtudiants(@Param("nom") String nom,
                                 @Param("matricule") String matricule);

    boolean existsByMatricule(String matricule);
}
