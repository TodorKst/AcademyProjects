package org.example.medicalrecordproject.dtos.in.creation;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class DoctorCreationDto extends UserCreationBaseDto {
    @NotNull
    private Boolean isGp;
    private Set<Long> specialties;
}