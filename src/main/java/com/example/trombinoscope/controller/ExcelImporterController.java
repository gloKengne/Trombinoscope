package com.example.trombinoscope.controller;

import com.example.trombinoscope.services.ExcelImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ExcelImporterController {

    @Autowired
    private ExcelImporter excelImporter;

    @PostMapping("/import-excel")
    public ResponseEntity<Map<String, Object>> importExcel(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();

        try{
            if (file.isEmpty()) {
                response.put("sucess", false);
                response.put("message", "Please select a file to upload");
                return ResponseEntity.badRequest().body(response);
            }

            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.toLowerCase().endsWith(".xlsx") && !fileName.toLowerCase().endsWith(".xls"))) {
                response.put("success", false); // Correction : "success" au lieu de "sucess"
                response.put("message", "Only excel files are supported");
                return ResponseEntity.badRequest().body(response);
            }

            File tempFile = File.createTempFile("Student_excel", fileName.substring(fileName.lastIndexOf(".")));
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(file.getBytes());
            }

            excelImporter.importFromExcel(tempFile.getAbsolutePath());

            tempFile.delete();

            response.put("sucess", true);
            response.put("message", "Excel file imported succssfully!");
            response.put("fileName", fileName);
            return ResponseEntity.ok(response);
        }
        catch (IOException e){
            response.put("sucess", false);
            response.put("message", "Error processing file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        catch (Exception e){
            response.put("sucess", false);
            response.put("message", "Import failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/import-status")
    public ResponseEntity<Map<String, Object>> getImportStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("ready", true);
        status.put("supportedFormats", new String[]{".xlsx", ".xls"});
        return ResponseEntity.ok(status);
    }

}
