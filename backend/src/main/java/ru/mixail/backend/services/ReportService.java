package ru.mixail.backend.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mixail.backend.models.Report;
import ru.mixail.backend.repositories.ReportRepository;

import java.util.List;
import java.util.Optional;

@Service
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
}
