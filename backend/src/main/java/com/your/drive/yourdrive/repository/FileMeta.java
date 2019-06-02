package com.your.drive.yourdrive.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

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
}
