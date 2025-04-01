package org.example.medicalrecordproject.dtos.out;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminRegisteredDto {
    private Long id;
    private String name;
    private String username;
    private String contactInfo;
}
