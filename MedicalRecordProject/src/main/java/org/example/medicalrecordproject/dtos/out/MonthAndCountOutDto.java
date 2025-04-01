package org.example.medicalrecordproject.dtos.out;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthAndCountOutDto {
    private Integer month;
    private Long count;
}
