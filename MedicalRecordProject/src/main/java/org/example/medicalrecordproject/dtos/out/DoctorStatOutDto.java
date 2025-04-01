package org.example.medicalrecordproject.dtos.out;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// used in 2 places to show 2 different statistics for doctors(visits and sick leaves) so the name is generic
public class DoctorStatOutDto {
    private Long doctorId;
    private String doctorName;
    private Long visitCount;
}
