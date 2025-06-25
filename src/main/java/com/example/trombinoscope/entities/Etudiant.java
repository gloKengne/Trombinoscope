package com.example.trombinoscope.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Etudiant {
    @Id
    private String matricule;

    private String nom;
    private String prenom;
    private Date dateDeNaissance;
    private String lieuDeNaissance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private sexe sexe;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private classe classe;

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    public void setDateDeNaissance(Date dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }
    public void setLieuDeNaissance(String lieuDeNaissance) {
        this.lieuDeNaissance = lieuDeNaissance;
    }
    public void setSexe(sexe sexe) {
        this.sexe = sexe;
    }
    public void setClasse(classe classe) {
        this.classe = classe;
    }


    public enum sexe{
        Masculin, Feminin
    }

    public enum classe{
        SRT5SSC, ISI5IL, ISI5MSI, SRT4, ISI4En, ISI4Fr, SRT3, ISI3En, ISI3Fr, GC3, En2, Fr2, En1, FrA1, FrB1
    }
}
