package org.example.medicalrecordproject.dtos.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorOutDto {
    private Long id;
    private String name;
    private Boolean isGp;
    private List<String> specialties;
    private int patientCount;
}
