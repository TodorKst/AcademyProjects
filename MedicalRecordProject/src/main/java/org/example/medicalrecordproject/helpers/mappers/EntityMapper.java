package org.example.medicalrecordproject.helpers.mappers;

import org.example.medicalrecordproject.dtos.in.creation.DiagnosisCreationDto;
import org.example.medicalrecordproject.dtos.in.creation.MedicalVisitCreationDto;
import org.example.medicalrecordproject.dtos.in.creation.SickLeaveCreationDto;
import org.example.medicalrecordproject.dtos.in.creation.SpecialtyCreationDto;
import org.example.medicalrecordproject.dtos.out.response.DiagnosisResponseDto;
import org.example.medicalrecordproject.dtos.out.response.MedicalVisitResponseDto;
import org.example.medicalrecordproject.dtos.out.response.SickLeaveResponseDto;
import org.example.medicalrecordproject.dtos.out.response.SpecialtyResponseDto;
import org.example.medicalrecordproject.models.Diagnosis;
import org.example.medicalrecordproject.models.MedicalVisit;
import org.example.medicalrecordproject.models.SickLeave;
import org.example.medicalrecordproject.models.Specialty;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EntityMapper {

    private final ModelMapper modelMapper;

    public EntityMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Specialty toSpecialty(SpecialtyCreationDto dto) {
        if (dto == null) return null;
        Specialty specialty = modelMapper.map(dto, Specialty.class);
        specialty.setId(null);

        return specialty;
    }

    public SpecialtyResponseDto toSpecialtyDto(Specialty specialty) {
        if (specialty == null) return null;
        return modelMapper.map(specialty, SpecialtyResponseDto.class);
    }

    public List<SpecialtyResponseDto> toSpecialtyDtoList(List<Specialty> specialties) {
        if (specialties == null) return null;
        return specialties.stream()
                .map(this::toSpecialtyDto)
                .toList();
    }


    public SickLeave toSickLeave(SickLeaveCreationDto dto) {
        if (dto == null) return null;
        return modelMapper.map(dto, SickLeave.class);
    }

    public SickLeaveResponseDto toSickLeaveDto(SickLeave sickLeave) {
        if (sickLeave == null) return null;
        return modelMapper.map(sickLeave, SickLeaveResponseDto.class);
    }

    public List<SickLeaveResponseDto> toSickLeaveDtoList(List<SickLeave> sickLeaves) {
        if (sickLeaves == null) return null;
        return sickLeaves.stream()
                .map(this::toSickLeaveDto)
                .toList();
    }


    public MedicalVisit toMedicalVisit(MedicalVisitCreationDto dto) {
        if (dto == null) return null;
        return modelMapper.map(dto, MedicalVisit.class);
    }

    public MedicalVisitResponseDto toMedicalVisitDto(MedicalVisit medicalVisit) {
        if (medicalVisit == null) return null;
        return modelMapper.map(medicalVisit, MedicalVisitResponseDto.class);
    }

    public List<MedicalVisitResponseDto> toMedicalVisitDtoList(List<MedicalVisit> medicalVisits) {
        if (medicalVisits == null) return null;
        return medicalVisits.stream()
                .map(this::toMedicalVisitDto)
                .toList();
    }


    public Diagnosis toDiagnosis(DiagnosisCreationDto dto) {
        if (dto == null) return null;
        return modelMapper.map(dto, Diagnosis.class);
    }

    public DiagnosisResponseDto toDiagnosisDto(Diagnosis diagnosis) {
        if (diagnosis == null) return null;
        return modelMapper.map(diagnosis, DiagnosisResponseDto.class);
    }

    public List<DiagnosisResponseDto> toDiagnosisDtoList(List<Diagnosis> diagnoses) {
        if (diagnoses == null) return null;
        return diagnoses.stream()
                .map(this::toDiagnosisDto)
                .toList();
    }

}
