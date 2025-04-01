package org.example.medicalrecordproject.dtos.out;

import lombok.*;

import java.util.List;

@Getter
@Setter
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
