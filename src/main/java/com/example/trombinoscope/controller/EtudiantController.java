package com.example.trombinoscope.controller;

import com.example.trombinoscope.entities.Etudiant;
import com.example.trombinoscope.repositories.EtudiantRepository;
import com.example.trombinoscope.services.EtudiantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/etudiant")
public class EtudiantController {

    @Autowired
    private EtudiantService etudiantService;
    @Autowired
    private EtudiantRepository etudiantRepository;

    @GetMapping("/{matricule}")
    public ResponseEntity<String> getEtudiant(@PathVariable String matricule) {
        System.out.println("Matricule called with matricule: " + matricule);
        Optional<Etudiant> etudiant = etudiantRepository.findById(matricule);
        if (etudiant.isPresent()) {
            return ResponseEntity.ok(etudiant.get().toString());
        }
            return ResponseEntity.notFound().build();
    }

    @GetMapping("/search2")
    public ResponseEntity<List<Etudiant>> getEtudiant(@RequestParam(required = false) String nom,
                                              @RequestParam(required = false) String prenom,
                                              @RequestParam(required = false) String matricule) {

        System.out.println("search called with nom: " + nom + " prenom: " + prenom);
        List<Etudiant> etudiants = etudiantService.searchEtudiant(nom, prenom, matricule);

        for (Etudiant etudiant : etudiants) {
            System.out.println("liste des etudaints" + etudiant.getMatricule());
        }
        return ResponseEntity.ok(etudiants);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Etudiant>> getEtudiants(@RequestParam String params) {

        List<Etudiant> etudiants = etudiantService.searchEtudiant2(params);

        for (Etudiant etudiant : etudiants) {
            System.out.println("liste des etudiants" + etudiant.getMatricule());
        }
        return ResponseEntity.ok(etudiants);
    }

    @GetMapping("/students")
    public List<Etudiant> getStudentsByClasse(@RequestParam Etudiant.classe classe) {
        return etudiantService.getStudentsByClassName(classe);
    }

    @GetMapping("/classes/all")
    public Etudiant.classe[] getAllClassesEnum() {
        return Etudiant.classe.values();
    }



    @GetMapping("/classes")
    public ResponseEntity<List<String>> getAllDistinctClasses() {
        try {
            List<Etudiant.classe> classes = etudiantRepository.findDistinctClasses();
            List<String> classNames = classes.stream()
                    .map(Enum::name)  // or .map(Object::toString) depending on your preference
                    .collect(Collectors.toList());

            return ResponseEntity.ok(classNames);

        } catch (Exception e) {
            System.err.println("Error fetching distinct classes: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/students/by-class")
    public ResponseEntity<List<Etudiant>> getStudentsByClassName(@RequestParam String className) {
        try {
            // Convert string to enum
            Etudiant.classe classeEnum = Etudiant.classe.valueOf(className.toUpperCase());
            List<Etudiant> students = etudiantRepository.findByClasse(classeEnum);
            return ResponseEntity.ok(students);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
