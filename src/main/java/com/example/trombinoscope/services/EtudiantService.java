package com.example.trombinoscope.services;

import com.example.trombinoscope.entities.Etudiant;
import com.example.trombinoscope.repositories.EtudiantRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EtudiantService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    public Etudiant createEtudiant(String matricule, String nom, String prenom, LocalDate dateDeNaissance, String lieuDeNaissance, Etudiant.sexe sexe, Etudiant.classe classe) {
        if(etudiantRepository.existsById(matricule)){
            throw new RuntimeException("matricule exists");
        }
        Etudiant etudiant = new Etudiant();
        etudiant.setMatricule(matricule);
        etudiant.setNom(nom);
        etudiant.setPrenom(prenom);
        etudiant.setDateDeNaissance(dateDeNaissance);
        etudiant.setLieuDeNaissance(lieuDeNaissance);
        etudiant.setSexe(sexe);
        etudiant.setClasse(classe);

        return etudiantRepository.save(etudiant);
    }

    public List<Etudiant> findAllEtudiants() {
       return etudiantRepository.findAll();
    }

    public Etudiant getEtudiantById(String matricule) {
        return etudiantRepository.findById(matricule).get();
    }

    public Etudiant getEtudiantByNom(String nom) {
        return etudiantRepository.findByNom(nom)
                .orElseThrow(() -> new RuntimeException("Etudiant with " + nom + "not found"));
    }

    public Etudiant updateEtudiant(Etudiant etudiant) {
        return etudiantRepository.save(etudiant);
    }

    @Transactional
    public void deleteEtudiant(Etudiant etudiant) {
        etudiantRepository.delete(etudiant);
    }

    public List<Etudiant> searchEtudiant(String nom, String prenom, String matricule) {
        return etudiantRepository.searchEtudiants(nom, prenom, matricule);
    }

    public List<Etudiant> searchEtudiant2(String params) {
        return etudiantRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCaseOrMatriculeContainingIgnoreCase(params, params, params);
    }

    public List<String> getDistinctClasses() {
        List<Etudiant.classe> classes = etudiantRepository.findDistinctClasses();
        return classes.stream()
                .map(Enum::name)
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Etudiant> getStudentsByClassName(Etudiant.classe classe) {
        try {
            Etudiant.classe classeEnum = Etudiant.classe.valueOf(String.valueOf(classe));
            return etudiantRepository.findByClasse(classeEnum);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid class name: " + classe);
        }
    }
}
