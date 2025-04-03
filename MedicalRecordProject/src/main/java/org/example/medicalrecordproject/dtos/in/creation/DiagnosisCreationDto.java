package org.example.medicalrecordproject.dtos.in.creation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisCreationDto {
    private String name;
    private String description;
}
