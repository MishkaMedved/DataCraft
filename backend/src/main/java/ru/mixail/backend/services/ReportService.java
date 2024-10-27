package ru.mixail.backend.services;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.mixail.backend.models.Report;
import ru.mixail.backend.repositories.ReportRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    public List<Report> findAll(){
        return reportRepository.findAll();
    }

    public Report findOne(int id){
        Optional<Report> report = reportRepository.findById(id);
        return report.get();
    }

    @Transactional
    public void save(Report report) {
        reportRepository.save(report);
    }

    @Transactional
    public void update(Report report) {
        Report originalReport = reportRepository.findById(report.getId()).orElseThrow();
        originalReport.setName(report.getName());
        originalReport.setStatus(report.getStatus());
        originalReport.setUpdatedAt(report.getUpdatedAt());
        originalReport.setFavorite(report.isFavorite());

        reportRepository.save(originalReport);
    }

    @Transactional
    public void delete(Long id) {
    }

    public ResponseEntity<?> processCsv(MultipartFile file) {
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

    public ResponseEntity<?> processJson(MultipartFile file) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> records = mapper.readValue(file.getInputStream(), new TypeReference<List<Map<String, Object>>>() {});

            // Сохранение records в базу или отправка на фронтенд для отображения(пока что не знаю, что с ними делать)
            return ResponseEntity.ok(records); // временно возвращаем данные для проверки
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка обработки JSON файла.");
        }
    }

    public ResponseEntity<?> exportToPPTX(Report report) {
        return null;
    }

    public ResponseEntity<?> exportToPDF(Report report) {
        return null;
    }

    public ResponseEntity<?> exportToDOCX(Report report) {
        return null;
    }

}
