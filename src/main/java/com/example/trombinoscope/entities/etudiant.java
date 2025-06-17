package com.example.trombinoscope.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class etudiant {
    @Id
    private String matricule;

    private String nom;
    private String prenom;
    private Date dateDeNaissance;
    private String lieuDeNaissance;
    private sexe sexe;
    private enum sexe{
        MALE,FEMALE
    }
    private enum classe{
        ING1A, ING1B, ING1EN, ING2EN, ING2FR, ING3ISIEN, ING3ISIFR, ING3SRT, ING4SRT, ING4ISIEN, ING4ISIFR, ING5SRT, ING5IL, ING5MSI
    }
}
