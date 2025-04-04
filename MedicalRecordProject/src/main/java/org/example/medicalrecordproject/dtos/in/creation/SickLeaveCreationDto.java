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
public class SickLeaveCreationDto {
    private Date startDate;
    private Date endDate;
    private Long medicalVisitId;
}
