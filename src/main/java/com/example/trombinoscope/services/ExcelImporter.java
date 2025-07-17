package com.example.trombinoscope.services;

import com.example.trombinoscope.entities.Etudiant;
import com.example.trombinoscope.repositories.EtudiantRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.*;
import org.springframework.stereotype.Service;
import java.text.Normalizer;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter
@Getter
@Service
public class ExcelImporter {

    @Autowired
    private EtudiantRepository etudiantRepository;

    public ImportResult importFromExcel(String filePath) {
        ImportResult result = new ImportResult();
        Workbook workbook = null;

        System.out.println("Importing Excel file... 1");
        try (FileInputStream fis = new FileInputStream(filePath)) {
            // Detect file extension and initialize correct Workbook type
            if (filePath.toLowerCase().endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(fis);
            } else if (filePath.toLowerCase().endsWith(".xls")) {
                workbook = new HSSFWorkbook(fis);
            } else {
                throw new IllegalArgumentException("Unsupported file type: must be .xls or .xlsx");
            }


            int sheetCount = workbook.getNumberOfSheets();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            for (int i = 0; i < sheetCount; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName();

                System.out.println("Processing sheet: " + sheetName);

                // Try to match sheet name to classe enum, or use default processing
                Etudiant.classe classeEnum = null;
                try {
                    classeEnum = Etudiant.classe.valueOf(sheetName);
                } catch (IllegalArgumentException e) {
                    System.out.println("Sheet name '" + sheetName + "' doesn't match any classe enum. Using first available classe or skipping...");
                    // You might want to set a default classe or skip this sheet
                    continue;
                }


                // Process rows (assuming row 0 has headers, data starts from row 1)
                for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    if (row == null) continue;

                    try {
                        // Extract data from cells
                        String matricule = getCellValue(row.getCell(0));
                        String nom = getCellValue(row.getCell(1));
                        String prenom = getCellValue(row.getCell(2));
                        String dateStr = getCellValue(row.getCell(3));
                        String classeStr = getCellValue(row.getCell(4));
                        String lieuDeNaissance = getCellValue(row.getCell(5));
                        String sexeStr = getCellValue(row.getCell(6));

                        // Validate required fields
                        if (matricule.isEmpty() || nom.isEmpty() || prenom.isEmpty()) {
                            result.addError("Row " + (rowIndex + 1) + " in sheet '" + sheetName + "': Missing required fields (matricule, nom, or prenom)");
                            continue;
                        }

                        // Check if student already exists
                        if (etudiantRepository.existsByMatricule(matricule)) {
                            result.addWarning("Row " + (rowIndex + 1) + " in sheet '" + sheetName + "': Student with matricule '" + matricule + "' already exists. Skipping.");
                            continue;
                        }

                        // Parse date
                        LocalDate dateNaissance = null;

                        if (!dateStr.isEmpty()) {
                            try {

                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                dateNaissance = LocalDate.parse(dateStr, formatter);
                            } catch (Exception e) {


                                result.addError("Row " + (rowIndex + 1) + " in sheet '" + sheetName + "': Invalid date format '" + dateStr + "'. Expected dd/MM/yyyy");
                                continue;
                            }
                        }



                        // Parse sexe enum
                        Etudiant.sexe sexeEnum = null;
                        if (!sexeStr.isEmpty()) {
                            try {
                                // Normalisation de la chaîne
                                String normalizedSexe = normalizeSexe(sexeStr);
                                sexeEnum = Etudiant.sexe.valueOf(normalizedSexe);
                            } catch (IllegalArgumentException e) {
                                result.addError("Row " + (rowIndex + 1) + " in sheet '" + sheetName + "': Invalid sexe value '" + sexeStr + "'. Expected: Masculin, Feminin, Féminin");
                                continue;
                            }
                        }
                        System.out.println("sexe: " + sexeEnum);

                        // Create new student
                        Etudiant newEtudiant = new Etudiant();
                        newEtudiant.setMatricule(matricule);
                        newEtudiant.setNom(nom);
                        newEtudiant.setPrenom(prenom);
                        newEtudiant.setDateDeNaissance(dateNaissance);
                        newEtudiant.setLieuDeNaissance(lieuDeNaissance);
                        newEtudiant.setSexe(sexeEnum);
                        newEtudiant.setClasse(classeEnum);
                        etudiantRepository.save(newEtudiant);
                        result.incrementSuccessCount();

                    } catch (Exception e) {
                        String errorMsg = "Error at row " + (rowIndex + 1) + " in sheet '" + sheetName + "': " + e.getMessage();
                        result.addError(errorMsg);
                        System.out.println(errorMsg);
                    }
                }
            }

        } catch (Exception e) {
            result.addError("Failed to process Excel file: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    System.out.println("Error closing workbook: " + e.getMessage());
                }
            }
        }

        return result;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return new SimpleDateFormat("dd/MM/yyyy").format(cell.getDateCellValue());
                } else {
                    // Return as integer if it's a whole number, otherwise as decimal
                    double numValue = cell.getNumericCellValue();
                    if (numValue == Math.floor(numValue)) {
                        return String.valueOf((long) numValue);
                    } else {
                        return String.valueOf(numValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                // Evaluate formula and get the result
                FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
                CellValue cellValue = evaluator.evaluate(cell);
                switch (cellValue.getCellType()) {
                    case STRING:
                        return cellValue.getStringValue().trim();
                    case NUMERIC:
                        return String.valueOf((long) cellValue.getNumberValue());
                    case BOOLEAN:
                        return String.valueOf(cellValue.getBooleanValue());
                    default:
                        return "";
                }
            default:
                return "";
        }
    }

    // Inner class to track import results
    @Data
    public static class ImportResult {
        private int successCount = 0;
        private java.util.List<String> errors = new java.util.ArrayList<>();
        private java.util.List<String> warnings = new java.util.ArrayList<>();

        public void incrementSuccessCount() {
            this.successCount++;
        }

        public void addError(String error) {
            this.errors.add(error);
        }

        public void addWarning(String warning) {
            this.warnings.add(warning);
        }

        public boolean hasErrors() {
            return !errors.isEmpty();
        }

        public boolean hasWarnings() {
            return !warnings.isEmpty();
        }
    }

    //fonction prive
    private String normalizeSexe(String sexeStr) {
        if (sexeStr == null || sexeStr.trim().isEmpty()) {
            return "";
        }

        // Suppression des accents et conversion en minuscules
        String normalized = removeAccents(sexeStr.trim().toLowerCase());

        switch (normalized) {
            case "masculin":
            case "m":
            case "male":
            case "homme":
                return "Masculin";

            case "feminin": // "féminin" devient "feminin" après suppression de l'accent
            case "f":
            case "female":
            case "femme":
                return "Feminin";

            default:
                throw new IllegalArgumentException("Valeur de sexe non reconnue: " + sexeStr);
        }
    }

    private String removeAccents(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}