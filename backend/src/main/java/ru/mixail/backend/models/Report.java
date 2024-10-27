package ru.mixail.backend.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import ru.mixail.backend.enums.StatusEnum;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "report")
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class Report {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "status")
    private StatusEnum status;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "is_favorite")
    private boolean isFavorite;
}
