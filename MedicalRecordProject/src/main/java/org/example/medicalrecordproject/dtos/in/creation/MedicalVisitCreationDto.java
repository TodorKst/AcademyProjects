package org.example.medicalrecordproject.dtos.in.creation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalVisitCreationDto {
    private Long id;
    private String treatment;
    private Date visitDate;
    private Long patientId;
    private Long doctorId;
}
