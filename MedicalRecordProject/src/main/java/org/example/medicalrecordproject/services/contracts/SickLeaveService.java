package org.example.medicalrecordproject.services.contracts;

import org.example.medicalrecordproject.dtos.in.creation.SickLeaveCreationDto;
import org.example.medicalrecordproject.dtos.out.DoctorStatOutDto;
import org.example.medicalrecordproject.dtos.out.MonthAndCountOutDto;
import org.example.medicalrecordproject.dtos.out.response.SickLeaveResponseDto;
import org.example.medicalrecordproject.exceptions.EntityNotFoundException;
import org.example.medicalrecordproject.models.SickLeave;

import java.util.List;

public interface SickLeaveService {
    List<SickLeaveResponseDto> getAllSickLeaves();

    SickLeave getSickLeaveById(long id) throws EntityNotFoundException;

    SickLeaveResponseDto getSickLeaveByIdResponse(long id) throws EntityNotFoundException;

    SickLeave saveSickLeave(SickLeave sickLeave);

    SickLeaveResponseDto createSickLeave(SickLeaveCreationDto sickLeave);

    void deleteSickLeave(long id);

    void updateSickLeave(long id, SickLeaveCreationDto sickLeave) throws EntityNotFoundException;

    MonthAndCountOutDto getMonthWithMostSickLeaves();

    List<DoctorStatOutDto> getDoctorsWithMostSickLeaves();

}
