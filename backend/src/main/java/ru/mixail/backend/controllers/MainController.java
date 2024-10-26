package ru.mixail.backend.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mixail.backend.models.Report;
import ru.mixail.backend.services.ReportService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            return processCsv(file);
        } else if (fileType.equals("application/json")) {
            return processJson(file);
        } else {
            return ResponseEntity.badRequest().body("Неподдерживаемый формат файла. Пожалуйста, загрузите CSV или JSON.");
        }
    }

    @PostMapping("/reports")
    public ResponseEntity<?> addReport(@RequestBody Report report) {
        try {
            reportService.save(report);
            return ResponseEntity.status(HttpStatus.CREATED).body().build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body().("Ошибка сохранения отчета");
        }
    }

    @PutMapping("/reports/{id}")
    public ResponseEntity<?> updateReport(@RequestBody Report report, @PathVariable Long id) {
        try{
            reportService.update(report);
            return ResponseEntity.status(HttpStatus.OK).body().build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body().("Ошибка изменения отчета");
        }
    }

    @DeleteMapping("/reports/{id}")
    public ResponseEntity<?> deleteReport(@PathVariable Long id) {
        try{
            reportService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body().build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body().("Ошибка изменения отчета");
        }
    }

    @GetMapping("/reports/exports")
    public ResponseEntity<?> exportReports(@PathVariable Long id, @RequestParam("format") String format) {
        try {
            if (format.equals("pptx")) {
                return exportToPPTX(reportService.findById(id));
            } else if (format.equals("pdf")) {
                return exportToPDF(reportService.findById(id));
            } else if (format.equals("docx")) {
                return exportToDOCX(reportService.findById(id));
            } else {
                return ResponseEntity.badRequest().body("Неподдерживаемый формат экспорта");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка экспорта отчета");
        }
    }

    private ResponseEntity<?> processCsv(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            List<Map<String, String>> records = new ArrayList<>();
            String line;
            String[] headers = reader.readLine().split(","); // Заголовки столбцов

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                Map<String, String> record = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    record.put(headers[i], values[i]);
                }
                records.add(record);
            }

            // Сохранение records в базу или отправка на фронтенд для отображения(пока что не знаю, что с ними делать)
            return ResponseEntity.ok(records); // временно возвращаю данные для проверки
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка обработки CSV файла.");
        }
    }

    private ResponseEntity<?> processJson(MultipartFile file) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> records = mapper.readValue(file.getInputStream(), new TypeReference<List<Map<String, Object>>>() {});

            // Сохранение records в базу или отправка на фронтенд для отображения(пока что не знаю, что с ними делать)
            return ResponseEntity.ok(records); // временно возвращаем данные для проверки
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка обработки JSON файла.");
        }
    }

    private ResponseEntity<?> exportToPPTX(Report report) {

    }

    private ResponseEntity<?> exportToPDF(Report report) {

    }

    private ResponseEntity<?> exportToDOCX(Report report) {

    }

}
