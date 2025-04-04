package org.example.medicalrecordproject.dtos.in.creation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalVisitCreationDto {
    private Long id;
    private String treatment;
    private LocalDateTime visitDate;
    private Long patientId;
    private Long doctorId;
}
