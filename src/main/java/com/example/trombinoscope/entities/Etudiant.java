    package com.example.trombinoscope.entities;

    import com.fasterxml.jackson.annotation.JsonFormat;
    import com.fasterxml.jackson.annotation.JsonProperty;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    import java.time.LocalDate;

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

        @Column(name = "photo")
        private String photo;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        public Specialitespe specialitespe;

        public enum Specialitespe {
            ISI("Informatique et Systèmes d'Information"),
            SRT("Systèmes Réseaux et Télécommunications"),
            GENIE_CIVIL("Génie Civil"),
            GENIE_ELECTRIQUE("Génie Électrique"),
            TRONC_COMMUN("Tronc Commun");

            private final String label;

            Specialitespe(String label) {
                this.label = label;
            }

            public String getLabel() {
                return label;
            }
        }

        public void setClasse(classe classe) {
            System.out.println("DEBUG: Setting classe to: " + classe);
            this.classe = classe;

            if (classe != null) {
                switch (classe) {
                    case Ingenieur5IL:
                    case Ingenieur5MSI:
                    case Ingenieur4ISIEn:
                    case Ingenieur4ISIFr:
                    case Ingenieur3ISIEn:
                    case Ingenieur3ISIFr:
                        this.specialitespe = Specialitespe.ISI;
                        break;

                    case Ingenieur5SSC:
                    case Ingenieur4SRT:
                    case Ingenieur3SRT:
                        this.specialitespe = Specialitespe.SRT;
                        break;

                    case Ingenieur3GC:
                    case Ingenieur4GC:
                        this.specialitespe = Specialitespe.GENIE_CIVIL;
                        break;
                    case Ingenieur3GE:
                        this.specialitespe = Specialitespe.GENIE_ELECTRIQUE;
                        break;

                    case Ingenieur1AFr:
                    case Ingenieur1BFr:
                    case Ingenieur2Fr:
                    case Ingenieur1En:
                    case Ingenieur2En:
                        this.specialitespe = Specialitespe.TRONC_COMMUN;
                        break;

                    default:
                        this.specialitespe = null;
                        System.out.println("DEBUG: Unknown classe, setting specialty to null");
                        break;
                }
                System.out.println("DEBUG: Specialty set to: " + this.specialitespe);
            } else {
                this.specialitespe = null;
                System.out.println("DEBUG: Classe is null, setting specialty to null");
            }

        }

        @PostLoad
        @PostPersist
        @PostUpdate
        public void computeSpecialiteAfterLifecycle() {
            if (this.classe != null) {
                // Re-use your existing method to set specialty based on class
                this.setClasse(this.classe);
            }
        }


        public void setSpecialite(classe classe) {
            System.out.println("setSpecialite called - delegating to setClasse");
            setClasse(classe);
        }

        @JsonProperty("specialiteLabel")
        public String getSpecialiteLabel() {
            return this.specialitespe != null ? this.specialitespe.getLabel() : "";
        }

        @PrePersist
        public void beforeSave() {
            System.out.println("BEFORE SAVE - Classe: " + this.classe + ", Specialty: " + this.specialitespe);
        }

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
        public classe getClasse() {
            return this.classe;
        }
        public Specialitespe getSpecialitespe() {
            return this.specialitespe;
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

        public enum sexe{
            Masculin, Feminin
        }

        public enum classe{
            Ingenieur5SSC("Ingénieur 5"), Ingenieur5IL("Ingénieur 5"), Ingenieur5MSI("Ingénieur 5"), Ingenieur4SRT("Ingénieur 4"), Ingenieur4ISIEn("Ingénieur 4"), Ingenieur4ISIFr("Ingénieur 4"), Ingenieur3SRT("Ingénieur 3"), Ingenieur3ISIEn("Ingénieur 3"), Ingenieur3ISIFr("Ingénieur 3"), Ingenieur3GC("Ingénieur 3"), Ingenieur2En("Ingénieur 2"), Ingenieur2Fr("Ingénieur 2"), Ingenieur1En("Ingénieur 1"), Ingenieur1AFr("Ingénieur 1"), Ingenieur1BFr("Ingénieur 1"), Ingenieur3GE("Ingénieur 3"), Ingenieur4GC("Ingénieur 4");

            private final String label;
            classe(String label) {
                this.label = label;
            }

            public String getLabel() {
                return label;
            }

        }
        @JsonProperty("classeLabel")
        public String getClasseLabel() {
            return this.classe != null ? this.classe.getLabel() : "";
        }





    }
