package org.example.medicalrecordproject.services.contracts;

import org.example.medicalrecordproject.dtos.in.creation.MedicalVisitCreationDto;
import org.example.medicalrecordproject.dtos.out.response.MedicalVisitResponseDto;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.models.MedicalVisit;

import java.time.LocalDateTime;
import java.util.List;

public interface MedicalVisitService {
    List<MedicalVisitResponseDto> getAllMedicalVisits();

    MedicalVisit getMedicalVisitById(long id) throws EntityNotFoundException;

    MedicalVisitResponseDto getMedicalVisitByIdResponse(long id) throws EntityNotFoundException;

    MedicalVisit saveMedicalVisit(MedicalVisit medicalVisit);

    public MedicalVisitResponseDto createMedicalVisit(MedicalVisitCreationDto dto);

    void deleteMedicalVisit(long id);

    void updateMedicalVisit(long id, MedicalVisitCreationDto medicalVisit) throws EntityNotFoundException;

    List<MedicalVisitResponseDto> getByPatientId(long id);

    List<MedicalVisitResponseDto> getByDoctorId(long id);

    List<MedicalVisitResponseDto> getByVisitDate(String date);

    List<MedicalVisitResponseDto> getByVisitDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<MedicalVisitResponseDto> getByDateRangeAndDoctor(LocalDateTime start, LocalDateTime end, Long doctorId);

}
