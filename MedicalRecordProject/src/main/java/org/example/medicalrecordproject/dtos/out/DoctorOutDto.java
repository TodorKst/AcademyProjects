package org.example.medicalrecordproject.dtos.out;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorOutDto {
    private Long id;
    private String name;
    private Boolean isGp;
    private List<String> specialties;
    private int patientCount;
}
