package org.example.medicalrecordproject.dtos.in.creation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatientCreationDto extends UserCreationBaseDto {
    private Long gpId;
}