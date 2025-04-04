package org.example.medicalrecordproject.dtos.out.response;

import lombok.*;
import org.example.medicalrecordproject.models.MedicalVisit;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SickLeaveResponseDto {
    private Long id;
    private Date startDate;
    private Date endDate;
    private Date createdAt;
    private Long medicalVisitId;
}
