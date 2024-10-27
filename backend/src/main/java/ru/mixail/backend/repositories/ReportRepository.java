package ru.mixail.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mixail.backend.models.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {

}
