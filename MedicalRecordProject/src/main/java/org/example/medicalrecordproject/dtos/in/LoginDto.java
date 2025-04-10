package org.example.medicalrecordproject.dtos.in;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    @NotBlank
    @Length(min = 3, max = 100)
    private String username;

    @Length(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @NotBlank
    private String password;
}
