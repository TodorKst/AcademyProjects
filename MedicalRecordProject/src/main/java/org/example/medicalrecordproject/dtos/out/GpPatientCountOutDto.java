package org.example.medicalrecordproject.dtos.out;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GpPatientCountOutDto {
    private Long gpId;
    private Long patientCount;
}
