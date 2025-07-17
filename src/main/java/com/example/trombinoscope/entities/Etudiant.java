    package com.example.trombinoscope.entities;

    import com.fasterxml.jackson.annotation.JsonFormat;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    import java.time.LocalDate;
    import java.util.Date;

    @Entity
    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    public class Etudiant {
        @Id
        private String matricule;

        private String nom;
        private String prenom;
        @JsonFormat(pattern = "dd/MM/yyyy")
        private LocalDate dateDeNaissance;
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

        public LocalDate getDateDeNaissance() {
            return dateDeNaissance;
        }

        public void setDateDeNaissance(LocalDate dateDeNaissance) {
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

        public String getMatricule() {
            return matricule;
        }

        public String getNom() {
            return nom;
        }

        public String getPrenom() {
            return prenom;
        }



        public String getLieuDeNaissance() {
            return lieuDeNaissance;
        }

        public sexe getSexe() {
            return sexe;
        }

        public classe getClasse() {
            return classe;
        }

        public enum sexe{
            Masculin, Feminin
        }

        public enum classe{
            SRT5SSC, ISI5IL, ISI5MSI, SRT4, ISI4En, ISI4Fr, SRT3, ISI3En, ISI3Fr, GC3, En2, Fr2, En1, FrA1, FrB1;
        }

        @Column(name = "photo")
        private String photo;

    }
