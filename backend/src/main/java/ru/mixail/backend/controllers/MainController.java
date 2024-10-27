package ru.mixail.backend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @GetMapping("reports/{id}")
    public ResponseEntity<?> getReportById(@PathVariable Integer id) {
        Report report = reportService.findOne(id);
        if (report != null) {
            return ResponseEntity.ok(report);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Отчет не найден");
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileType = file.getContentType();

        if ("text/csv".equals(fileType)) {
            return reportService.processCsv(file);
        } else if ("application/json".equals(fileType)) {
            return reportService.processJson(file);
        } else {
            return ResponseEntity.badRequest().body("Неподдерживаемый формат файла. Пожалуйста, загрузите CSV или JSON.");
        }
    }

    @PostMapping("/reports")
    public ResponseEntity<?> addReport(@RequestBody Report report) {
        try {
            reportService.save(report);
            return ResponseEntity.status(HttpStatus.CREATED).body("Отчет успешно создан");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка сохранения отчета");
        }
    }

    @PutMapping("/reports/{id}")
    public ResponseEntity<?> updateReport(@RequestBody Report report, @PathVariable Integer id) {
        try {
            report.setId(id); // Установить ID для обновляемого отчета
            reportService.update(report);
            return ResponseEntity.ok("Отчет успешно обновлен");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка изменения отчета");
        }
    }

    @DeleteMapping("/reports/{id}")
    public ResponseEntity<?> deleteReport(@PathVariable Integer id) {
        try {
            reportService.delete(id);
            return ResponseEntity.ok("Отчет успешно удален");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка удаления отчета");
        }
    }

//    @GetMapping("/reports/export/{id}")
//    public ResponseEntity<?> exportReport(@PathVariable Integer id, @RequestParam("format") String format) {
//        try {
//            Report report = reportService.findOne(id);
//            if (report == null) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Отчет не найден");
//            }
//
//            switch (format.toLowerCase()) {
//                case "pptx":
//                    return reportService.exportToPPTX(report);
//                case "pdf":
//                    return reportService.exportToPDF(report);
//                case "docx":
//                    return reportService.exportToDOCX(report);
//                default:
//                    return ResponseEntity.badRequest().body("Неподдерживаемый формат экспорта");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка экспорта отчета");
//        }
//    }
}

