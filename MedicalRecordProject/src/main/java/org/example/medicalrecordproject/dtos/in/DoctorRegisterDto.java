package org.example.medicalrecordproject.dtos.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Set;
@Data
@NoArgsConstructor
public class DoctorRegisterDto {
    @NotBlank
    @Length(min = 3, max = 50)
    private String username;
    @NotBlank
    @Length(min = 8, max = 100)
    private String password;
    @NotBlank
    @Length(min = 3, max = 100)
    private String name;
    @NotNull
    private Boolean isGp;
    private Set<Long> specialties;
}