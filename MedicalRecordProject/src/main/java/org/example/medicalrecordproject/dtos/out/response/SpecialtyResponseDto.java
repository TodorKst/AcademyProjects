package org.example.medicalrecordproject.dtos.out.response;


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
public class SpecialtyResponseDto {
    private Long id;
    @NotBlank
    @Length(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;
}
