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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    public void delete(int id) {
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

    public List<Report> filterReports(String name, String createdAfter, String updatedAfter) {
        // Пример фильтрации, можно добавить дополнительные проверки
        return reportRepository.findAll().stream()
                .filter(report -> (name == null || report.getName().contains(name)))
                .filter(report -> (createdAfter == null || report.getCreatedAt().isAfter(LocalDateTime.parse(createdAfter))))
                .filter(report -> (updatedAfter == null || report.getUpdatedAt().isAfter(LocalDateTime.parse(updatedAfter))))
                .collect(Collectors.toList());
    }
//
//    public ResponseEntity<?> exportToPPTX(Report report) {
//        try {
//            XMLSlideShow ppt = new XMLSlideShow();
//            ppt.createSlide();
//            XSLFSlideMaster defaultMaster = ppt.getSlideMasters().get(0);
//            XSLFSlideLayout layout = defaultMaster.getLayout(SlideLayout.TITLE_AND_CONTENT);
//            XSLFSlide slide = ppt.createSlide(layout);
//            XSLFTextShape titleShape = slide.getPlaceholder(0);
//            XSLFTextShape contentShape = slide.getPlaceholder(1);
//
//            XSLFTextBox shape = slide.createTextBox();
//            XSLFTextParagraph p = shape.addNewTextParagraph();
//            XSLFTextRun r = p.addNewTextRun();
//            r.setText(report.getName());
//            r.setFontColor(Color.blue);
//            r.setFontSize(24.);
//
//            FileOutputStream out = new FileOutputStream(report.getName()+".pptx");
//            ppt.write(out);
//            out.close();
//            return new ResponseEntity<>(HttpStatus.OK);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);        }
//    }

//    public ResponseEntity<?> exportToPDF(Report report) {
//        try {
//            PDDocument document = new PDDocument();
//            PDPage page = new PDPage();
//            document.addPage(page);
//
//            PDPageContentStream contentStream = new PDPageContentStream(document, page);
//
//            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER), 12);
//            contentStream.beginText();
//            contentStream.showText(report.getName());
//            contentStream.showText(String.valueOf(report.getStatus()));
//            contentStream.endText();
//            contentStream.close();
//
//            document.save(report.getName()+".pdf");
//            document.close();
//            return new ResponseEntity<>(HttpStatus.OK);
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

//    public ResponseEntity<?> exportToDOCX(Report report) {
//        try {
//            WordprocessingMLPackage wordPackage = WordprocessingMLPackage.createPackage();
//            MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();
//            mainDocumentPart.addStyledParagraphOfText(report.getName(), String.valueOf(report.getStatus()));
//            mainDocumentPart.addParagraphOfText(String.valueOf(report.getCreatedAt()));
//            File exportFile = new File(report.getName()+".docx");
//            wordPackage.save(exportFile);
//            return new ResponseEntity<>(HttpStatus.OK);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

}
