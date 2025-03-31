package org.example.medicalrecordproject.dtos.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthAndCountOutDto {
    private Integer month;
    private Long count;
}
