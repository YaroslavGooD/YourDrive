package com.your.drive.yourdrive.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
public class User extends DateAudit {

    public User(
            @NotBlank @Email @Size(max = 40) String email,
            @NotBlank @Size(max = 100) String password) {
        this.email = email;
        this.password = password;
    }

    @Id
    @GeneratedValue
    private Long id;

    @JsonIgnore
    @NaturalId
    @NotBlank
    @Email
    @Size(max = 40)
    private String email;

    @JsonIgnore
    @NotBlank
    @Size(max = 100)
    private String password;

}