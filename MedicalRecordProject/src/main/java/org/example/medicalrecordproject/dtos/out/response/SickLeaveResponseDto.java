package org.example.medicalrecordproject.dtos.out.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.medicalrecordproject.models.MedicalVisit;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SickLeaveResponseDto {
    private Long id;
    private Date startDate;
    private Date endDate;
    private Date createdAt;
    private MedicalVisit medicalVisit;
}
