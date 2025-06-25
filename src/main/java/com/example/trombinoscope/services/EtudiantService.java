package com.example.trombinoscope.services;

import com.example.trombinoscope.entities.Etudiant;
import com.example.trombinoscope.repositories.EtudiantRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EtudiantService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    public Etudiant createEtudiant(String matricule, String nom, String prenom, Date dateDeNaissance, String lieuDeNaissance, Etudiant.sexe sexe, Etudiant.classe classe) {
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
        return etudiantRepository.searchEtudiants(matricule, nom);
    }

}
