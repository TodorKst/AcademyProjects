package org.example.medicalrecordproject.services.contracts;

import org.example.medicalrecordproject.models.SickLeave;

import java.util.List;

public interface SickLeaveService {
    List<SickLeave> getAllSickLeaves();

    SickLeave getSickLeaveById(long id);

    SickLeave saveSickLeave(SickLeave sickLeave);

    void deleteSickLeave(long id);

    void updateSickLeave(long id, SickLeave sickLeave);
}
