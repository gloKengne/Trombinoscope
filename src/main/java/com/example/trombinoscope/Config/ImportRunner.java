package com.example.trombinoscope.Config;
import com.example.trombinoscope.services.ExcelImporter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImportRunner {

    //@Bean
    public CommandLineRunner runImport(ExcelImporter importer) {
        return args -> {
            String filePath = "D:\\cle usb/Trombinoscope Etudiants_24-25.xlsx"; // ✅ Adjust path
            importer.importFromExcel(filePath);
            System.out.println("✅ Import terminé !");
        };
    }
}
