package com.example.trombinoscope.controller;

import com.example.trombinoscope.entities.Etudiant;
import com.example.trombinoscope.repositories.EtudiantRepository;
import com.example.trombinoscope.services.EtudiantService;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/etudiant")
public class EtudiantController {

    @Autowired
    private EtudiantService etudiantService;
    @Autowired
    private EtudiantRepository etudiantRepository;
    @Autowired
    private TemplateEngine templateEngine;

    @GetMapping("/{matricule}")
    @ResponseBody
    public Map<String, Object> getEtudiantByMatricule(@PathVariable String matricule) {
        Optional<Etudiant> etudiant = etudiantRepository.findById(matricule);
        if (etudiant.isEmpty()) return Collections.emptyMap();

        System.out.println("specialiteeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee" + etudiant.get().getSpecialiteLabel());

        Map<String, Object> data = new HashMap<>();
        data.put("nom", etudiant.get().getNom());
        data.put("prenom", etudiant.get().getPrenom());
        data.put("matricule", etudiant.get().getMatricule());
        data.put("classe", etudiant.get().getClasse());
        data.put("photo", etudiant.get().getPhoto());
        data.put("dateDeNaissance", etudiant.get().getDateDeNaissance());
        data.put("lieuDeNaissance", etudiant.get().getLieuDeNaissance());
        data.put("sexe", etudiant.get().getSexe());

        data.put("specialiteLabel", etudiant.get().getSpecialiteLabel());

        return data;
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

    @GetMapping("/student/class/{classe}/pdf")
    public ResponseEntity<byte[]> generatePdfForClass(@PathVariable Etudiant.classe classe) throws Exception {
        try {
            classe = classe.valueOf(classe.name());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid class name");
        }

        List<Etudiant> students = etudiantRepository.findByClasse(classe);

        if (students.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No students found");
        }

        Context context = new Context();
        context.setVariable("students", students);

        String html = templateEngine.process("carte", context);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useFastMode();
        builder.withHtmlContent(html, null);
        builder.toStream(outputStream);
        builder.run();

        byte[] pdfBytes = outputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename("class_" + classe + "_cards.pdf").build());

        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }

    @DeleteMapping("/student/{matricule}")
    public ResponseEntity<Void> deleteEtudiantByMatricule(@PathVariable String matricule) {
        Optional<Etudiant> etudiant = etudiantRepository.findById(matricule);
        if (etudiant.isPresent()) {
            etudiantRepository.delete(etudiant.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
