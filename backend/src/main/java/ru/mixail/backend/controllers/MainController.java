package ru.mixail.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mixail.backend.models.Report;
import ru.mixail.backend.services.ReportService;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class MainController {

    private final ReportService reportService;

    @GetMapping("reports")
    public List<Report> getReports() {
        return reportService.findAll();
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileType = file.getContentType();

        if (fileType.equals("text/csv")) {
            return reportService.processCsv(file);
        } else if (fileType.equals("application/json")) {
            return reportService.processJson(file);
        } else {
            return ResponseEntity.badRequest().body("Неподдерживаемый формат файла. Пожалуйста, загрузите CSV или JSON.");
        }
    }

}
