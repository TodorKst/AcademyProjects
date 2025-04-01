package org.example.medicalrecordproject.dtos.out;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GpPatientCountOutDto {
    private Long gpId;
    private Long patientCount;
}
