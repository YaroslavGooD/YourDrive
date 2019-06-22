package com.your.drive.yourdrive.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class FileMeta {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User owner;

    private String pathKey;

    private String contentType;

    private Long size;

    private LocalDate createdAt;

    private String token = UUID.randomUUID().toString();
}
