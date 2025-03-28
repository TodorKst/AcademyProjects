package org.example.medicalrecordproject.services;

import org.example.medicalrecordproject.models.SickLeave;
import org.example.medicalrecordproject.repositories.SickLeaveRepository;
import org.example.medicalrecordproject.services.contracts.SickLeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SickLeaveServiceImpl implements SickLeaveService {
    private final SickLeaveRepository sickLeaveRepository;

    @Autowired
    public SickLeaveServiceImpl(SickLeaveRepository sickLeaveRepository) {
        this.sickLeaveRepository = sickLeaveRepository;
    }

    @Override
    public List<SickLeave> getAllSickLeaves() {
        return sickLeaveRepository.findAll();
    }

    @Override
    public SickLeave getSickLeaveById(long id) {
        return sickLeaveRepository.findById(id).orElse(null);
    }

    @Override
    public SickLeave saveSickLeave(SickLeave sickLeave) {
        return sickLeaveRepository.save(sickLeave);
    }

    @Override
    public void deleteSickLeave(long id) {
        sickLeaveRepository.deleteById(id);
    }

    @Override
    public void updateSickLeave(long id, SickLeave sickLeave) {
        SickLeave existingSickLeave = sickLeaveRepository.findById(id).orElse(null);
        if (existingSickLeave != null) {
            existingSickLeave.setStartDate(sickLeave.getStartDate());
            existingSickLeave.setEndDate(sickLeave.getEndDate());
            sickLeaveRepository.save(existingSickLeave);
        }
    }
}
