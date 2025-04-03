package org.example.medicalrecordproject.dtos.out.creationresponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminCreationResponseDto {
    private Long id;
    private String name;
    private String username;
    private String contactInfo;
}
