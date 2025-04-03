package org.example.medicalrecordproject.dtos.out.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalVisitResponseDto {
    private Long id;
    private String treatment;
    private String visitDate;
    private PatientResponseDto patient;
    private DoctorResponseDto doctor;
    private List<DiagnosisResponseDto> diagnoses;
}
