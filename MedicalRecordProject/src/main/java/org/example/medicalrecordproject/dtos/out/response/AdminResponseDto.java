package org.example.medicalrecordproject.dtos.out.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminResponseDto {
    private Long id;
    private String name;
    private String username;
    private String contactInfo;
}
