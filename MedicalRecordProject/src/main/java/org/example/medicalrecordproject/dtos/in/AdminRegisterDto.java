package org.example.medicalrecordproject.dtos.in;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class AdminRegisterDto {
    @NotBlank
    @Length(min = 3, max = 50)
    private String username;
    @NotBlank
    @Length(min = 8, max = 100)
    private String password;
    @NotBlank
    @Length(min = 3, max = 100)
    private String name;
    private String contactInfo;
}