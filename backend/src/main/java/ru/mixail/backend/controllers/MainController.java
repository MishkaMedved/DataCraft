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

    @PostMapping("/reports")
    public ResponseEntity<?> addReport(@RequestBody Report report) {
        try {
            reportService.save(report);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка сохранения отчета");
        }
    }

    @PutMapping("/reports/{id}")
    public ResponseEntity<?> updateReport(@RequestBody Report report, @PathVariable Integer id) {
        try{
            reportService.update(report);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка изменения отчета");
        }
    }

    @DeleteMapping("/reports/{id}")
    public ResponseEntity<?> deleteReport(@PathVariable int id) {
        try{
            reportService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка изменения отчета");
        }
    }

//    @GetMapping("/reports/exports")
//    public ResponseEntity<?> exportReports(@PathVariable Integer id, @RequestParam("format") String format) {
//        try {
//            if (format.equals("pptx")) {
//                return reportService.exportToPPTX(reportService.findOne(id));
//            } else if (format.equals("pdf")) {
//                return reportService.exportToPDF(reportService.findOne(id));
//            } else if (format.equals("docx")) {
//                return reportService.exportToDOCX(reportService.findOne(id));
//            } else {
//                return ResponseEntity.badRequest().body("Неподдерживаемый формат экспорта");
//            }
//        } catch (Exception e){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка экспорта отчета");
//        }
//    }
}
