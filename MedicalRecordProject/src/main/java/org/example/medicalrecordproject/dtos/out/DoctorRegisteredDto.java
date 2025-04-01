package org.example.medicalrecordproject.dtos.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRegisteredDto {
    private Long id;
    private String name;
    private String username;
    private Boolean isGp;
    private List<String> specialties;
    private int patientCount;
}
