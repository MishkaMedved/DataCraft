package ru.mixail.backend.dto;

import lombok.Value;
import ru.mixail.backend.enums.StatusEnum;

import java.time.LocalDateTime;

/**
 * DTO for {@link ru.mixail.backend.models.Report}
 */
@Value
public class ReportDto {
    String name;
    StatusEnum status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    boolean isFavorite;
}