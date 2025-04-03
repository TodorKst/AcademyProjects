package org.example.medicalrecordproject.dtos.in;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
public class PatientCreationDto {
    @NotBlank
    @Length(min = 3, max = 50)
    private String username;
    @NotBlank
    @Length(min = 8, max = 100)
    private String password;
    @NotBlank
    @Length(min = 3, max = 100)
    private String name;
    private Long gpId;
}